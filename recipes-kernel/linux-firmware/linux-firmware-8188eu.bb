SUMMARY = "RTKLib GPS real-time kinematic open-source software."
DESCRIPTION = "Open-source software to get accurate gps position information."
HOMEPAGE = "http://www.rtklib.com"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PR = "0"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/lwfinger/rtl8188eu.git \
          "

S = "${WORKDIR}/git"

#inherit autotools

#do_compile() {
#     make -C app/rtkrcv/gcc/
#}
#
#do_install() {
#}

do_compile() {
	echo "Kernel version = ${KERNEL_VERSION}"
#	make scripts 
#	make ARCH=arm CROSS_COMPILE=arm-poky-linux-gnueabi- -C ${STAGING_DIR}/${MACHINE}/usr/src/kernel modules_prepare 
	make ARCH=arm CROSS_COMPILE=arm-poky-linux-gnueabi- -C ${STAGING_DIR}/${MACHINE}/usr/src/kernel scripts 
	make ARCH=arm CROSS_COMPILE=arm-poky-linux-gnueabi- KSRC=${STAGING_DIR}/${MACHINE}/usr/src/kernel all
#	export ARCH=arm 
#	export CROSS_COMPILE=arm-poky-linux-gnueabi-
#	make modules
#	make ARCH=arm CROSS_COMPILE=arm-poky-linux-gnueabi- -C ${STAGING_DIR}/${MACHINE}/usr/src/kernel modules 
}

do_install() {
	#MODDESTDIR = /lib/modules/$(KERNEL_VERSION)/kernel/drivers/net/wireless
	#install -p -D -m 644 8188eu.ko  $(MODDESTDIR)/8188eu.ko
#  install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/${PN}
#  install -m 0644 hrt${KERNEL_OBJECT_SUFFIX} ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/${PN}

	#install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/net/wireless
	install -d ${D}${base_libdir}/modules/3.6.0/kernel/net/wireless
	#install -m 644 8188eu.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/net/wireless/8188eu.ko
	install -m 644 8188eu.ko ${D}${base_libdir}/modules/3.6.0/kernel/net/wireless/8188eu.ko

	#/sbin/depmod -a ${KVER}
	depmod -a

	#mkdir -p /lib/firmware/rtlwifi
	#cp -n rtl8188eufw.bin /lib/firmware/rtlwifi/.
	install -d ${D}${base_libdir}/firmware/rtlwifi
	install -m 644 rtl8188eufw.bin ${D}${base_libdir}/firmware/rtlwifi/rtl8188eufw.bin 
}

FILES_${PN} += "${base_libdir}/modules/3.6.0/kernel/net/wireless/8188eu.ko \
		${base_libdir}/firmware/rtlwifi/rtl8188eufw.bin \
		${base_libdir}/* \
		"
