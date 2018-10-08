#!/bin/sh
# ----------------------------------------------------------------------------
#  Copyright 2001-2006 The Apache Software Foundation.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
# ----------------------------------------------------------------------------
#
#   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
#   reserved.


# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`
BASEDIR=`cd "$PRGDIR/.." >/dev/null; pwd`



# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true
           if [ -z "$JAVA_VERSION" ] ; then
             JAVA_VERSION="CurrentJDK"
           else
             echo "Using Java version: $JAVA_VERSION"
           fi
           if [ -z "$JAVA_HOME" ] ; then
             JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
           fi
           ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# If a specific java binary isn't specified search for the standard 'java' binary
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=`which java`
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." 1>&2
  echo "  We cannot execute $JAVACMD" 1>&2
  exit 1
fi

if [ -z "$REPO" ]
then
  REPO="$BASEDIR"/lib
fi

CLASSPATH=$CLASSPATH_PREFIX:"$BASEDIR"/conf:"$REPO"/junit-4.12.jar:"$REPO"/hamcrest-core-1.3.jar:"$REPO"/spring-boot-starter-web-1.5.8.RELEASE.jar:"$REPO"/spring-boot-starter-1.5.8.RELEASE.jar:"$REPO"/spring-boot-autoconfigure-1.5.8.RELEASE.jar:"$REPO"/spring-boot-starter-logging-1.5.8.RELEASE.jar:"$REPO"/logback-classic-1.1.11.jar:"$REPO"/logback-core-1.1.11.jar:"$REPO"/jcl-over-slf4j-1.7.25.jar:"$REPO"/jul-to-slf4j-1.7.25.jar:"$REPO"/log4j-over-slf4j-1.7.25.jar:"$REPO"/snakeyaml-1.17.jar:"$REPO"/spring-boot-starter-tomcat-1.5.8.RELEASE.jar:"$REPO"/tomcat-embed-core-8.5.23.jar:"$REPO"/tomcat-annotations-api-8.5.23.jar:"$REPO"/tomcat-embed-el-8.5.23.jar:"$REPO"/tomcat-embed-websocket-8.5.23.jar:"$REPO"/hibernate-validator-5.3.5.Final.jar:"$REPO"/validation-api-1.1.0.Final.jar:"$REPO"/jboss-logging-3.3.1.Final.jar:"$REPO"/classmate-1.3.4.jar:"$REPO"/jackson-databind-2.8.10.jar:"$REPO"/jackson-annotations-2.8.0.jar:"$REPO"/jackson-core-2.8.10.jar:"$REPO"/spring-web-4.3.12.RELEASE.jar:"$REPO"/spring-aop-4.3.12.RELEASE.jar:"$REPO"/spring-beans-4.3.12.RELEASE.jar:"$REPO"/spring-context-4.3.12.RELEASE.jar:"$REPO"/spring-webmvc-4.3.12.RELEASE.jar:"$REPO"/spring-expression-4.3.12.RELEASE.jar:"$REPO"/spring-boot-starter-thymeleaf-1.5.8.RELEASE.jar:"$REPO"/thymeleaf-spring4-2.1.5.RELEASE.jar:"$REPO"/thymeleaf-2.1.5.RELEASE.jar:"$REPO"/ognl-3.0.8.jar:"$REPO"/javassist-3.21.0-GA.jar:"$REPO"/unbescape-1.1.0.RELEASE.jar:"$REPO"/slf4j-api-1.7.25.jar:"$REPO"/thymeleaf-layout-dialect-1.4.0.jar:"$REPO"/groovy-2.4.12.jar:"$REPO"/mysql-connector-java-5.1.18.jar:"$REPO"/spring-boot-starter-jdbc-1.5.8.RELEASE.jar:"$REPO"/tomcat-jdbc-8.5.23.jar:"$REPO"/tomcat-juli-8.5.23.jar:"$REPO"/spring-jdbc-4.3.12.RELEASE.jar:"$REPO"/spring-tx-4.3.12.RELEASE.jar:"$REPO"/mybatis-spring-boot-starter-1.3.0.jar:"$REPO"/mybatis-spring-boot-autoconfigure-1.3.0.jar:"$REPO"/mybatis-3.4.4.jar:"$REPO"/mybatis-spring-1.3.1.jar:"$REPO"/fastjson-1.1.23.jar:"$REPO"/shiro-spring-1.2.1.jar:"$REPO"/shiro-core-1.2.1.jar:"$REPO"/commons-beanutils-1.9.3.jar:"$REPO"/commons-collections-3.2.2.jar:"$REPO"/shiro-web-1.2.1.jar:"$REPO"/thymeleaf-extras-shiro-1.0.2.jar:"$REPO"/spring-boot-test-1.5.8.RELEASE.jar:"$REPO"/spring-boot-1.5.8.RELEASE.jar:"$REPO"/spring-test-4.3.12.RELEASE.jar:"$REPO"/spring-core-4.3.12.RELEASE.jar:"$REPO"/etl-1.0-SNAPSHOT.jar
EXTRA_JVM_ARGUMENTS="-Xms128m"

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$HOME" ] && HOME=`cygpath --path --windows "$HOME"`
  [ -n "$BASEDIR" ] && BASEDIR=`cygpath --path --windows "$BASEDIR"`
  [ -n "$REPO" ] && REPO=`cygpath --path --windows "$REPO"`
fi

exec "$JAVACMD" $JAVA_OPTS \
  $EXTRA_JVM_ARGUMENTS \
  -classpath "$CLASSPATH" \
  -Dapp.name="start" \
  -Dapp.pid="$$" \
  -Dapp.repo="$REPO" \
  -Dapp.home="$BASEDIR" \
  -Dbasedir="$BASEDIR" \
  com.mongohua.etl.EtlApplication \
  "$@"
