SUMMARY = "RTKLib GPS real-time kinematic open-source software."
DESCRIPTION = "Open-source software to get accurate gps position information."
HOMEPAGE = "http://www.rtklib.com"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PR = "0"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/tomojitakasu/RTKLIB.git \
           file://gumstix-flags.patch \
           file://rtk-conf.patch \ 
          "

S = "${WORKDIR}/git"

do_compile() {
     make -C app/rtkrcv/gcc/
}

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/init.d
    install -d ${D}${sysconfdir}/rtklib
    install -d ${D}${sysconfdir}/rtklib/temp
    install -m 0755 ${S}/app/rtkrcv/gcc/rtkrcv ${D}${bindir}
    install -m 0644 ${S}/data/* ${D}${sysconfdir}/rtklib/
    install -m 0755 ${S}/app/rtkrcv/gcc/rtkstart.sh ${D}${sysconfdir}/rtklib/ 
    install -m 0755 ${S}/app/rtkrcv/gcc/rtkshut.sh ${D}${sysconfdir}/rtklib/ 
    install -m 0644 ${S}/app/rtkrcv/rtk.conf ${D}${sysconfdir}/rtklib/rtkrcv.conf 
}

