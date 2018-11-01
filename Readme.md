vpl-junit
=========

Purpose
-------
This project builds a single jar which can be implemented into the [Virtual Programming Lab](http://vpl.dis.ulpgc.es/) plugin for [Moodle Elearning Platform](https://moodle.org/ ) to run [JUnit 4](https://junit.org/junit4/) tests which are automatically graded.


Installation
------------

1. You need to get the latest version of the jar file. You can either compile it by yourself, or just grab the [latest version from github](https://github.com/bytebang/vpl-junit/tree/master/release).
2. Upload it to the execution files within the VPL Plugin
3. Modify the `vpl_evaluate.sh` to include the jar into the classpath.
4. Run the jar file
	a. If you run it without any arguments then it searches for classes wich are ending in `Test` or `Tests`. 
	b. If you want to test only a certain JUnit class (with optional package) then just pass it as commandline parameter to the jar file.

``````````````````````````{.bash}
#!/bin/bash
#load common script and check programs

. common_script.sh
check_program javac
check_program java
get_source_files java

#compile all .java files

export CLASSPATH=$CLASSPATH:./vpl-junit-0.1.jar
javac -J-Xmx16m -Xlint:deprecation *.java

if [ "$?" -ne "0" ] ; then

  echo "Not compiled"
  exit 0

fi

cat common_script.sh > vpl_execution
echo "java -jar vpl-junit-0.1.jar" >> vpl_execution
chmod +x vpl_execution
``````````````````````````

Useage
------

Write your Unit tests as you are used to do. If you want it to be graded, then add the annotation `@RunWith(VplGrader.class)` to your class. Every JUnit test (functions with the `@Test` annotation which has an addtional `@VplTestcase` annotation is graded.

Each testcase weights 10 points. If you want to change this, then you can add the points to the annotation. Eg: `@VplTestcase(points=15)`


Building the library
--------------------

The latest release can be built with ant. Just checkout the code an run `ant`. The newest release will be put into the release directory.


Help & Support
--------------

If you need help with this or if you have any suggestions / remarks please file an [issue](https://github.com/bytebang/vpl-junit/issues) at the GitHub project.
