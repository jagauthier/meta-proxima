# Makefile for ghi_eeprom
# Uses CC from environment (set by BitBake for cross-compilation)

CC ?= gcc
CFLAGS = -Wall -Wextra -O2
LDFLAGS ?=
TARGET = ghi_eeprom
SOURCE = ghi_eeprom.c

# Default target
all: $(TARGET)

# Build the executable
$(TARGET): $(SOURCE)
	$(CC) $(CFLAGS) $(LDFLAGS) -o $(TARGET) $(SOURCE)

# Clean build artifacts
clean:
	rm -f $(TARGET)

# Rebuild
rebuild: clean all

.PHONY: all clean rebuild
