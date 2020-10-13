# Sonar-WekaNose-Plugin

This is a [SonarQube](https://www.sonarqube.org/) plugin that allows to use machine learning algorithm for Code Smell detection.
This plugin was developed during my university internship provided by the [ESSeRE Lab](http://essere.disco.unimib.it/wiki/) (University of Milano Bicocca).

PRE-REQUISITES:
-----------

This plugin can be used only with __WINDOWS Operating Systems__.

NB: Check that the SonarQube folder is _NOT_ inserted in paths where administrative permission is required to access or edit 
(for example, in the Windows Programs folder).

STRUCTURE OF THE ZIP FILE:
-----------

Inside the zip to download are present the Sonar-WekaNose-Plugin JAR, and a folder called "sonar-wekanose-plugin-tools" that contains:

1) A folder called "Algorithms" that contains all the algorithms previously trained with WekaNose to analyze the projects.
It's possible to add personal algorithms (obviously calculated with WekaNose) to this folder.
2) A file called "AddExternalDependencies.properties" to be used if not all the dependencies of the project analyzed in the .pom file 
are specified. To insert a dependency in this file, just specify the entire path of the dependency. Examples of insertion and further 
information can be found within the same file.
3) The JCodeOdor JAR.
4) After the first SonarQube run will appear a new folder called Analysis that is used to contains all the files created during the 
analysis.

NB: __All these files and folders are necessary__ for the correct operation of the plugin.

INSTALLATION:
-----------

1) [Dowload](https://github.com/uazadi/Sonar-WekaNose-Plugin/releases/tag/v1.0) the zip file "Sonar-WekaNose-Plugin.zip".
2) Unpack this file in the "...\SonarQube\extensions\plugins" folder.
3) Start SonarQube, and open it at localhost:9000.
4) Log in as Administrator.
5) Navigate to "Quality Profile" tab, then create a new Java profile called Sonar-WekaNose-Plugin.
6) Set as default the just created profile, then activate as many rules as the number of the algorithms inserted.
7) Run the project to analyze with SonarQube.

POSSIBLE ERRORS:
-----------

If no all additional dependencies are placed in the AddExternalDependencies.properties file, the program remains locked while doing nothing. This is notified during the analysis, if this happens, but it is also possible that the warning message appears during the analysis of a project with many dependencies (in which case it is sufficient to let the execution continue). The message is as follows:

"Be aware that if even one dependency was not specified the computation will ramain stuck, therefore, please check again the dependencies (this is an automated prited message, if you are analysing a large project, it may not be addess to you)"

I ask to be very careful to insert all the additional dependencies in the file mentioned above.

For more details consult the following [User Guide](https://drive.google.com/file/d/1fAA4OJUqjEgITcmx1AdgzeFHOWsXTvQO/view?usp=sharing).

For any problem related to the operation contact: u.azadi@campus.unimib.it
