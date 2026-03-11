SUMMARY = "Systemd service to log EEPROM data at boot"
DESCRIPTION = "A systemd service that runs ghi_eeprom -L with the current date at boot time"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/ghi-eeprom-service:"

SRC_URI = "file://ghi-eeprom-log.service"

inherit systemd

SYSTEMD_SERVICE:${PN} = "ghi-eeprom-log.service"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/ghi-eeprom-log.service ${D}${systemd_system_unitdir}/
}

FILES:${PN} = "${systemd_system_unitdir}/ghi-eeprom-log.service"

RDEPENDS:${PN} = "ghi-eeprom systemd"

pkg_postinst:${PN} () {
    if [ -n "$D" ]; then
        # During rootfs construction, enable the service
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
            systemctl --root=$D enable ghi-eeprom-log.service
        fi
    else
        # On first boot, enable the service
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
            systemctl enable ghi-eeprom-log.service
        fi
    fi
}
