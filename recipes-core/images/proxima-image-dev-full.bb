SUMMARY = "All dev tools for local building including python and python modules."

require proxima-image-dev.bb

DESCRIPTION = "An image with a full suite of development tools"

# Python stuff
IMAGE_INSTALL:append = " python3 python3-numpy python3-pip python3-pyserial python3-websockets python3-aiohttp"
IMAGE_INSTALL:append = " python3-configargparse python3-matplotlib python3-pycurl"

# IMAGE_INSTALL:append = " python3-fastapi python3-uvicorn python3-starlette python3-gunicorn"
