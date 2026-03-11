SUMMARY = "EEPROM configuration utility for GHI boards"
DESCRIPTION = "A command-line utility to read and write EEPROM data on GHI boards, \
including serial number, boot flags, boot count, power-on count, manufacture date, \
board revision, and part number."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://ghi_eeprom.c \
           file://Makefile"

S = "${UNPACKDIR}"

do_compile() {
    oe_runmake -C ${S}
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/ghi_eeprom ${D}${bindir}/ghi_eeprom
}

FILES:${PN} = "${bindir}/ghi_eeprom"
