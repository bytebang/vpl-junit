vpl-junit
=========

Purpose
-------
This project builds a single jar which can be implemented into the [Virtual Programming Lab](http://vpl.dis.ulpgc.es/) plugin for [Moodle Elearning Platform](https://moodle.org/) to run [JUnit 4](https://junit.org/junit4/) tests which are automatically graded.

![Students classes are tested against JUnit tests which are automatically graded](https://github.com/bytebang/vpl-junit/blob/master/doc/student-evaluation.png)

Installation
------------

1. You need to get the latest version of the jar file. You can either compile it by yourself, or just grab the [latest version from github](https://github.com/bytebang/vpl-junit/tree/master/release).
2. Upload it to the execution files within the VPL Plugin
3. Add the jar file under _Advanced settings_ to the _Files to keep when running_ 
4. Modify the `vpl_evaluate.sh` to include the jar into the classpath.
5. Run the jar file
  * If you run it without any arguments then it searches for classes wich are ending in `Test` or `Tests`. 
  * If you want to test only a certain JUnit class (with optional package) then just pass it as commandline parameter to the jar file.

``````````````````````````{.bash}
#!/bin/bash
#load common script and check programs

# Enter timeout for unittests here
vpl_junit_timeout=5

# use the latest available version
vpl_junit_version=$(basename  $(ls vpl-junit*) .b64)

. common_script.sh
check_program javac
check_program java
get_source_files java

#compile all .java files

export CLASSPATH=$CLASSPATH:./$vpl_junit_version
javac -J-Xmx16m -Xlint:deprecation *.java

if [ "$?" -ne "0" ] ; then
  echo "Not compiled"
  exit 0
fi

cat common_script.sh > vpl_execution
echo "timeout $vpl_junit_timeout java -jar $vpl_junit_version" >> vpl_execution
chmod +x vpl_execution
``````````````````````````

Dont forget to set/ modify the desired maximum execution timeout (here it is set to 5 seconds) at the top of the script as needed.

Useage
------

### Scenario 1: Plain unittests

We prefer the paradigm: Convention over configuration. This means that you can write your Unit tests as you used to do. If you want to award points for a JUnit Test then prepend the points you want to award. Here are some examples:

``````````````````````````{.java}
public class SimpleVplTest {

    @Test // Will reward 25 points
    public void testOne_25P() {
        assertTrue(true);
    }

    @Test // Would reward 10 points, but fails -> 0 points
    public void testTwo_10P() {
    	fail("This went wrong");
    }
    
    @Test // Test will be run, but does not reward any points
    public void testThree() {
    	assertTrue("One should be 1", 1==1 );
    }
}
``````````````````````````

### Scenario 2: Check program output

Sometimes you just want to check if a submitted program outputs the correct values for given input values. (Because in the first programming lessons you are most likely playing aroung with modified HelloWorld examples). This can also be achived using the `VplConsoleEmulator` class in your unittests. 

I tried to illustrate this with the `CowSay` example, where the students are asked to draw an ascii-art cow which greets whoever was named within the first parameter of their [CowSay class](https://github.com/bytebang/vpl-junit/blob/master/examples/CowSay.java) 

The corresponding [CowSay-Unittests](https://github.com/bytebang/vpl-junit/blob/master/examples/CowSayTests.java) are testing if the programm reacts with a proper output.


### Scenario 3: Interact with the sumbitted program

When examples become more difficult, then the students start to interact with their own programs via the commandline. The `VplConsoleEmulator` is also able to handle this.

A good example of how you can test an students interactive [commandline calulator](https://github.com/bytebang/vpl-junit/blob/master/examples/SimpleCalculator.java) is given in the corresponding junit tests:
[CalculatorTests](https://github.com/bytebang/vpl-junit/blob/master/examples/CalculatorTests.java)

Here is a sneak preview:

``````````````````````````{.java}
/**
 * Independend Test to check if multiplication was implemented correctly
 * @throws IOException
 */
@Test
public void h_compactTestMultiplication_20P() throws IOException 
{
	VplConsoleSimulator con = new VplConsoleSimulator("SimpleCalculator");
	
	con.skipOutputTill(a-> a.equals("Enter the first integer"));
	con.enterLine("6");
	
	con.skipOutputTill(a-> a.equals("Enter the operation [+, -, *]"));
	con.enterLine("*");
	
	con.skipOutputTill(a-> a.equals("Enter the second integer"));
	con.enterLine("3");
	
	assertTrue(con.expectOutput("The multiplication of 6 * 3 = 18"));
	
}
``````````````````````````

### Scenario 4: Check the programming style of the submissions

This feature uses [checkstyle](http://checkstyle.sourceforge.net/) which has to be installed on the jail server. During the style-checking phase it searches for all checkstyle_xx.xml files and invokes checkstyle against every java file that does not look like a unit test. If checkstyle is not found or if there are no java files, then this tests are skipped.

If there are no violations (of type `WARN`)) then the graded points will be the points from the unit tests. Every violation counts as -1 Point. This means that if you have a program with 75 points grading from the unittests, but there are 10 style-violations found then the final grade will be 65 points.  

You can also limit the maximum deduction of points by adding a prefix to the checkstyle file. Lets say your style checks are within the file checkstyle_myschool-10P.xml then the maximum deduction which can becaused by this file will be limited to -10 points.

**Attention**: The jail server prohibits network connections. Since the default checkstyle runs are written in XML with an external document type definition checkstyle wants to download referenced `https://checkstyle.org/dtds/configuration_1_3.dtd`

Since this is doomed to fail you have to embed the dtd into each checkstyle configuration itself. After the dtd you can embed all [checks](http://checkstyle.sourceforge.net/checks.html) as usual. Here is an example how this looks like:

``````````````````````````{.xml}
<?xml version="1.0" standalone="yes"?>
<!DOCTYPE module [
    <!ELEMENT module (module|property|metadata|message)*>
    <!ATTLIST module name NMTOKEN #REQUIRED>
    <!ELEMENT property EMPTY>
    <!ATTLIST property
        name NMTOKEN #REQUIRED
        value CDATA #REQUIRED
        default CDATA #IMPLIED
    >
    <!ELEMENT metadata EMPTY>
    <!ATTLIST metadata
        name NMTOKEN #REQUIRED
        value CDATA #REQUIRED
    >
    <!ELEMENT message EMPTY>
    <!ATTLIST message
        key NMTOKEN #REQUIRED
        value CDATA #REQUIRED
    >
]>

<!-- Checkstyle configuration which checks the naming of functions -->

<module name = "Checker">
    <property name="charset" value="UTF-8"/>
    <property name="severity" value="warning"/>
    <property name="fileExtensions" value="java"/>

    <module name="TreeWalker">
         <module name="MethodName">
		   <property name="format" value="^[a-z](_?[a-zA-Z0-9]+)*$"/>
		</module>
    </module>
</module>
``````````````````````````
Further examples can be found in the [checkstyle directory](https://github.com/bytebang/vpl-junit/blob/master/checkstyle/) of the project. Dont forget to add the checks that you want to perform  under _Advanced settings_ to the _Files to keep when running_. Otherwise vpl deletes the checkstyle definitions after compilation and the checks will not be started.

Building the library
--------------------

The latest release can be built with ant. Just checkout the code an run `ant`. The newest release will be put into the release directory.


Help & Support
--------------

If you need help with this or if you have any suggestions / remarks please file an [issue](https://github.com/bytebang/vpl-junit/issues) at the GitHub project.
