SUMMARY = "MAVLink proxy app for AeroDroid (DuoVero) to connect to groundstation."
DESCRIPTION = "Small application for Gumstix AeroDroid UAV autopilot board. The program relays information between UART port and UDP address."
HOMEPAGE = "http://arl.stanford.edu"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "glib-2.0"

PR = "0"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/mavlink/mavlink.git \
           file://mavlink_duovero.c \
           file://aerodroid-mavlink.init \
           file://aerodroid-mavlink.conf "

S = "${WORKDIR}/git"

INITSCRIPT_NAME = "aerodroid-mavlink" 
INITSCRIPT_PARAMS = "defaults 80" 

inherit update-rc.d 

do_configure() {
    python "${S}/pymavlink/generator/mavgen.py" --output=${STAGING_INCDIR}/mavlink --lang=C --wire-protocol=1.0 ${S}/message_definitions/v1.0/common.xml 
}

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} ../mavlink_duovero.c -I ${STAGING_INCDIR}/mavlink/common -I ${STAGING_INCDIR}/glib-2.0/ -I ${STAGING_LIBDIR}/glib-2.0/include/ -lglib-2.0 -o aerodroid-mavlink
}

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 aerodroid-mavlink ${D}${bindir}
    install -m 0755 ${S}/../aerodroid-mavlink.init ${D}${sysconfdir}/init.d/aerodroid-mavlink 
    install -m 0644 ${S}/../aerodroid-mavlink.conf ${D}${sysconfdir}/aerodroid-mavlink.conf
}

