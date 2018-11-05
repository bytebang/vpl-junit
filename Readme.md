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

vpl_junit_version=0.3
vpl_junit_timeout=5

. common_script.sh
check_program javac
check_program java
get_source_files java

#compile all .java files

export CLASSPATH=$CLASSPATH:./vpl-junit-0.2.jar
javac -J-Xmx16m -Xlint:deprecation *.java

if [ "$?" -ne "0" ] ; then

  echo "Not compiled"
  exit 0

fi

cat common_script.sh > vpl_execution
echo "timeout $vpl_junit_timeout java -jar vpl-junit-$vpl_junit_version.jar" >> vpl_execution
chmod +x vpl_execution
``````````````````````````
Dont forget to set the actual version you are running the desired execution timeout (here it is set to 5 seconds) at the top of the script as needed.

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

Building the library
--------------------

The latest release can be built with ant. Just checkout the code an run `ant`. The newest release will be put into the release directory.


Help & Support
--------------

If you need help with this or if you have any suggestions / remarks please file an [issue](https://github.com/bytebang/vpl-junit/issues) at the GitHub project.
