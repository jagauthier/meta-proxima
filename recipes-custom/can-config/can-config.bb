SUMMARY = "Configure CAN interface at boot"
DESCRIPTION = "Systemd-networkd configuration for can0 interface with FD CAN support"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://10-can0.network"

do_install() {
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${UNPACKDIR}/10-can0.network ${D}${sysconfdir}/systemd/network/
}

FILES:${PN} += "${sysconfdir}/systemd/network/10-can0.network"

RDEPENDS:${PN} = "systemd"
