SUMMARY = "A development image for use with ARL hardware which includes gstreamer"
LICENSE = "GPLv2"

require ARL-console-image.bb

PR = "0"

GSTREAMER_PACKAGES = " \
    gstreamer1.0 \
    gstreamer1.0-dev \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-base-dev \
    gstreamer1.0-libav \
    gstreamer1.0-libav-dev \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-good-dev \
    libv4l \
    libv4l-dev \
    v4l-utils \
 "

AV_EXTRA = " \
    libav \
    libav-dev \
    libvpx \
    libvpx-dev \
 "

IMAGE_INSTALL += " \
    ${GSTREAMER_PACKAGES} \
    ${AV_EXTRA} \
 "

export IMAGE_BASENAME = "ARL-gst1.0-image"

