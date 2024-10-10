FILESEXTRAPATHS:prepend := "${THISDIR}/base-files:"

SRC_URI += "file://etc/issue \
           file://etc/motd \
		   "


do_install:append(){
	install -d ${D}/etc/
	install -m 0644 ${UNPACKDIR}/etc/issue ${D}/etc/issue
	install -m 0644 ${UNPACKDIR}/etc/motd ${D}/etc/motd
}
 