require proxima-image-minimal.bb

DESCRIPTION = "A small image just capable of allowing a device to boot and \
is suitable for development work."

IMAGE_FEATURES += "dev-pkgs tools-sdk tools-debug tools-sdk"

IMAGE_INSTALL += "make cmake strace gdb nano git"
