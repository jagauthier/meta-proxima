SUMMARY = "Burn to EMMC"
DESCRIPTION = "Use SD card boot up and copy image to EMMC"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=94d55d512a9ba36caa9b7df079bae19f"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

SRC_URI = "file://root/burn_emmc.sh \
           file://licenses/GPL-2 \
          "

do_install(){
    install -d ${D}/root/
	install -m 755 ${UNPACKDIR}/root/burn_emmc.sh ${D}/root/burn_emmc.sh
}

FILES:${PN} = "/"
