SUMMARY = "Base image for Proxima distribution"

# IMAGE_FEATURES += "splash"

LICENSE = "MIT"

inherit core-image

# Set default timezone
DEFAULT_TIMEZONE = "America/New_York"

IMAGE_INSTALL += "can-config"
IMAGE_INSTALL += "systemd-timesyncd-config tzdata"
IMAGE_INSTALL += "ghi-eeprom ghi-eeprom-service"
