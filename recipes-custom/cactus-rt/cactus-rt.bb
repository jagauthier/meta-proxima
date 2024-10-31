LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=815ca599c9df247a0c7f619bab123dad"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE += "-DFETCHCONTENT_FULLY_DISCONNECTED=FALSE -DBUILD_TESTING=OFF -DENABLE_EXAMPLES=ON -DBUILD_DOCS=OFF -DENABLE_TRACING=ON -DCMAKE_BUILD_TYPE=Release" 

# Add protobuf-native and protobuf as dependencies
DEPENDS = "googletest googlebenchmark protobuf-native protobuf"
# Fetch source code from a Git repository

SRC_URI += "git://github.com/cactusdynamics/cactus-rt.git;protocol=https;branch=master"
SRCREV="${AUTOREV}"

do_install() {
  
	cd ${B}
	cmake -P cmake_install.cmake

}