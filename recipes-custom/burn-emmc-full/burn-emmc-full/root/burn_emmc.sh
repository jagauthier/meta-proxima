#!/bin/sh

SDCARD=/dev/mmcblk1
EMMC=/dev/mmcblk0

umount /boot

# wipe the partition table
echo "Clearing EMMC partition table."
dd if=/dev/zero of=${EMMC} bs=1024 count=50000 >/dev/null 2>&1 
echo "Recreating partitions."

(
 echo n
 echo p
 echo 1
 echo
 echo 264191
 echo t
 echo c
 echo n
 echo p
 echo 2
 echo
 echo
 echo w ) | fdisk ${EMMC}
 
echo "Copying SD card to EMMC"
dd if=${SDCARD}p1 of=${EMMC}p1 bs=2048
dd if=${SDCARD}p2 of=${EMMC}p2 bs=2048

echo "Resizing"
(
  echo d
  echo 2
  echo n
  echo p
  echo 2
  echo 
  echo
  echo w ) | fdisk ${EMMC}

  e2fsck -y -f ${EMMC}p2
  resize2fs ${EMMC}p2

echo "Done."







