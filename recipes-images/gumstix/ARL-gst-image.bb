SUMMARY = "A development image for use with ARL hardware which includes gstreamer"
LICENSE = "GPLv2"

require ARL-console-image.bb

PR = "0"

GSTREAMER_PACKAGES = " \
    gstreamer \
    gstreamer-dev \
    gst-plugins-bad \
    gst-plugins-bad-dev \
    gst-plugins-base-meta \
    gst-plugins-base-dev \
    gst-plugins-good-meta \
    gst-plugins-good-dev \
    gst-plugins-bad-meta \
    gst-plugins-bad-dev \
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

export IMAGE_BASENAME = "ARL-gst-image"

