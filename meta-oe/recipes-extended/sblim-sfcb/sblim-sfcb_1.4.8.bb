SUMMARY = "Small Footprint CIM Broker"
DESCRIPTION = "\
Small Footprint CIM Broker (sfcb) is a CIM server conforming to the CIM \
Operations over HTTP protocol. It is robust, with low resource consumption \
and therefore specifically suited for embedded and resource constrained \
environments. sfcb supports providers written against the Common \
Manageability Programming Interface (CMPI)."
HOMEPAGE = "http://www.sblim.org"
SECTION = "Applications/System"
LICENSE = "EPL-1.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=f300afd598546add034364cd0a533261"
DEPENDS = "curl libpam openssl sblim-sfc-common"

SRC_URI = "http://downloads.sourceforge.net/sblim/${BP}.tar.bz2 \
           file://sblim-sfcb-1.3.9-sfcbrepos-schema-location.patch \
           file://sblim-sfcb-1.3.15-fix-provider-debugging.patch \
           file://sblim-sfcb-1.3.16-maxMsgLen.patch \
           file://sblim-sfcb-1.4.5-service.patch \
           file://sblim-sfcb-1.3.16-multilib-man-cfg.patch \
           file://sblim-sfcb-1.4.8-default-ecdh-curve-name.patch \
           file://sblim-sfcb-1.4.9-fix-ftbfs.patch \
           file://sfcb.service"
           
SRC_URI[md5sum] = "b2e0cb26628cb31e2374959637d9268c"
SRC_URI[sha256sum] = "7dc0f16e2c2d3767466a09afee04e6febd611c157d66f8f0ae666be1fefbff3b"

inherit autotools
inherit ${@base_contains('VIRTUAL-RUNTIME_init_manager','systemd','systemd','', d)}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "sblim-sfcb.service"
SYSTEMD_AUTO_ENABLE = "enable"

LDFLAGS_append = "${@base_contains('DISTRO_FEATURES', 'ld-is-gold', ' -fuse-ld=bfd ', '', d)}"

EXTRA_OECONF = '--enable-debug \
                --enable-ssl \
                --enable-pam \
                --enable-ipv6 \
                CFLAGS="${CFLAGS} -D_GNU_SOURCE"'

# make all with -j option is unsafe.
PARALLEL_MAKE = ""

INSANE_SKIP_${PN} = "dev-so"
CONFIG_SITE = "${WORKDIR}/config-site.${P}"

do_install() {
    cp -f ${S}/sfcb.cfg.pre.in ${S}/sfcb.cfg

    oe_runmake DESTDIR=${D} install

    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/sfcb.service ${D}${systemd_unitdir}/system/sblim-sfcb.service
    fi

    install -d ${D}${sysconfdir}/init.d
    mv ${D}${sysconfdir}/init.d/sfcb ${D}${sysconfdir}/init.d/sblim-sfcb
    sed -i -e 's/\/var\/lock\/subsys\/sfcb/\/var\/lock\/subsys\/sblim-sfcb/g' ${D}${sysconfdir}/init.d/sblim-sfcb
    
    rm -rf ${D}${libdir}/sfcb/*.la
}

pkg_postinst_${PN} () {
    OPTS=""

    if [ x"$D" != "x" ]; then
        OPTS="--root=$D"
        if type systemctl >/dev/null 2>/dev/null; then
                systemctl $OPTS ${SYSTEMD_AUTO_ENABLE} ${SYSTEMD_SERVICE}
        fi
        exit 1
    fi

    ${datadir}/sfcb/genSslCert.sh ${sysconfdir}/sfcb
    ${bindir}/sfcbrepos -f
}

FILES_${PN} += "${libdir}/sfcb ${datadir}/sfcb"
FILES_${PN}-dbg += "${libdir}/sfcb/.debug"

RDEPENDS_${PN} = "perl bash"
