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

FILES:${PN} = "${datadir} ${libdir} /root" 

do_install() {
  
    install -d "${D}"/usr/lib
  
	install -d "${D}"/usr/include
	install -d "${D}"/usr/include/cactus_rt
	install -d "${D}"/usr/include/quill
	install -d "${D}"/usr/include/readerwriterqueue
		
	install -d "${D}"/usr/share
	install -d "${D}"/usr/share/cactus_rt
	install -d "${D}"/usr/share/cactus_rt/examples
		
	cp -r "${S}"/include/cactus_rt "${D}"/usr/include
	cp -r "${B}"/_deps/quill-src/quill/include/quill "${D}"/usr/include
	
	install -m 0644 "${B}"/_deps/readerwriterqueue-src/atomicops.h "${D}"/usr/include/readerwriterqueue
	install -m 0644 "${B}"/_deps/readerwriterqueue-src/readerwriterqueue.h "${D}"/usr/include/readerwriterqueue
	install -m 0644 "${B}"/_deps/readerwriterqueue-src/readerwritercircularbuffer.h "${D}"/usr/include/readerwriterqueue
	
	install -m 0644 "${B}"/libcactus_rt.a "${D}"/usr/lib
	install -m 0644 "${B}"/_deps/quill-build/quill/libquill.a "${D}"/usr/lib

	install -m 0644 "${B}"/libcactus_rt.a "${D}"/root
	install -m 0644 "${B}"/_deps/quill-build/quill/libquill.a "${D}"/root

	find "${B}"/examples -type f -executable -exec cp {} "${D}"/usr/share/cactus_rt/examples \;
	

}