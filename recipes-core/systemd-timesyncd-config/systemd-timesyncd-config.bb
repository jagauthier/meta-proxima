SUMMARY = "Enable systemd-timesyncd service for NTP synchronization"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/systemd-timesyncd-config:"

SRC_URI = "file://50-ntp-servers.conf"

inherit systemd

do_install() {
    install -d ${D}${sysconfdir}/systemd/timesyncd.conf.d
    install -m 0644 ${UNPACKDIR}/50-ntp-servers.conf ${D}${sysconfdir}/systemd/timesyncd.conf.d/
}

FILES:${PN} = "${sysconfdir}/systemd/timesyncd.conf.d/50-ntp-servers.conf"

RDEPENDS:${PN} = "systemd"

pkg_postinst:${PN} () {
    if [ -n "$D" ]; then
        # During rootfs construction, enable the service
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
            systemctl --root=$D enable systemd-timesyncd.service
        fi
    else
        # On first boot, enable and restart the service to pick up new config
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
            systemctl enable systemd-timesyncd.service
            systemctl restart systemd-timesyncd.service
        fi
    fi
}
