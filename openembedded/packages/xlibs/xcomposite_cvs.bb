PV = "0.0+cvs${SRCDATE}"
LICENSE= "BSD-X"
SECTION = "x11/libs"
DEPENDS = "x11 compositeext xextensions libxfixes"
DESCRIPTION = "X Composite extension library."

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xcomposite"
S = "${WORKDIR}/Xcomposite"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
