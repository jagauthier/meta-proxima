LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=11e8c8dbfd5fa373c703de492140ff7a"

SRC_URI[md5sum] = "d1e8eacb1f30d20fff56549a75274fa2"
SRC_URI[sha256sum] = "9af890290133b79fc3db55474ade20f6220a364a0402e0b556e7cd5e1e093823"

RDEPENDS:${PN} += "python3-anyio python3-typing-extensions"

PYPI_PACKAGE = "starlette"
inherit pypi python_hatchling
