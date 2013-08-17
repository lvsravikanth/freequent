# This will force bash to ignore carriage return (\r) characters used in Windows line separators.
(set -o igncr) 2>/dev/null && set -o igncr; # this comment is needed

#! /bin/sh


#set -x

# THIS IS A SHARED BUILD SCRIPT.
# MAKE SURE YOU KNOW THE FULL IMPACT BEFORE SUBMITTING

##############################################################################

usageError() {
	echo ""
	echo "Usage: `basename $0` [options] [ant target]"
	echo ""
	echo "Optional arguments:"
	echo "  -debug            Run the build in debug mode."
	echo "  -v                Run the build in verbose mode."
	echo "  -antarg <arg>     Add an argument to the ant command line arguments."
	echo "  -antprofile       Enable Ant profiling using org.apache.tools.ant.listener.ProfileLogger."
	echo ""
	echo "Optional environment settings:"
	echo "  JAVA_HOME_VUI  Optional external JDK directory."
	echo "                 Normally uses the one in the tools client,"
	echo "                 but this is the override.  This is most likely"
	echo "                 required for aix, hpux, or osx builds, where the"
	echo "                 tools client does not contain the appropriate JDK."
	echo "                 For osx, if JAVA_HOME_VUI isn't set, it will"
	echo "                 default to the JAVA_HOME value."
	exit 1
}

##############################################################################

process_args() {
	while  [ $# -ne 0 ] ; do
		case $1 in
			"-v")
				ANTARGS="${ANTARGS} -verbose"
				shift
			;;

			"-debug")
				ANTARGS="${ANTARGS} -debug"
				shift
			;;

			"-antarg")
				ANTARGS="${ANTARGS} $2"
				shift 2
			;;

			"-antprofile")
				ANTARGS="${ANTARGS} -logger org.apache.tools.ant.listener.ProfileLogger"
				shift 1
			;;

			*)
				TARGET=$1
				shift
				ADDITIONAL_ARGS=$*
				shift $#
				;;
		esac
	done
}

##############################################################################
# Set the default values for the variables
TARGET=all
ADDITIONAL_ARGS=""

# Get the command line arguments
process_args $*

# Get environment variables if they are set
OS=${OS:-`uname`}
if [ `echo ${OS} | grep "CYGWIN"` ] ; then
	OS="Windows_NT"
fi
BASE="`pwd`"
# convert the cygwin's idea of a path to a proper windows path
# necessary for the Java compiler to understand the classpath
if [ `uname | grep "CYGWIN"` ] ; then
	BASE_CYGWIN=`cygpath -md "${BASE}"`
	echo "Modifying BASE (${BASE}) to ${BASE_CYGWIN} for Cygwin's sake"
	BASE=${BASE_CYGWIN}
fi

# Prepare derived build variables
BUILDFILE=$BASE/build.xml
BUILDLOG=$BASE/build.log

if [ -f "$BASE/build.properties" ]; then
	FILESET=`cat $BASE/build.properties | tr -d '\15\32' | grep "^[ ]*fileset.dir" | cut -d= -f2`
	if [ -z "$FILESET" ] ; then
		FILESET=/dev/main/fileset
	fi
fi
TP_DIR=${FILESET:-$BASE/../fileset}/thirdparty

BUILDTOOLS=${TOOLS:-"."}

# Prepare OS specific variables
PATHSEP=":"
OS_PLATFORM=
case $OS in
	"Windows_NT")
		PATHSEP=";"
		OS_TYPE="winnt"
	;;

	"SunOS")
		PLATFORM=${PLATFORM:-`uname -i`}
		if [ $PLATFORM = "i86pc" ]
		then
		    OS_TYPE="solaris_x86"
		else
		    OS_TYPE="solaris"
		fi
	;;

	"Linux")
		OS_TYPE="linux"
	;;

	"AIX")
		OS_TYPE="aix"
	;;

	"HP-UX")
		OS_TYPE="hpux"
	;;

	"Darwin")
		OS_TYPE="osx"
	;;

	*)
		echo This Unix flavor not supported by build.sh
		exit 1
	;;
esac

OS_PLATFORM=${OS_PLATFORM:-${OS_TYPE}}

OS_JAVA_HOME=${TP_DIR}/bea/jdk.dist/${OS_PLATFORM}_32
if [ ! -d "${OS_JAVA_HOME}" ] ; then
	OS_JAVA_HOME=${TP_DIR}/bea/jdk.dist/${OS_PLATFORM}_64
fi

# Set the JDK.  Normally uses the one in the fileset.dir is used, but can be
# overridden by setting JAVA_HOME_VUI.  This may be required for aix,
# hpux, or osx/mac builds, where the fileset (cs) does not contain
# the appropriate JDK.  For osx, if JAVA_HOME_VUI isn't set, it will
# default to the JAVA_HOME value.
if [ "${JAVA_HOME_VUI}" = "" ]; then
	if [ "$OS_TYPE" != "osx" ]; then
		JAVA_HOME=${OS_JAVA_HOME}
	fi
else
	JAVA_HOME=${JAVA_HOME_VUI}
fi

# ensure JAVA_HOME is set to a reachable location
if [ "${JAVA_HOME}" = "" ] ; then
	echo "JAVA_HOME is not set!"
	exit 1
elif [ ! -d "${JAVA_HOME}" ] ; then
	echo "${JAVA_HOME} does not exist!"
	exit 1
fi

# Set and export PATH
PATH=${JAVA_HOME}/jre/bin${PATHSEP}${JAVA_HOME}/bin${PATHSEP}${JAVA_HOME}/lib${PATHSEP}${BUILDROOT}/bldenv${PATHSEP}${PATH}
export PATH

# Set and export CLASSPATH
CLASSPATH=${JAVA_HOME}/lib/tools.jar

CLASSPATH=${CLASSPATH}${PATHSEP}${TP_DIR}/antcontrib/lib/ant-contrib.jar
for i in ${TP_DIR}/apache-ant/lib/*.jar
do
	CLASSPATH=${CLASSPATH}${PATHSEP}${i}
done

CLASSPATH=${CLASSPATH}${PATHSEP}${TP_DIR}/cobertura/cobertura.jar

for i in ${TP_DIR}/cobertura/lib/*.jar
do
	CLASSPATH=${CLASSPATH}${PATHSEP}${i}
done

export CLASSPATH

ANTARGS="${ANTARGS} -buildfile ${BUILDFILE}"

echo "$JAVA_HOME/bin/java -Xmx256m -classpath $CLASSPATH -Dbuild.java.home=${JAVA_HOME} -Dos.type=$OS_TYPE $ADDITIONAL_ARGS org.apache.tools.ant.Main ${ANTARGS} $TARGET"

# Perform the build
$JAVA_HOME/bin/java -Xmx256m -classpath "$CLASSPATH" -Dbuild.java.home="${JAVA_HOME}" -Dos.type=$OS_TYPE $ADDITIONAL_ARGS org.apache.tools.ant.Main ${ANTARGS} $TARGET

RETVAL=$?

if [ $RETVAL -ne 0 ] ; then
        echo ------------------------------------------------------------------------
        echo BUILD FAILED - $RETVAL
        echo ------------------------------------------------------------------------
fi

exit $RETVAL
