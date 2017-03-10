SUMMARY = "A small image just capable of allowing a device to boot."

DEPENDS += "riscv-pk"

IMAGE_FEATURES += "package-management"

inherit core-image

IMAGE_INSTALL = "packagegroup-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"

# Basic packages
IMAGE_INSTALL += "apt libffi libffi-dev zsh"

# Python
IMAGE_INSTALL += "python-numpy python-subprocess python-ctypes python-html python-netserver python-compile"

# Basic toolchain on target
IMAGE_INSTALL += "gcc binutils glibc glibc-dev libgcc libgcc-dev libstdc++ libstdc++-dev make"

# Add Mesa
IMAGE_INSTALL += "freeglut mesa mesa-demos"

#IMAGE_INSTALL += "packagegroup-core-x11 xvideo-tests xserver-xorg-xvfb"
IMAGE_LINGUAS = " "

# Add ssh
#IMAGE_INSTALL += "openssh"
#IMAGE_INSTALL += "doomlegacy"

LICENSE = "MIT"

IMAGE_ROOTFS_SIZE ?= "8192"

#IMAGE_ROOTFS_EXTRA_SPACE = "4194304"
IMAGE_ROOTFS_EXTRA_SPACE = "16384"

