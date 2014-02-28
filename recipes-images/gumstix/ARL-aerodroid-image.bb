SUMMARY = "A development image for use with ARL hardware which includes gstreamer and AeroDroid applications."
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

UAV_EXTRA = " \
    aerodroid-mavlink \
    rtklib-rtkrcv \
 "
#    inetutils 

IMAGE_INSTALL += " \
    ${UAV_EXTRA} \
 "
#    ${GSTREAMER_PACKAGES} 
#    ${AV_EXTRA} 

export IMAGE_BASENAME = "ARL-aerodroid-image"

