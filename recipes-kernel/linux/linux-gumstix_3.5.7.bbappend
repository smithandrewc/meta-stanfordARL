FILESEXTRAPATHS_prepend := "${THISDIR}/linux-gumstix-3.5:"

# PRINC := "${@int(PRINC) + 1}"

SRC_URI += "file://defconfig"
# SRC_URI += "file://menuconfig.cfg"
SRC_URI += "file://robovero-gpio-board-overo.patch"

