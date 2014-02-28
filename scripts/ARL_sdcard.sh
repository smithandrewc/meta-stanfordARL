#!/bin/bash

if [ "x${1}" = "x" ]; then
	echo -e "\nUsage: ${0} <block device> [<console> <hostname> <interfaces>]\n"
	exit 0
fi

if [ -z "$OETMP" ]; then
	#OETMP="../../../build/tmp" 
	OETMP="~/yocto-dylan-2/build/tmp"
	OETMP="/home/andrew/yocto-dylan-2/build/tmp"
	SCRTMP="/home/andrew/yocto-dylan-2/poky/meta-stanford-ARL/scripts"
	echo -e "\nWorking from default directory: $OETMP"
else
	echo -e "\nOETMP: $OETMP"

	if [ ! -d ${OETMP}/deploy/images ]; then
		echo "Directory not found: ${OETMP}/deploy/images"
		exit 1
	fi
fi 

if [[ -z "${MACHINE}" ]]; then
	echo "Environment variable MACHINE not found!"
	echo "By default using overo"
	MACHINE=overo
#	echo "Choices are overo|duovero|beaglebone"
#	exit 1
else
	echo -e "MACHINE: $MACHINE\n"
fi

if [ "x${3}" = "x" ]; then
        TARGET_HOSTNAME=$MACHINE
else
	MACHINE=${3}
        TARGET_HOSTNAME=${3}
fi

if [ ! -z "$OETMP" ]; then
	cd ${OETMP}/deploy/images
fi

if [ ! -f MLO-${MACHINE} ]; then
	echo -e "File not found: MLO-${MACHINE}\n"
 
	if [ ! -z "$OETMP" ]; then
		cd $OLDPWD
	fi

	exit 1
fi

if [ ! -f u-boot-${MACHINE}.img ]; then
	echo -e "File not found: u-boot-${MACHINE}.img\n"
 
	if [ ! -z "$OETMP" ]; then
		cd $OLDPWD
	fi

	exit 1
fi

if [ ! -f uImage-${MACHINE}.bin ]; then
	echo -e "File not found: uImage-${MACHINE}.bin\n"
 
	if [ ! -z "$OETMP" ]; then
		cd $OLDPWD
	fi

	exit 1
fi

DEV=/dev/${1}1

if [ -b $DEV ]; then
	echo "Unmounting $DEV"
	sudo umount ${DEV}

	echo "Formatting FAT partition on $DEV"
	sudo mkfs.vfat -F 32 ${DEV} -n BOOT

	echo "Mounting $DEV"
	sudo mount ${DEV} /media/card

	echo "Copying MLO"
	sudo cp MLO-${MACHINE} /media/card/MLO
	echo "Copying u-boot"
	sudo cp u-boot-${MACHINE}.img /media/card/u-boot.img

	if [ -f boot.scr ]; then
		echo "Copying boot.scr"
		sudo cp boot.scr /media/card/boot.scr

		if [ -f boot.cmd ]; then
			echo "Copying boot.cmd"
			sudo cp boot.cmd /media/card/boot.cmd
		fi
	fi

	echo "Copying uImage"
	sudo cp uImage-${MACHINE}.bin /media/card/uImage

	sync
	echo "Unmounting ${DEV}"
	sudo umount ${DEV}
else
	echo -e "\nBlock device not found: $DEV\n"
fi

if [ ! -z "$OETMP" ]; then
	cd $OLDPWD
fi

# now copy over the rootfs

if [ "x${2}" = "x" ]; then
	#default image is the console image
        IMAGE=console
else
        IMAGE=${2}
fi

echo "IMAGE: $IMAGE"


echo -e "HOSTNAME: $TARGET_HOSTNAME\n"


if [ ! -z "$OETMP" ]; then
        cd ${OETMP}/deploy/images
fi

if [ ! -f "ARL-${IMAGE}-image-${MACHINE}.tar.bz2" ]; then
        echo -e "File not found: $PWD/ARL-${IMAGE}-image-${MACHINE}.tar.bz2\n"

                if [ ! -z "$OETMP" ]; then
                        cd $OLDPWD
                fi

        exit 1
fi

DEV=/dev/${1}2

# what interfaces file are we using
if [ "x${4}" = "x" ]; then
	# default one with dhcp wifi
	INTERFACES=files/interfaces
else
	INTERFACES=${4}
fi

if [ -b $DEV ]; then
	echo "Unmounting $DEV"
	sudo umount ${DEV}

        echo "Formatting $DEV as ext3"
        sudo mkfs.ext3 -L ROOT $DEV

        echo "Mounting $DEV"
        sudo mount $DEV /media/card

        echo "Extracting ARL-${IMAGE}-image-${MACHINE}.tar.bz2 to /media/card"
        sudo tar -xaf ARL-${IMAGE}-image-${MACHINE}.tar.bz2 -C /media/card

        echo "Writing hostname to /etc/hostname"
        export TARGET_HOSTNAME
        sudo -E bash -c 'echo ${TARGET_HOSTNAME} > /media/card/etc/hostname'

	if [ ! -z "$OETMP" ]; then
		cd $OLDPWD
	fi

        if [ -f ${SCRTMP}/files/$INTERFACES ]; then
                echo "Writing $INTERFACES to /media/card/etc/network/interfaces"
                sudo cp ${SCRTMP}/files/$INTERFACES /media/card/etc/network/interfaces
        fi

        if [ -f ${SCRTMP}/files/wpa_supplicant.conf ]; then
                echo "Writing wpa_supplicant.conf to /media/card/etc/"
                sudo cp ${SCRTMP}/files/wpa_supplicant.conf /media/card/etc/wpa_supplicant.conf
        fi

	if [ -d ${SCRTMP}/files/quarc_overo ]; then
		echo "Writing quarc files to /media/card/home/root/"
		sudo cp -R ${SCRTMP}/files/quarc_overo /media/card/home/root/
	fi

	if [ "${IMAGE}" = "gst" ]; then
		echo "Copying gst capture files for streaming to /media/card/home/root/"
		sudo cp ${SCRTMP}/files/build /media/card/home/root/
		sudo cp ${SCRTMP}/files/capture.c /media/card/home/root/
	fi
	
	sync
        echo "Unmounting $DEV"
        sudo umount $DEV
else
        echo "Block device $DEV does not exist"
	if [ ! -z "$OETMP" ]; then
        	cd $OLDPWD
	fi
fi

echo "Done making ARL SD card for $MACHINE."

