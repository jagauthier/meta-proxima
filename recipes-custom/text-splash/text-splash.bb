SUMMARY = "Issue splash"
DESCRIPTION = "For customized issue/issue.net"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=94d55d512a9ba36caa9b7df079bae19f"

S = "${WORKDIR}"

PROVIDES += "virtual/issue"
PROVIDES += "virtual/issue-net"

SRC_URI = "file://etc/issue \
		  file://licenses/GPL-2 \
		  "
	

do_install:append(){

	install -d ${D}/etc/
	install -m 0644 ${WORKDIR}/etc/issue ${D}/etc/issue
	install -m 0644 ${WORKDIR}/etc/issue ${D}/etc/issue.net
}
 
                                                                              
                                                                              