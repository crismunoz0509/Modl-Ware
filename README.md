# Modl-Ware
## Installation
1. Download the latest version of JavaFX SDK at https://openjfx.io/ and unzip the package
2. Download Zulu Java 8 (LTS) from https://www.azul.com/downloads/?version=java-8-lts&package=jdk-fx Ensuring the Java Package is JDK FX. Then unzip the package
3. Download and install the latest version of Apache Netbeans https://netbeans.apache.org/download/index.html

## Setup

### Platform Manager
1. Once everything is downloaded, open Apache Netbeans and navigate under _Tools > Java Platforms_ in order to open the Java Platform Manager
2. Inside the _Java Platform Manager_ click _Add Platform..._
3. Select _Java Standard Edition_ for the platform type
4. Navigate to where your _zulu-9.jdk_ folder is, go into it's contents, then select Home and click Next
5. Give the platform a name and click done

### Libraries
1. In Apache Netbeans navigate under _Tools > Libraries_ to open up the Ant Library Manager
2. Select _New Library..._
3. Name the library JavaFX 19 (or the that was version downloaded)
4. find and select the name of the library under _Libraries:_
5. Click _Add JAR/Folder..._ and navigate to the location where the unzipped/downloaded JavaFX SDK is and open the _lib folder_
6. Select all the JavaFX files ending with .jar

### New Project
1. Start a new project
2. Under _Java with Ant_ select JavaFX then JavaFX Application and click next
3. Change the _JavaFX Platform_ to JDK 1.8 (the name of the platform we added in the Platform Manager section) and click finish
4. Navigate to the project properties by selecting _File > Project Properties_
5. Go to the Libraries section and go to Compile
6. Click on the '+' sign next to _Classpath_ and select all the .jar files inside of your JavaFX SDK library download
7. Make sure to update each file so they begin with "package your_file_name;" and not "package javafxtesting;"
8. Update the FXML.fxml file so that the AnchorPane says _fx:controller="your_file_name.Controller"_
9. Run the application

# How to
### Nodes
- Click on the NODES button to activate the node tool
- Type a name into the node text field to name newly placed nodes
- Click on the canvas to place a node
- Click on a node with the NODES button activated to edit the node name OR delete the node
### Edges
- Click on the EDGES button to activate the edge tool
- Type a name into the edge text field to name newly placed edge
- Click on one node to select the "down point" 
- Click on another to create an edge/relationship between the two nodes
- Edges have a direction based on the node that was selected first towards the node that was selected second
- Click on an Edge with the EDGES button activated to edit the relation/edge name OR delete the edge

### Output
- Click on output to produce a `.als` file that can be read by Alloy
- Choose where you want this file to be produced by changing the file location in the `Controller.java` file in the `OutputButtonPressed()` method
