#define _XOPEN_SOURCE 700
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <unistd.h>
#include <fcntl.h>
#include <time.h>
#include <getopt.h>
#include <arpa/inet.h>

#define EEPROM_PATH "/sys/bus/i2c/devices/0-0050/eeprom"
#define FIXED_NAME "GoHypersonic, Inc. Warp-Velocity Adaptable Research Platform"
#define HEADER_MAGIC {0x88, 0x67, 0x31, 0x65}

/*
 * Warp Autonomous Research Platform
 * Warp-Velocity Adaptable Research Platform
 * Or Response
 */

// Structure based on TI AM6 board_detect.h
struct __attribute__((packed)) ghi_eeprom_record_board_id {
    uint8_t header[4];              // 0x88, 0x67, 0x31, 0x65
    uint8_t name[128];              // Board name (fixed to "Insert Text Here")
    uint8_t serial[32];             // Serial number
    uint32_t boot_flags;            // Boot configuration flags
    uint32_t flags;                 // General flags
    uint32_t boot_count;            // Total boot count
    uint32_t power_on_count;        // Total power on count
    uint8_t last_boot[64];          // Last boot timestamp (formatted)
    uint8_t manufacture_date[32];   // Manufacture date
    uint8_t board_revision[16];     // PCB revision
    uint8_t part_number[32];        // SKU/part number
	uint8_t boot_device[8];         // Boot device string
};

// Function prototypes
int read_eeprom(struct ghi_eeprom_record_board_id *data);
int write_eeprom(struct ghi_eeprom_record_board_id *data);
void print_eeprom_data(struct ghi_eeprom_record_board_id *data);
void zero_eeprom(struct ghi_eeprom_record_board_id *data);
void convert_timestamp_to_string(time_t timestamp, char *buffer, size_t buffer_size);
void print_usage(const char *prog_name);
int is_valid_header(const struct ghi_eeprom_record_board_id *data);
int is_valid_timestamp_string(const char *timestamp_str);

int main(int argc, char *argv[]) {
    struct ghi_eeprom_record_board_id board_data;
    int opt;
    int modified = 0;
    int zero_mode = 0;
    
    // Command line options
    static struct option long_options[] = {
        {"serial",        required_argument, 0, 's'},
        {"boot-flags",    required_argument, 0, 'b'},
        {"flags",         required_argument, 0, 'f'},
        {"boot-count",    required_argument, 0, 'c'},
        {"last-boot",     required_argument, 0, 'L'},
        {"manufacture-date", required_argument, 0, 'm'},
        {"board-revision",required_argument, 0, 'r'},
        {"part-number",   required_argument, 0, 'P'},
        {"set-header",    no_argument,       0, 'H'},
        {"zero",          no_argument,       0, 'z'},
        {"help",          no_argument,       0, 'h'},
        {0, 0, 0, 0}
    };

    // Read existing EEPROM data
    if (read_eeprom(&board_data) != 0) {
        // If read fails, initialize with defaults
        printf("Warning: Could not read EEPROM, initializing with defaults\n");
        uint8_t magic[] = HEADER_MAGIC;
        memcpy(board_data.header, magic, sizeof(magic));
        memset(board_data.name, 0, sizeof(board_data.name));
        strncpy((char*)board_data.name, FIXED_NAME, sizeof(board_data.name) - 1);
        memset(board_data.serial, 0, sizeof(board_data.serial));
        board_data.boot_flags = 0;
        board_data.flags = 0;
        board_data.boot_count = 0;
        board_data.power_on_count = 0;
        memset(board_data.last_boot, 0, sizeof(board_data.last_boot));
        memset(board_data.manufacture_date, 0, sizeof(board_data.manufacture_date));
        memset(board_data.board_revision, 0, sizeof(board_data.board_revision));
        memset(board_data.part_number, 0, sizeof(board_data.part_number));
    }

    // Parse command line arguments
    while ((opt = getopt_long(argc, argv, "s:b:f:c:p:L:m:r:P:Hzh", long_options, NULL)) != -1) {
        modified = 1;
        
        switch (opt) {
            case 's': // Serial number
                memset(board_data.serial, 0, sizeof(board_data.serial));
                strncpy((char*)board_data.serial, optarg, sizeof(board_data.serial) - 1);
                printf("Set serial: %s\n", optarg);
                break;
                
            case 'b': // Boot flags
                board_data.boot_flags = strtoul(optarg, NULL, 0);
                printf("Set boot flags: 0x%08x\n", board_data.boot_flags);
                break;
                
            case 'f': // Flags
                board_data.flags = strtoul(optarg, NULL, 0);
                printf("Set flags: 0x%08x\n", board_data.flags);
                break;
                
            case 'c': // Boot count
                board_data.boot_count = strtoul(optarg, NULL, 0);
                printf("Set boot count: %u\n", board_data.boot_count);
                break;
                
            case 'L': // Last boot (string)
                memset(board_data.last_boot, 0, sizeof(board_data.last_boot));
                // Check if timestamp is valid (not from 1969/1970 era when time hasn't synced)
                if (is_valid_timestamp_string(optarg)) {
                    strncpy((char*)board_data.last_boot, optarg, sizeof(board_data.last_boot) - 1);
                    printf("Set last boot: %s\n", optarg);
                } else {
                    strncpy((char*)board_data.last_boot, "[Offline boot]", sizeof(board_data.last_boot) - 1);
                    printf("Invalid timestamp detected, using: [Offline boot]\n");
                }
                board_data.boot_count++;
                printf("Incremented boot count to: %u\n", board_data.boot_count);
                break;
                
            case 'm': // Manufacture date
                memset(board_data.manufacture_date, 0, sizeof(board_data.manufacture_date));
                strncpy((char*)board_data.manufacture_date, optarg, sizeof(board_data.manufacture_date) - 1);
                printf("Set manufacture date: %s\n", optarg);
                break;
                
            case 'r': // Board revision
                memset(board_data.board_revision, 0, sizeof(board_data.board_revision));
                strncpy((char*)board_data.board_revision, optarg, sizeof(board_data.board_revision) - 1);
                printf("Set board revision: %s\n", optarg);
                break;
                
            case 'P': // Part number
                memset(board_data.part_number, 0, sizeof(board_data.part_number));
                strncpy((char*)board_data.part_number, optarg, sizeof(board_data.part_number) - 1);
                printf("Set part number: %s\n", optarg);
                break;
                
            case 'H': // Set header to magic number
                {
                    uint8_t magic[] = HEADER_MAGIC;
                    memcpy(board_data.header, magic, sizeof(magic));
                    printf("Header written. All data should be valid.\n");
                }
                break;
                
            case 'z': // Zero out EEPROM
                zero_mode = 1;
                printf("Zeroing EEPROM structure\n");
                break;
                
            case 'h': // Help
                print_usage(argv[0]);
                return 0;
                
            default:
                print_usage(argv[0]);
                return 1;
        }
    }

    // Handle zero mode
    if (zero_mode) {
        zero_eeprom(&board_data);
        // Set header and name when zeroing
        uint8_t magic[] = HEADER_MAGIC;
        memcpy(board_data.header, magic, sizeof(magic));
        memset(board_data.name, 0, sizeof(board_data.name));
        strncpy((char*)board_data.name, FIXED_NAME, sizeof(board_data.name) - 1);
    } else {
        // Ensure name is always set to fixed value
        memset(board_data.name, 0, sizeof(board_data.name));
        strncpy((char*)board_data.name, FIXED_NAME, sizeof(board_data.name) - 1);
    }

    // If no arguments were provided, just print the data
    if (!modified && !zero_mode) {
        // Check if header is valid before printing
        if (!is_valid_header(&board_data)) {
            fprintf(stderr, "Error: Invalid EEPROM data.\n");
            return 1;
        }
        print_eeprom_data(&board_data);
        return 0;
    }

    // Write the modified structure back to EEPROM
    if (write_eeprom(&board_data) != 0) {
        return 1;
    }

    return 0;
}

int read_eeprom(struct ghi_eeprom_record_board_id *data) {
    int fd = open(EEPROM_PATH, O_RDONLY);
    if (fd < 0) {
        return -1;
    }

    ssize_t bytes_read = pread(fd, data, sizeof(*data), 0);
    close(fd);

    if (bytes_read != sizeof(*data)) {
        return -1;
    }

    // Convert uint32_t fields from big-endian (EEPROM) to host byte order
    data->boot_flags = ntohl(data->boot_flags);
    data->flags = ntohl(data->flags);
    data->boot_count = ntohl(data->boot_count);
    data->power_on_count = ntohl(data->power_on_count);

    return 0;
}

int write_eeprom(struct ghi_eeprom_record_board_id *data) {
    int fd = open(EEPROM_PATH, O_WRONLY);
    if (fd < 0) {
        perror("Error opening EEPROM file (are you root?)");
        return -1;
    }

    // Convert uint32_t fields to big-endian for EEPROM storage
    struct ghi_eeprom_record_board_id temp = *data;
    temp.boot_flags = htonl(data->boot_flags);
    temp.flags = htonl(data->flags);
    temp.boot_count = htonl(data->boot_count);
    temp.power_on_count = htonl(data->power_on_count);

    if (pwrite(fd, &temp, sizeof(temp), 0) != sizeof(temp)) {
        perror("Error writing to EEPROM");
        close(fd);
        return -1;
    }

    close(fd);
    return 0;
}

void print_eeprom_data(struct ghi_eeprom_record_board_id *data) {
    printf("=== EEPROM Board ID Data ===\n");
    printf("Name: %s\n", data->name);
    printf("Serial: %s\n", data->serial);
    printf("Boot Flags: 0x%08x\n", data->boot_flags);
    printf("Flags: 0x%08x\n", data->flags);
    printf("Boot Count: %u\n", data->boot_count);
    printf("Power On Count: %u\n", data->power_on_count);
    printf("Last Boot: %s\n", data->last_boot);
    printf("Manufacture Date: %s\n", data->manufacture_date);
    printf("Board Revision: %s\n", data->board_revision);
    printf("Part Number: %s\n", data->part_number);
    printf("Boot device: %s\n", data->boot_device);
    printf("============================\n");
}

void zero_eeprom(struct ghi_eeprom_record_board_id *data) {
    memset(data, 0, sizeof(*data));
}

int is_valid_header(const struct ghi_eeprom_record_board_id *data) {
    const uint8_t expected[] = HEADER_MAGIC;
    return memcmp(data->header, expected, sizeof(expected)) == 0;
}

void convert_timestamp_to_string(time_t timestamp, char *buffer, size_t buffer_size) {
    struct tm *tm_info = localtime(&timestamp);
    
    if (tm_info == NULL) {
        strncpy(buffer, "Invalid Timestamp", buffer_size - 1);
        buffer[buffer_size - 1] = '\0';
        return;
    }

    // Format: "DayOfWeek Month Day Year" (e.g., "Monday March 10 2025")
    const char *days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", 
                          "Thursday", "Friday", "Saturday"};
    const char *months[] = {"January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};

    snprintf(buffer, buffer_size, "%s %s %d %d",
             days[tm_info->tm_wday],
             months[tm_info->tm_mon],
             tm_info->tm_mday,
             tm_info->tm_year + 1900);
}

int is_valid_timestamp_string(const char *timestamp_str) {
    struct tm tm = {0};
    char *result = strptime(timestamp_str, "%a %b %d %H:%M:%S %Z %Y", &tm);
    
    if (result == NULL) {
        // Try alternative format without timezone
        result = strptime(timestamp_str, "%a %b %d %H:%M:%S %Y", &tm);
        if (result == NULL) {
            return 0; // Invalid format
        }
    }
    
    // Convert to time_t
    time_t timestamp = mktime(&tm);
    if (timestamp == (time_t)-1) {
        return 0; // Invalid time
    }
    
    // Check if timestamp is from 1969/1970 era (time not synced)
    // Use a threshold of year 2000 as a reasonable cutoff
    struct tm *tm_info = localtime(&timestamp);
    if (tm_info->tm_year < 100) { // tm_year is years since 1900, so 100 = year 2000
        return 0; // Invalid year (before 2000)
    }
    
    return 1; // Valid timestamp
}

void print_usage(const char *prog_name) {
    printf("Usage: %s [OPTIONS]\n", prog_name);
    printf("\nOptions:\n");
    printf("  -s, --serial STRING              Set serial number\n");
    printf("  -b, --boot-flags HEX             Set boot flags (hex)\n");
    printf("  -f, --flags HEX                  Set general flags (hex)\n");
    printf("  -c, --boot-count INT             Set boot count\n");
    printf("  -p, --power-on-count INT         Set power on count\n");
    printf("  -L, --last-boot STRING           Set last boot (date string)\n");
    printf("  -m, --manufacture-date STRING    Set manufacture date\n");
    printf("  -r, --board-revision STRING      Set board revision\n");
    printf("  -P, --part-number STRING         Set part number\n");
    printf("  -H, --set-header                 Set header to magic number\n");
    printf("  -z, --zero                       Zero out the entire EEPROM structure\n");
    printf("  -h, --help                       Display this help message\n");
    printf("\nExamples:\n");
    printf("  %s                                    # Print current EEPROM contents\n", prog_name);
    printf("  %s --serial \"ABC123\" --boot-count 5   # Update multiple fields\n", prog_name);
    printf("  %s --last-boot \"Mon Mar 10 00:00:00 UTC 2025\"  # Set last boot string\n", prog_name);
    printf("  %s --set-header                       # Set header to magic number\n", prog_name);
    printf("  %s --zero                             # Zero out EEPROM\n", prog_name);
}
