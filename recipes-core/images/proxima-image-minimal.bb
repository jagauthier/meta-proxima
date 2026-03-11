SUMMARY = "A small image just capable of allowing a device to boot."

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image


DEFAULT_TIMEZONE = "America/New_York"


# Misc utils
IMAGE_INSTALL += "openssh e2fsprogs e2fsprogs-resize2fs elfutils i2c-tools slang slang-dev"
IMAGE_INSTALL += "util-linux psutils ntp netcat tcpdump"
IMAGE_INSTALL += "systemd-timesyncd-config tzdata rt-tests mmc-utils usbutils"
IMAGE_INSTALL += "ethtool mdio-tools"
IMAGE_INSTALL += "libgpiod libgpiod-dev libgpiod-tools"
IMAGE_INSTALL += "dosfstools rsync opkg emacs jed"
IMAGE_INSTALL += "nuttcp hdparm k3conf can-utils iproute2 devmem2"
IMAGE_INSTALL += "lmsensors"
IMAGE_INSTALL += "python3 python3-pip"

# GPU stuff
IMAGE_INSTALL += "ti-img-rogue-driver libgbm ti-img-rogue-umlibs mesa-megadriver libegl-mesa libgles2-mesa kernel-modules"

# custom progs
IMAGE_INSTALL += "burn-emmc-full ghi-eeprom"

# services 
IMAGE_INSTALL += "can-config ghi-eeprom-service"

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE:append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "", d)}"


# Enable GPU support
MACHINE_FEATURES += "gpu"