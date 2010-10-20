#!/bin/bash
rm -rf $CATALINA_HOME/webapps/files
cp target/files-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/files.war
