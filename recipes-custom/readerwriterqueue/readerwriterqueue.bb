LICENSE = "BSD-Source-Code"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=ee6a511ac75d33a4520d961860f77103"

S = "${WORKDIR}/git"

# Fetch source code from a Git repository
SRC_URI += "git://github.com/cameron314/readerwriterqueue.git;protocol=https;branch=master"
SRCREV="${AUTOREV}"


PACKAGES = "${PN}"
FILES = ""


FILES:${PN} = "${includedir}"

do_install() {

	install -d "${D}"/usr/include/readerwriterqueue
	
	install -m 0644 "${S}"/atomicops.h "${D}"/usr/include/readerwriterqueue
	install -m 0644 "${S}"/readerwriterqueue.h "${D}"/usr/include/readerwriterqueue
	install -m 0644 "${S}"/readerwritercircularbuffer.h "${D}"/usr/include/readerwriterqueue
}

  
