SUMMARY = "Recipe to embedded the Python PiP Package fastapi"
HOMEPAGE ="https://pypi.org/project/fastapi"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=95792ff3fe8e11aa49ceb247e66e4810"

inherit pypi python_poetry_core
PYPI_PACKAGE = "fastapi"
SRC_URI[md5sum] = "1be069a4db82af6acedf769e61c2aaeb"
SRC_URI[sha256sum] = "f93b4ca3529a8ebc6fc3fcf710e5efa8de3df9b41570958abf1d97d843138004"

RDEPENDS:${PN} += "python3-pdm-backend python3-pdm python3-typing-extensions"

inherit pypi python_hatchling