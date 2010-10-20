#!/bin/bash
rm -rf $CATALINA_HOME/webapps/files
cp target/files-0.1-SNAPSHOT.war $CATALINA_HOME/webapps/files.war
