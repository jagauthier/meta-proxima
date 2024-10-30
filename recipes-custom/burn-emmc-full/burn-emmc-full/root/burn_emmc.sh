#!/bin/sh

# Want to clear the boot partition
BOOT_SIZE=33423360


BOOT_PART=mmcblk0boot0
BOOT_FILES=/boot
SDCARD=/dev/mmcblk1
EMMC=/dev/mmcblk0


setup_boot(){
 
  echo "Copying necassary boot files to /dev/${BOOT_PART}"

  # Turn off RO mode
  echo 0 > /sys/block/${BOOT_PART}/force_ro

  dd if=${BOOT_FILES}/tiboot3.bin of=/dev/${BOOT_PART} bs=512 seek=0
  dd if=${BOOT_FILES}/tispl.bin of=/dev/${BOOT_PART} bs=512 seek=1024
  dd if=${BOOT_FILES}/u-boot.img  of=/dev/${BOOT_PART} bs=512 seek=5120
  sync
}


create_partitions(){
   # Avoid the first 100M, because mmcblk0boot0 is there
   # will all the important boot files
   BOOT_ROM_SIZE=100
   # this is on /boot
   BOOT_PART_SIZE=128

   # Clear the partition (6528*5120) = the size of the partition
   echo "Zeroing..."
   dd if=/dev/zero of=${EMMC} bs=6528 count=$((BOOT_SIZE/(6528*2)))

   echo "Creating new partitions."
   sfdisk --force ${EMMC} <<EOF
     ${BOOT_ROM_SIZE}M,${BOOT_PART_SIZE}M,0c
     $(($BOOT_PART_SIZE + 10))M,,83
EOF

}

copy_boot_files() {
    echo "Copying ${SDCARD}p1 to ${EMMC}p1..."
    dd if=${SDCARD}p1 of=${EMMC}p1 bs=2M
}


copy_file_system() {
  echo "Copying ${SDCARD}p2 to ${EMMC}p2..."
  dd if=${SDCARD}p2 of=${EMMC}p2 bs=2M
}

resize_emmc() {
    e2fsck -y -f ${EMMC}p2
    resize2fs ${EMMC}p2
    sync
}

enable_bootpart(){
    mmc bootpart enable 1 1 ${EMMC}
    mmc bootbus set single_backward x1 x8 ${EMMC}
    mmc hwreset enable ${EMMC}
    mmc extcsd read ${EMMC} | grep RST
}


# may not be mounted
umount_emmc()
{
  umount /run/media/mmcblk0p1 2>/dev/null
  umount /run/media/mmcblk0p2 2>/dev/null
  
}

check_boot_device()
{
 mount |grep "${EMMC}p2 on / type" > /dev/null
 if [ $? -eq 0 ]; then
   echo "You are booted from EMMC.  This should be run when booted from SD card."
   exit 0
fi
}

check_boot_device
umount_emmc
create_partitions
copy_boot_files
setup_boot
copy_file_system
resize_emmc
enable_bootpart