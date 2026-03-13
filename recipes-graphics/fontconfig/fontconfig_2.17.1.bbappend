# Fix parallel build race condition where fcpat.c compilation starts
# before fcalias.h is generated
# Upstream backport: https://gitlab.freedesktop.org/fontconfig/fontconfig/-/commit/0bba79bfd3d8a216794d15af97ce4abefd4b8748
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-build-Added-missing-target-rule-dependencies.patch"
