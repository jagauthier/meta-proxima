SUMMARY = "All dev tools, python, docker"

require proxima-image-dev-full.bb

DESCRIPTION = "An image with a full suite of development tools"

DISTRO_FEATURES:append = " virtualization"

# Python stuff
IMAGE_INSTALL:append = " python3 python3-numpy python3-pip python3-pyserial python3-websockets python3-aiohttp"
IMAGE_INSTALL:append = " python3-configargparse python3-matplotlib python3-pycurl"

# IMAGE_INSTALL:append = " python3-fastapi python3-uvicorn python3-starlette python3-gunicorn"

# web stuff
# IMAGE_INSTALL:append = " nginx"

# docker
IMAGE_INSTALL:append = " docker-moby"