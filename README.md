= SEPIA - Strategy Engine for Programming Intelligent Agents =

== Introduction ==

SEPIA is a real-time strategy (RTS) game engine intended specifically as a testbench for artificial intelligence research. It is based of of Wargus, a Warcraft II clone (http://wargus.sourceforge.net), but has a number of distinguishing features:

* Graphics and sound are not included in the base engine. This makes SEPIA lightweight and extremely fast.
* Written in Java to make it more accessible to students.
* Resources and units are highly configurable. Attributes can be modified by editing XML files.
* Features such as fog of war can be turned on or off, providing a sliding scale of difficulty.
* Player agents are embedded in the same JVM as the game engine. Agents extend a base class instead of sending and receiving data over sockets.
* Multiple copies of the game engine can exist in the same process at the same time and can start from any state.

== Repository Structure ==

* Sepia - the core classes for the SEPIA engine. Requires the libraries listed under "Required Libraries".
* SepiaAgents - a collection of sample agents. Requires Sepia plus its dependencies.
* SepiaGUI - a visual debugger agent and a map editor. Requires SepiaAgents plus its dependencies.

== Required Libraries ==

This project requires the following libraries as either direct or indirect dependencies:

Compile time dependencies:
* Apache Commons Configuration (1.9): http://commons.apache.org/proper/commons-configuration/

Runtime dependencies:
* Apache Commons Logging (1.1): http://commons.apache.org/proper/commons-logging/
* Apache Commons Lang (2.6): http://commons.apache.org/proper/commons-lang/

== Java Version ==

SEPIA uses functionality introduced in Java 1.7 (multi-catch, new classes, etc.). While use of the latest version of Java is recommended, it will also work under 1.5 or 1.6 with minimal code changes.

== How to Build ==

Each project includes a build.xml file for Apache Ant (http://ant.apache.org). All build files assume that the required libraries are placed in Sepia/lib/. The build files for SepiaAgents and SepiaGui assume that these folders are siblings of the Sepia folder. 

Note - the build files for SepiaGui and SepiaAgents are still under development and assume that Sepia has been compiled into .class files in Sepia/bin (Eclipse will do this automatically).

== How to Run ==

SEPIA can be embedded into other applications, but it also comes with a standalone entry point. To use this, you must use edu.cwru.sepia.Main as your main class and provide a configuration file as the only argument (see Sepia/data/8pffaConfig.xml for an example).
