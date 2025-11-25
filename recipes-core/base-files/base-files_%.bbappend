FILESEXTRAPATHS:prepend := "${THISDIR}/base-files:"

SRC_URI += "file://etc/issue \
           file://etc/motd \
		   "

# Include poky distro configuration to get Yocto version variables
include conf/distro/poky.conf

do_install:append(){
	install -d ${D}/etc/
	install -m 0644 ${UNPACKDIR}/etc/issue ${D}/etc/issue
	install -m 0644 ${UNPACKDIR}/etc/motd ${D}/etc/motd
	sed -i "s/YOCTO_VERSION/${DISTRO_VERSION}-${DISTRO_CODENAME}/g" ${D}/etc/issue
}
 