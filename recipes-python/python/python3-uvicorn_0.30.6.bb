LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=5c778842f66a649636561c423c0eec2e"

SRC_URI[md5sum] = "d75153180f8c19a1bf8299818d3acdb2"
SRC_URI[sha256sum] = "4b15decdda1e72be08209e860a1e10e92439ad5b97cf44cc945fcbee66fc5788"

RDEPENDS:${PN} += "python3-click python3-h11"

PYPI_PACKAGE = "uvicorn"
inherit pypi python_hatchling

