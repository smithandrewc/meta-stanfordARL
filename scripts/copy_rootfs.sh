#!/bin/bash

if [ "x${1}" = "x" ]; then
	echo -e "\nUsage: ${0} <block device> [ <image-type> [<hostname>] ]\n"
	exit 0
fi

if [ "x${2}" = "x" ]; then
        IMAGE=qte
else
        IMAGE=${2}
fi

if [ -z "$OETMP" ]; then
	echo -e "\nWorking from local directory"
else
	echo -e "\nOETMP: $OETMP"

	if [ ! -d ${OETMP}/deploy/images ]; then
		echo "Directory not found: ${OETMP}/deploy/images"
		exit 1
	fi
fi 

if [[ -z "${MACHINE}" ]]; then
	echo "Environment variable MACHINE not found!"
	echo "Choices are overo|duovero|beaglebone"
	exit 1
else
	echo "MACHINE: $MACHINE"
fi


echo "IMAGE: $IMAGE"

if [ "x${3}" = "x" ]; then
        TARGET_HOSTNAME=$MACHINE
else
        TARGET_HOSTNAME=${3}
fi

echo -e "HOSTNAME: $TARGET_HOSTNAME\n"


if [ ! -z "$OETMP" ]; then
	cd ${OETMP}/deploy/images
fi

# to work around an issue with the VPU codec
if [ "${MACHINE}" = "wandboard-dual" ]; then
	echo -e "\nUsing wandboard-quad root fs for wandboard-dual\n"
	MACHINE="wandboard-quad"
fi

if [ ! -f "pansenti-${IMAGE}-image-${MACHINE}.tar.xz" ]; then
        echo -e "File not found: pansenti-${IMAGE}-image-${MACHINE}.tar.xz\n"

		if [ ! -z "$OETMP" ]; then
			cd $OLDPWD
		fi

        exit 1
fi

DEV=/dev/${1}2

if [ -b $DEV ]; then
	echo "Formatting $DEV as ext3"
	sudo mkfs.ext3 -L ROOT $DEV

	echo "Mounting $DEV"
	sudo mount $DEV /media/card

	echo "Extracting pansenti-${IMAGE}-image-${MACHINE}.tar.xz to /media/card"
	sudo tar -C /media/card -xJf pansenti-${IMAGE}-image-${MACHINE}.tar.xz

	echo "Writing hostname to /etc/hostname"
	export TARGET_HOSTNAME
	sudo -E bash -c 'echo ${TARGET_HOSTNAME} > /media/card/etc/hostname'        

	if [ -f interfaces ]; then
		echo "Writing interfaces to /media/card/etc/network/"
		sudo cp interfaces /media/card/etc/network/interfaces
	fi

	if [ -f wpa_supplicant.conf ]; then
		echo "Writing wpa_supplicant.conf to /media/card/etc/"
		sudo cp wpa_supplicant.conf /media/card/etc/wpa_supplicant.conf
	fi

	echo "Unmounting $DEV"
	sudo umount $DEV
else
	echo "Block device $DEV does not exist"
fi

if [ ! -z "$OETMP" ]; then
	cd $OLDPWD
fi

echo "Done"

