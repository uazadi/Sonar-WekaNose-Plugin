# Sonar-WekaNose-Plugin

SonarQube plugin for Code Smell detection through machine-learning techniques.
This plugin was developed during my university internship provided by the ESSeRe Lab (University of Milano Bicocca).

PRE-REQUISITES:

This plugin can be used only with WINDOWS Operating Systems.

STRUCTURE OF THE ZIP FILE:

Inside the zip to download are present:
1) A folder called "sonar-wekanose-plugin-tools" that contains:
    a) A folder called "Algorithms" that contains all the algorithms previously trained with WekaNose to analyze the projects.
      It's possible to add personal algorithms (obviously calculated with WekaNose) to this folder.
    b) A file called "AddExternalDependencies.properties" to be used if not all the dependencies of the project analyzed in the .pom file 
    are specified. To insert a dependency in this file, just specify the entire path of the dependency. Examples of insertion and further 
    information can be found within the same file.
    c) The JCodeOdor JAR.
    d) After the first SonarQube run will appear a new folder called Analysis that is used to contains all the files created during the 
    analysis.
2) The Sonar-WekaNose-Plugin JAR.

NB: All these files and folders are necessary for the correct operation of the plugin.

INSTALLATION:

1) Download the zip file "Sonar-WekaNose-Plugin.zip".
2) Unpack this file in the "...\SonarQube\extensions\plugins" folder.
3) Start SonarQube, and open it at localhost:9000.
4) Log in as Administrator.
5) Navigate to "Quality Profile" tab, then create a new Java profile called Sonar-WekaNose-Plugin.
6) Set as default the just created profile, then activate as many rules as the number of the algorithms inserted.
7) Run the project to analyze with SonarQube.

For more details consult my batchelor thesis.
For any problem related to the operation contact: alessandro.polastri94@gmail.com
