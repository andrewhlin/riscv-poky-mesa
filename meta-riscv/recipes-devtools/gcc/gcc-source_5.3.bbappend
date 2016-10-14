FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://gcc-5.3-riscv.patch file://gcc-5.3-riscv-musl.patch file://gcc-5.3-riscv-atomic.patch file://gcc-5.3-riscv-atomics.patch"
#SRC_URI += "file://gcc-4.9-config.patch"
