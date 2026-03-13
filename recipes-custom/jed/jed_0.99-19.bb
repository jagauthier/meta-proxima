SUMMARY = "Jed is a powerful editor for programmers"

DESCRIPTION = "Jed is a freely available text editor for Unix, VMS, MSDOS, OS/2, \
BeOS, QNX, and win9X/NT platforms. Although it is a powerful editor designed \
for use by programmers, its drop-down menu facility make it attractive to \
novices. Jed supports emulation of the Emacs, EDT, WordStar and Brief editors."

HOMEPAGE = "http://www.jedsoft.org/jed/"
SECTION = "console/editors"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=18810669f13b87348459e611d31ab760"

SRC_URI = "file://${THISDIR}/binary/jed-0.99-19.tar.gz"

PV = "0.99-19"

S = "${UNPACKDIR}/jed-${PV}"

DEPENDS = "slang ncurses"
RDEPENDS:${PN} = "slang (>= 2.3.3) ncurses-libtinfo"

inherit bin_package

# Pre-built binary package - just install it
do_install() {
    # Create directories - jed expects files in /usr/local/jed/lib/
    install -d ${D}${prefix}/local/bin
    install -d ${D}${prefix}/local/jed/lib
    install -d ${D}${prefix}/local/share/man/man1
    
    # Install jed binary
    install -m 0755 ${S}/src/objs/jed ${D}${prefix}/local/bin/jed
    
    # Create xjed symlink for convenience
    ln -sf jed ${D}${prefix}/local/bin/xjed
    
    # Install jed data files to /usr/local/jed/lib/ (where jed expects them)
    cp -r ${S}/lib/* ${D}${prefix}/local/jed/lib/
    cp -r ${S}/info/* ${D}${prefix}/local/jed/lib/
    cp -r ${S}/doc/txt/* ${D}${prefix}/local/jed/lib/
    cp -r ${S}/doc/hlp/* ${D}${prefix}/local/jed/lib/
    cp -r ${S}/doc/manual/* ${D}${prefix}/local/jed/lib/
    cp -r ${S}/doc/manual/jed.1 ${D}${prefix}/local/share/man/man1/
}

FILES:${PN} = "${prefix}/local/bin/jed ${prefix}/local/bin/xjed ${prefix}/local/jed ${prefix}/local/share/man"
FILES:${PN}-doc += "${prefix}/local/jed/lib/doc"
