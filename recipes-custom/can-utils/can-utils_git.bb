SUMMARY = "Linux-CAN / SocketCAN user space utilities"
DESCRIPTION = "This is a set of command line utilities for Linux-CAN / SocketCAN."
HOMEPAGE = "https://github.com/linux-can/can-utils"
SECTION = "console/network"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSES/GPL-2.0-only.txt;md5=f9d20a453221a1b7e32ae84694da2c37"

SRC_URI = "git://github.com/linux-can/can-utils.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

inherit pkgconfig

EXTRA_OEMAKE = "CC='${CC}' CFLAGS='${CFLAGS}' LDFLAGS='${LDFLAGS}' PREFIX=${prefix} DESTDIR=${D}"

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake install
}

PACKAGES = "${PN} ${PN}-dbg"

FILES:${PN} = "${bindir}/*"
FILES:${PN}-dbg = "${bindir}/.debug/*"