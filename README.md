# sonar-android-application #
The open source SonarQube code analyzer tool for your Android device. It allows you to view server, project and analysis information.

### Documentation ###

See the [Confluence pages](http://docs.codehaus.org/display/SONAR/SonarQube+Android+Application) for this project.

### Issue tracker ###

See the [JIRA tracker](http://jira.codehaus.org/browse/SONARPLUGINS/component/16156) for this project.

### Building in Eclipse ###

In order for this project to build in Eclipse certain resources from the
AppCompat support library are required to be present in your Eclipse workspace.

Check [Adding libraries with resources](http://developer.android.com/tools/support-library/setup.html#add-library)
for information on how to import the `android-support-v7-appcompat` project into
your Eclipse workspace and add the necessary library reference.


### Details ###

* Name SonarQube Android Application (sonar-android-application)
* Latest version	0.1.0
* License LGPL v3
* Minimum Android version	Android 2.2, API Level 8
* Author name Balázs Bakai (http://www.bakaibalazs.hu/)
* Wiki	SonarQube Android Application
* Git https://github.com/SonarCommunity/sonar-android-application
* Jira	SONARPLUGINS/component/16156
* Jenkins	https://sonarplugins.ci.cloudbees.com/job/sonar-android-application
* Nemo	http://nemo.sonarqube.org/dashboard/index/sonar-android-application:java
* Documentations Jenkins configuration guide
* Google Play download	https://play.google.com/store/apps/details?id=hu.balazsbakai.sq&hl=en


### RoadMap ###
2015 December:

* Server management will be on a new viewpager component
* SearchBy feature: projects, users, plugins
* Project details: measures (charted), issues, action plans
* Favorites (projects, etc...)
* Notifications (new SonarQube instance is out...)
* Compuware Mobil ADK integration

### Releases ###

* 0.1.0 Initial Release
* Listing projects, users and plugins per server
* Server management: add, delete
* Adding public servers from a list
* Sharing, rating and donation functions
* Supported languages: English, Hungarian
