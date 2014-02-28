SOC_FAMILY = "omap4"
require conf/machine/include/soc-family.inc

TARGET_ARCH = "arm"

#DEFAULTTUNE ?= "cortexa9hf-neon"

#change below to a9 if you are building omap4 only images
require conf/machine/include/tune-cortexa9.inc

# Increase this everytime you change something in the kernel
MACHINE_KERNEL_PR = "r0"

KERNEL_IMAGETYPE = "uImage"

UBOOT_ENTRYPOINT = "0x80008000"
UBOOT_LOADADDRESS = "0x80008000"

