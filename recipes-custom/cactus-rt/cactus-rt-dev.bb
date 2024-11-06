LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=815ca599c9df247a0c7f619bab123dad"

S = "${WORKDIR}/git"

PACKAGES = "${PN}-staticdev"

FILES:${PN}-staticdev = "${datadir}/libcactus_rt.a ${datadir}/libquill.a"

do_install() {
  
    install -m 0644 "${B}"/libcactus_rt.a ${D}${libdir}
    install -m 0644 "${B}"/_deps/quill-build/quill/libquill.a ${D}${libdir}

}