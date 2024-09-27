SUMMARY = "A small image just capable of allowing a device to boot."

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image


DEFAULT_TIMEZONE = "America/New_York"

IMAGE_INSTALL += "kernel-modules"


# Misc utils
IMAGE_INSTALL += "openssh e2fsprogs e2fsprogs-resize2fs elfutils i2c-tools"
IMAGE_INSTALL += "util-linux psutils ntp netcat tcpdump"
IMAGE_INSTALL += "tzdata rt-tests mmc-utils"


# custom progs
IMAGE_INSTALL += "burn-emmc-full"

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE:append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "", d)}"
