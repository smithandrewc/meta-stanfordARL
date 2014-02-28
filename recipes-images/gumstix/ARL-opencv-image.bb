SUMMARY = "A development image for use with ARL hardware which includes opencv"
LICENSE = "GPLv2"

require ARL-console-image.bb

PR = "0"

OPENCV_PACKAGES = " \
    opencv \
 "
AV_EXTRA = " \
    libv4l \
    libv4l-dev \
    v4l-utils \
    libav \
    libav-dev \
    libvpx \
    libvpx-dev \
 "

IMAGE_INSTALL += " \
    ${OPENCV_PACKAGES} \
    ${AV_EXTRA} \
 "

export IMAGE_BASENAME = "ARL-opencv-image"

