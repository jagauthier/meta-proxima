SUMMARY = "Burn to EMMC"
DESCRIPTION = "Use SD card boot up and copy image to EMMC"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=94d55d512a9ba36caa9b7df079bae19f"

inherit  systemd

S = "${WORKDIR}"

SRC_URI = "file://home/root/burn_emmc.sh \
           file://licenses/GPL-2 \
          "

do_install(){

	install -d ${D}/home/root/
	install -m 755 ${WORKDIR}/home/root/burn_emmc.sh ${D}/home/root/burn_emmc.sh
}

FILES:${PN} = "/"
