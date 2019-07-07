  <parent>
  	<groupId>org.idempiere</groupId>
  	<artifactId>org.idempiere.parent</artifactId>
  	<version>6.2.0-SNAPSHOT</version>
  	<relativePath>../../myexperiment/org.idempiere.parent/pom.xml</relativePath>
  </parent>
  <packaging>eclipse-plugin</packaging>


== Steps to build and setup development environment ==
# Materialize and build (this step is needed for both headless build and setup of Eclipse)
## Clone source from [http://bitbucket.org/idempiere/idempiere bitbucket.org] to a folder [idempiere-home]
## Open terminal, cd to [idempiere-home] and execute command "mvn verify" (in case you focus to setup eclipse, don't want to build product, use "mvn validate")
##* Downloads all libraries on Bundle-ClassPath
##* Build all projects and output binary to [idempiere-home]/org.idempiere.p2/target/products
# Setup Eclipse Workspace
## Import Projects
### Open Eclipse with Workspace at [idempiere-home]
### From Eclipse Menu, choose File/Import
### At Import dialog, choose Maven/Existing Maven Projects
### Click Next button and Browse to [idempiere-home]
### Select all projects, click Finish to complete the import
### All projects are loaded to workspace
## Set Target Platform
### From Eclipse Menu, choose File/Import
### At Import dialog, choose General/Existing Projects into Workspace
### Click Next button and Browse to [idempiere-home]/org.idempiere.p2.targetplatform. Click Finish to complete the import
### Open the org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target file. This is the default target platform definition with remote url
### At Target Editor, click the "Set as Active Target Platform" link
### Wait for Eclipse to download bundles onto target platform. Click the "Reload Target Platform" link if some download fail
## Say good bye to Buckminster
# Know issue
## in case you do step "Import Projects" before "mvn validate" eclipse will auto change some classpath, so you you will build fail ever you run "mvn validate". Need to revert change of eclipse before you run "mvn validate"

[[User:Hengsin|Hengsin]] ([[User talk:Hengsin|talk]]) 05:54, 20 October 2018 (UTC), org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target is slow and error prone for me. I'm not sure that's the intended alternative but idempiere/org.idempiere.p2/target/repository works fine for me as Target Platform (add new empty target platform and add the idempiere/org.idempiere.p2/target/repository folder/directory to it) and without all the hassle with org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target

== Config For Jenkins ==
# Setup Maven for Jenkins
# Create a Maven Project
# In "Source Code Management" Section
#* Repository URL=ssh://hg@bitbucket.org/idempiere/idempiere-experimental (or the url of your target repository)
#* Revision=experimental (or the revision of your target repository)
# On build step, setup with value:
#* Root POM=pom.xml
#* Goals and options=verify

== How to set an OSGi Plugin as a Maven Project ==
# Create a normal plugin/feature project on eclipse
# Right click on project and choose "''Configure/Convert to Maven Project"''
# At dialog, value of "Package" type field depends on type of plug-in
#* Eclipse feature => "eclipse-feature"
#* Eclipse plugin/fragment => "eclipse-plugin"
# After eclipse has converted project, there will be a pom.xml inside project space, please adjust value as below:
#* Add parent section
<pre>
    <parent>
        <groupId>org.idempiere</groupId>
	<artifactId>org.idempiere.parent</artifactId>
	<version>6.2.0-SNAPSHOT</version>
	<relativePath>../org.idempiere.parent/pom.xml</relativePath>
    </parent>
</pre>
# Open MANIFEST.MF and continue checking..
#* At Runtime tab, section classpath has item "." if not, you should create one (it's importance in case your plugin have java file)
# In build.properties 
#* Adjust Output to "target/classes/"
#* In case your project has source, make sure it is stated: "source.. = src/"
#* In case your project has source, make sure on "bin.includes = " have item "." otherwise your export miss .class
# Reference:
#* http://codeandme.blogspot.co.at/2012/12/tycho-build-1-building-plug-ins.html
#* Core project

== Build Extra Plugin ==

=== Build by Run Script from local ===
# Build idempiere core so we have a p2 repository that contains idempiere core plus dependency at idempiere/org.idempiere.p2/target/repository
# Host this repository with a web server. If you don't have one, you can use [https://www.eclipse.org/jetty/documentation/current/jetty-maven-plugin.html#jetty-start-goal jetty-maven-plugin] to host it
# Create a maven tycho target plugin point to the p2 repository server setup at step 2
# Build extra plugin by using target at step 3
Full example and step by step guideline [https://bitbucket.org/idplugin/idempiere.maven.tycho.build.extra.bundle/overview here]

=== Build by Run Jenkins Job ===
Because you have to check out from multi repository, I prefer to use jenkins pipeline

Besides, normal pipeline plugin, you have to install "Pipeline Maven Integration Plugin"

For newbie to jenkins pipeline, I suggest to use this tool to generate script http://&#x5B;jenkins_server&#x5D;/job/&#x5B;job_name&#x5D;/pipeline-syntax/
[[File:Pipeline syntax.png|none|thumb]]

==== Steps to build ====
# Setup a web server (like apache httpd at port 80 on same server jenkin, for access by localhost), assume document dir at /var/www/html
# Setup a jenkins job to build idempiere core.
# Successful build will copy artifact to /var/www/html/idempiere-core/latest
# This is pipeline for build idempiere-core
{| class="wikitable mw-collapsible mw-collapsed"
|- 
! Pipe script example
|-
|<pre>
node {
   def mvnHome
   stage('Preparation') { // for display purposes
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', revision: 'hsv', source: 'ssh://hg@bitbucket.org/hasuvimex/idempiere', subdir: 'idempiere'])
   }
   stage('Build') {
            
        withMaven(jdk: 'openjdk', maven: 'build-in maven', publisherStrategy: 'EXPLICIT') {
            sh "cd ${WORKSPACE}/idempiere && mvn verify"
        }
        
        sh '''p2OutputDir="${WORKSPACE}/idempiere/org.idempiere.p2/target/repository"
            webDir="/var/www/html/repository/idempiere-hsv-core/latest"
            mkdir -p ${webDir}
            cp -r ${p2OutputDir}/* ${webDir}'''
   }
   stage('Results') {
 
   }
}
</pre>
|}
# Setup other jenkins job to build extra plugin
# This's pipeline script
{| class="wikitable mw-collapsible mw-collapsed"
|- 
! Pipe script example
|-
|<pre>
node {
   def mvnHome
   stage('Preparation') { // for display purposes
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', revision: 'hsv', source: 'ssh://hg@bitbucket.org/hasuvimex/idempiere', subdir: 'idempiere'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', revision: 'hsv', source: 'ssh://hg@bitbucket.org/idplugin/idempiere.maven.tycho.build.extra.bundle', subdir: 'idempiere.maven.tycho.build.extra.bundle'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', source: 'ssh://hg@bitbucket.org/hasuvimex/project.extra.bundle', subdir: 'project.extra.bundle'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', revision: 'hsv', source: 'ssh://hg@bitbucket.org/hasuvimex/org.idempiere.customize-feature', subdir: 'project.extra.bundle/org.idempiere.customize-feature'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', source: 'ssh://hg@bitbucket.org/idplugin/th.motive.idempiere.base', subdir: 'project.extra.bundle/th.motive.idempiere.base'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', source: 'ssh://hg@bitbucket.org/idplugin/th.motive.jasper.report.font', subdir: 'project.extra.bundle/th.motive.jasper.report.font'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', source: 'ssh://hg@bitbucket.org/idplugin/th.motive.utility', subdir: 'project.extra.bundle/th.motive.utility'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', source: 'ssh://hg@bitbucket.org/hasuvimex/vn.hsv.editor.currencyrate', subdir: 'project.extra.bundle/vn.hsv.editor.currencyRate'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', source: 'ssh://hg@bitbucket.org/hasuvimex/vn.hsv.idempiere.base', subdir: 'project.extra.bundle/vn.hsv.idempiere.base'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', source: 'ssh://hg@bitbucket.org/hasuvimex/vn.hsv.idempiere.override', subdir: 'project.extra.bundle/vn.hsv.idempiere.override'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', source: 'ssh://hg@bitbucket.org/hasuvimex/vn.hsv.jasperreport.install', subdir: 'project.extra.bundle/vn.hsv.jasperreport.install'])
        checkout([$class: 'MercurialSCM', clean: true, credentialsId: 'mainSshKey', source: 'ssh://hg@bitbucket.org/idplugin/th.motive.jasperreport.configuration', subdir: 'project.extra.bundle/th.motive.jasperreport.configuration'])
        
   }
   stage('Build') {
            
        withMaven(jdk: 'openjdk', maven: 'build-in maven', publisherStrategy: 'EXPLICIT') {
            sh  '''
                    ${WORKSPACE}/idempiere.maven.tycho.build.extra.bundle/update.version.sh "/var/www/html/repository/idempiere-hsv-core/latest/plugins" "org.adempiere.base_*.jar"
                '''
        }
        
        
        sh '''newP2Url="http://localhost/repository/idempiere-hsv-core/latest"
            oldP2Url=http://localhost:8080/idempiere-core
            sed -ri "s|${oldP2Url}|${newP2Url}|g" ${WORKSPACE}/idempiere.maven.tycho.build.extra.bundle/org.idempiere.p2.build.extra.bundle.targetplatform/org.idempiere.p2.build.extra.bundle.targetplatform.target'''
        
        withMaven(jdk: 'openjdk', maven: 'build-in maven', publisherStrategy: 'EXPLICIT') {
            sh '''cd ${WORKSPACE}/idempiere.maven.tycho.build.extra.bundle && mvn verify -Didempiere.target="org.idempiere.p2.build.extra.bundle.targetplatform"'''
        }
   }
   stage('Results') {
 
   }
}
</pre>
|}

note: you still have to checkout idempire on this job, because some plugin refer to parent pom on idempiere

== Add more bundle to p2 maven repository of idempiere ==
to add more bundle we need to modify [idempiere-source]/org.idempiere.maven.to.p2/org.idempiere.maven.to.p2/pom.xml

have some pattern

=== artifacts from maven and already exists Osgi metadata (source also HAS correct Osgi metadata) ===
append to same area of <id>org.passay:passay:jar
<pre>
<artifact>
   <id>[artifactGroup]:[artifactID]:jar:[artifactVersion]</id>
   <source>true</source>
   <transitive>false</transitive>
</artifact>
</pre>
=== artifacts from maven and already exists Osgi metadata (source HASN'T Osgi metadata) ===
<pre>
<artifact>
   <id>[artifactGroup]:[artifactID]:jar:[artifactVersion]</id>
   <source>false</source> <!-- set source to false -->
   <transitive>false</transitive>
</artifact>
<artifact>
   <id>[artifactGroup]:[artifactID]:jar:sources:[artifactVersion]</id>
   <source>false</source>
   <transitive>false</transitive>
   <instructions>
     <Export-Package/>
     <Private-Package/>
     <Private-Package/>
     <Bundle-SymbolicName>[bundleName].source</Bundle-SymbolicName>
     <Bundle-Name>[bundleName].source</Bundle-Name>
     <Eclipse-SourceBundle>[bundleName];version="[artifactVersion]";roots:="."</Eclipse-SourceBundle>
   </instructions>
</artifact>
</pre>
=== artifacts from maven and already exists Osgi metadata (source HAS BUT WRONG Osgi metadata) ===
<pre>
<artifact>
   <id>[artifactGroup]:[artifactID]:jar:[artifactVersion]</id>
   <source>false</source> <!-- set source to false -->
   <transitive>false</transitive>
</artifact>
<artifact>
   <id>[artifactGroup]:[artifactID]:jar:sources:[artifactVersion]</id>
   <source>false</source>
   <override>true</override> <!-- said want to override -->
   <transitive>false</transitive>
   <instructions>
     <Export-Package/>
     <Private-Package/>
     <Private-Package/>
     <Bundle-SymbolicName>[bundleName].source</Bundle-SymbolicName>
     <Bundle-Name>[bundleName].source</Bundle-Name>
     <Eclipse-SourceBundle>[bundleName];version="[artifactVersion]";roots:="."</Eclipse-SourceBundle>
   </instructions>
</artifact>
</pre>
=== artifacts from other p2 repository ===
* define that p2 at <repositories> section
* on <p2> area add
<pre>
<artifact>
   <id>[bundleSymbolicName]:[bundleVersion]</id>
</artifact>
</pre>

=== rebuild ===
* open terminal
* cd [idempiere-source]/org.idempiere.maven.to.p2/org.idempiere.maven.to.p2
* mvn clean verify -X -e -P buildP2FromMaven -Dtycho.disableP2Mirrors=true
* [idempiere-source]/org.idempiere.maven.to.p2/org.idempiere.maven.to.p2
* rename and commit this folder to git [idempiere-source]/org.idempiere.maven.to.p2/org.idempiere.maven.to.p2/target/repository
=== update [idempiere-source]/org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target ===
* on eclipse open [idempiere-source]/org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target
* on definition tab, update value of target name to idempiere-[yymmdd]
* on locations choose link to maven p2 and click edit to open dialog "edit content"
* click "manage ..." button to show dialog "available software site"
* click add button on "add site" dialog set value name = "maven idempiere-[yymmdd]", location = [link to git of new p2 maven] and click add
* click ok on dialog "available software site" to close it
* on dialog "edit content" at "work with" combobox choose item just created
* in case checkbox "show only the latest version" is checked then unchecked it
* choose and unchoose node "maven osgi-bundles" sometime to let content update
* click finish to done
[[File:AddMoreBundleToMavenP2Repository.png|300px|thumb|center|update Maven P2 Repository]]


== Add extra bundle for extra plugin ==
How about when you use a bundle on your extra plugin that bundle don't include on core target platform?

=== prepare p2 repository for extra bundle (in case your extra bundle get from maven or from somes p2 repository and you want to combine it to one p2) ===
* create a pom.xml with content like [idempiere-source]/org.idempiere.maven.to.p2/org.idempiere.maven.to.p2/pom.xml on folder [general.extra.p2]
* I make a example here for reference (comment inline already self explain file content)
{| class="wikitable mw-collapsible mw-collapsed"
|- 
! pom.xml
|-
|<pre><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<parent>
		<groupId>org.idempiere</groupId>
		<artifactId>org.idempiere.parent</artifactId>
		<version>6.2.0-SNAPSHOT</version>
		<!-- relative path to org.idempiere.parent on idempiere source.
		in case you run mvn install on org.idempiere.parent before run this one, you don't need care about this value -->
		<relativePath>../../org.idempiere.parent/pom.xml</relativePath>
	</parent>
	<modelVersion>4.0.0</modelVersion>
	<artifactId>org.idempiere.maven.to.p2.extra</artifactId>
	<packaging>pom</packaging>
<!-- full document here https://github.com/reficio/p2-maven-plugin -->
    <repositories>

    	<!-- add more p2 repository where you can get your extra bundle -->
        <repository>
            <id>orbit-photon-R20180531190352</id>
            <url>http://download.eclipse.org/tools/orbit/downloads/drops2/R20180531190352/repository</url>
            <!-- importance for p2 repository-->
            <layout>p2</layout>
        </repository>
        <!-- add more maven repository where you can get your extra bundle -->
        <repository>
            <id>zkoss</id>
            <url>http://mavensync.zkoss.org/maven2</url>
        </repository>
    </repositories>

	<build>
		<plugins>
			<plugin>
				<groupId>org.reficio</groupId>
				<artifactId>p2-maven-plugin</artifactId>
				<version>1.3.0</version>
				<executions>
					<execution>
						<id>default-cli</id>
						<phase>package</phase>
						<goals>
							<goal>site</goal>
						</goals>
						<configuration>
							<featureDefinitions>
								<feature>
									<!-- feature information to group extra bundle  -->
									<id>org.idempiere.maven.extra.feature</id>
									<version>${project.version}</version>
									<label>Idempiere maven extra osgi dependency ${project.version} feature</label>
									<providerName>Idempiere</providerName>
									<description>feature group all osgi bundle get from maven repository</description>
									<copyright>Idempiere</copyright>
									<license>GPL v2.1</license>
									<generateSourceFeature>true</generateSourceFeature>

									<artifacts>
										<!-- a artifact host on maven repository and Osgi ready -->
										<artifact>
											<id>com.rabbitmq:amqp-client:jar:5.7.0</id>
											<source>true</source>
											<transitive>false</transitive>
										</artifact>
										<!-- a artifact host on maven repository withoud Osgi metadata, need to add  -->
										<artifact>
											<id>org.conscrypt:conscrypt-openjdk-uber:jar:${jetty.conscrypt.openjdk.uber.version}</id>
											<source>true</source>
											<transitive>false</transitive>
											<instructions>
        										<Export-Package>org.conscrypt;version="${jetty.conscrypt.openjdk.uber.version}"</Export-Package>
        										<Bundle-Name>org.idempiere.org.conscrypt.openjdk-uber</Bundle-Name>
        										<Bundle-SymbolicName>org.idempiere.org.conscrypt.openjdk-uber</Bundle-SymbolicName>
    										</instructions>
										</artifact>
										<!--  a artifact ready Osgi metadata, but source bundle not yet has Osgi so need to make source bundle (help easy debug) -->
										<artifact>
											<id>org.apache.servicemix.bundles:org.apache.servicemix.bundles.spring-beans:jar:sources:${springframework.version}</id>
											<source>false</source>
											<transitive>false</transitive>
                                            <override>true</override>
											<instructions>
											    <Export-Package/><!-- important -->
											    <Private-Package/><!-- important -->
											    <Private-Package/><!-- important -->
											    <Bundle-SymbolicName>org.apache.servicemix.bundles.spring-beans.source</Bundle-SymbolicName>
											    <Bundle-Name>spring-expression.source</Bundle-Name>
												<Eclipse-SourceBundle>org.apache.servicemix.bundles.spring-beans;version="${springframework.version}";roots:="."</Eclipse-SourceBundle>
											</instructions>
										</artifact>
									</artifacts>
								</feature>
							</featureDefinitions>
                            <p2>
                            	<!--  a artifact ready Osgi metadata, get from other P2 repository -->
                                <artifact>
                                    <id>org.apache.batik.anim:1.9.1.v20180528-1434</id>
                                </artifact>
                            </p2>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project></pre>
|}
* open terminal 
<pre>
cd [general.extra.p2]
mvn clean verify -X -e -P buildP2FromMaven -Dtycho.disableP2Mirrors=true
</pre>
it generate p2 at [general.extra.p2]/target/repository
* host your extra bundle to web server or keep it on local directory

=== update target platform ===
# on eclipse open [idempiere-source]/org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target
# add your new p2 link to target platform
# in case you don't want to make a commit to core, you can do bellow step
## duplicate [idempiere-source]/org.idempiere.p2.targetplatform to [somedir]/org.idempiere.p2.extra.targetplatform
## close or remove project [idempiere-source]/org.idempiere.p2.targetplatform
## import [somedir]/org.idempiere.p2.extra.targetplatform to eclipse working-space
## on eclipse open [somedir]/org.idempiere.p2.targetplatform/org.idempiere.p2.targetplatform.target
## add your new p2 link to target platform and active it
## when you run mvn verify on [idempiere-source] append parameter -Didempiere.target=[somedir]/org.idempiere.p2.targetplatform
<pre>
mvn verify -Didempiere.target=[somedir]/org.idempiere.p2.targetplatform
you can use relative path from [somedir] to [idempiere-source] also
</pre>

== Common command ==
* mvn verify -P online
** build idempiere with online profile (will use remote repository to feed all dependency bundle)

== Summary about tycho use on idempiere (for full [http://www.eclipse.org/tycho/sitedocs/index.html reference tycho API]) ==

=== [idempiere-root]/pom.xml ===
setup list of module (bundle) that build with command "mvn verify -P online"

=== [idempiere-root]/org.idempiere.parent/pom.xml ===
* parent pom with common configuration, for other bundle to inherit from this pom
* profiles
** Set properties for build condition, currently use to setup online and offline repository
* repositories
** Setup list of repository to resolve dependency
** with P2 repository use "<layout>p2</layout>"
* dependencies
** define OSGi bundles feed from maven repositories

== Known Issue ==
# Before the completion of step 2.2 (Set Target Platform), you should turn off "Project/Build Automatically". Before the setting of Active Target Platform, build will be slow and full with errors.
# Sometimes due to condition in Sourceforge, the setting of Active Target Platform ends with errors, hindering a complete clean build. Take a look bellow for tips:
[[File:TargetIssue.png|none|thumb]]

== Reference ==
[http://www.eclipse.org/tycho/sitedocs/index.html API to configure tycho]

[http://www.eclipse.org/tycho/sitedocs-extras/index.html More API to configure tycho plugin]

[http://hudson.eclipse.org/tycho/job/tycho-sitedocs/ws/target/staging/tycho-release/tycho-versions-plugin/update-eclipse-metadata-mojo.html Sync bundle version on manifest with version on pom.xml]

[http://wiki.eclipse.org/Tycho/Packaging_Types Tycho package type]

[http://github.com/FTSRG/cheat-sheets/wiki/Maven-and-Eclipse A good summary]

[http://stackoverflow.com/questions/11373009/should-i-use-pom-first-or-manifest-first-when-developing-osgi-application-with-m Idempiere maven tycho is MANIFEST-First]

[http://codeandme.blogspot.co.at/p/tycho-articles.html A complete guide about tycho setup] (Must read)

[http://www.lorenzobettini.it/2015/03/build-your-own-custom-eclipse/ Explain about root-ui for feature]

==See Also==
[[Update_JDK]]
