#  COMMAND TO Build Maven Plugins
mvn verify -Didempiere.target=org.amerpsoft.com.idempiere.p2.targetplatform -X

# NOTE: Compilation is configured for local MAC OS development environment
# Compiling in a different server setup you need to change relativePath and Location.
#
# 1. Change Relative Path in files:
#   org.amerpsoft.idempiere.p2.site/pom.xml
#   org.amerpsoft.idempiere.p2.targetplatform/pom.xml
#   org.amerpsoft.idempiere.editors-com/pom.xml
#   org.amerpsoft.idempiere.themes/pom.xml
#  Relative Path is set as: 
#  <relativePath>../../myexperiment/org.idempiere.parent/pom.xml</relativePath>
#
# 2. Change the repository location in file:
#   org.amerpsoft.com.idempiere.p2.targetplatform/org.amerpsoft.com.idempiere.p2.targetplatform.target
#   Repository Location is set as: 
# 		<repository location="file:///Volumes/Datos/Adempiere/iDempiere6.2srcmac/myexperiment/org.idempiere.p2/target/repository"/>
