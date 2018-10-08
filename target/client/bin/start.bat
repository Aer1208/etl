@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\lib

set CLASSPATH="%BASEDIR%"\conf;"%REPO%"\junit-4.11.jar;"%REPO%"\hamcrest-core-1.3.jar;"%REPO%"\spring-boot-starter-web-1.5.8.RELEASE.jar;"%REPO%"\spring-boot-starter-1.5.8.RELEASE.jar;"%REPO%"\spring-boot-1.5.8.RELEASE.jar;"%REPO%"\spring-boot-autoconfigure-1.5.8.RELEASE.jar;"%REPO%"\spring-boot-starter-logging-1.5.8.RELEASE.jar;"%REPO%"\logback-classic-1.1.11.jar;"%REPO%"\logback-core-1.1.11.jar;"%REPO%"\jcl-over-slf4j-1.7.25.jar;"%REPO%"\jul-to-slf4j-1.7.25.jar;"%REPO%"\log4j-over-slf4j-1.7.25.jar;"%REPO%"\spring-core-4.3.12.RELEASE.jar;"%REPO%"\snakeyaml-1.17.jar;"%REPO%"\spring-boot-starter-tomcat-1.5.8.RELEASE.jar;"%REPO%"\tomcat-embed-core-8.5.23.jar;"%REPO%"\tomcat-annotations-api-8.5.23.jar;"%REPO%"\tomcat-embed-el-8.5.23.jar;"%REPO%"\tomcat-embed-websocket-8.5.23.jar;"%REPO%"\hibernate-validator-5.3.5.Final.jar;"%REPO%"\validation-api-1.1.0.Final.jar;"%REPO%"\jboss-logging-3.3.1.Final.jar;"%REPO%"\classmate-1.3.4.jar;"%REPO%"\jackson-databind-2.8.10.jar;"%REPO%"\jackson-annotations-2.8.0.jar;"%REPO%"\jackson-core-2.8.10.jar;"%REPO%"\spring-web-4.3.12.RELEASE.jar;"%REPO%"\spring-aop-4.3.12.RELEASE.jar;"%REPO%"\spring-beans-4.3.12.RELEASE.jar;"%REPO%"\spring-context-4.3.12.RELEASE.jar;"%REPO%"\spring-webmvc-4.3.12.RELEASE.jar;"%REPO%"\spring-expression-4.3.12.RELEASE.jar;"%REPO%"\spring-boot-starter-thymeleaf-1.5.8.RELEASE.jar;"%REPO%"\thymeleaf-spring4-2.1.5.RELEASE.jar;"%REPO%"\thymeleaf-2.1.5.RELEASE.jar;"%REPO%"\ognl-3.0.8.jar;"%REPO%"\javassist-3.21.0-GA.jar;"%REPO%"\unbescape-1.1.0.RELEASE.jar;"%REPO%"\slf4j-api-1.7.25.jar;"%REPO%"\thymeleaf-layout-dialect-1.4.0.jar;"%REPO%"\groovy-2.4.12.jar;"%REPO%"\mysql-connector-java-5.1.18.jar;"%REPO%"\spring-boot-starter-jdbc-1.5.8.RELEASE.jar;"%REPO%"\tomcat-jdbc-8.5.23.jar;"%REPO%"\tomcat-juli-8.5.23.jar;"%REPO%"\spring-jdbc-4.3.12.RELEASE.jar;"%REPO%"\spring-tx-4.3.12.RELEASE.jar;"%REPO%"\mybatis-spring-boot-starter-1.3.0.jar;"%REPO%"\mybatis-spring-boot-autoconfigure-1.3.0.jar;"%REPO%"\mybatis-3.4.4.jar;"%REPO%"\mybatis-spring-1.3.1.jar;"%REPO%"\fastjson-1.1.23.jar;"%REPO%"\etl-1.0-SNAPSHOT.jar
set EXTRA_JVM_ARGUMENTS=-Xms128m
goto endInit

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath %CLASSPATH_PREFIX%;%CLASSPATH% -Dapp.name="start" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" com.ftoul.bi.etl.EtlApplication %CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
