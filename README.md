A Java implementation of a simple raytracer.

Included folders:
- Raytracer-Tests-and-Keys: the assignment test files with keys


Included files:

Class files
- Light.class: the class file for light objects
- RayTracer.class: the class file for the ray tracer
- Sphere.class: the class file for the sphere objects

Java files
- RayTracer.java: the java file for the ray tracer

Outputted .ppm files from my raytracer
- testAmbient.ppm: the outputted file for testAmbient.txt
- testBackground.ppm: the outputted file for testBackground.txt
- testBehind.ppm: the outputted file for testBehind.txt
- testDiffuse.ppm: the outputted file for testDiffuse.txt
- testIllum.ppm: the outputted file for testIllum.txt
- testImgPlane.ppm: the outputted file for testImgPlane.txt
- testIntersection.ppm: the outputted file for testIntersection.txt
- testParsing.ppm: the outputted file for testParsing.txt
- testReflection.ppm: the outputted file for testReflection.txt
- testSample.ppm: the outputted file for testSample.txt
- testShadow.ppm: the outputted file for testShadow.txt
- testSpecular.ppm: the outputted file for testSpecular.txt


Note: all .ppm files already created to speed up runtime


RayTracer.java FULL DETAIL description:

Imported libraries
- io: for file input and output
- util: for scanning files
- time dot Instant: for checking program runtime

Classes
- Sphere: includes all sphere input values except for name (all floats are doubles as to be consistent with Jama Matrix)
- Light: includes all light input values except for name (all floats are doubles as to be consistent with Jama Matrix)
- RayTracer: the main class to run the ray tracer

RayTracer functions:
- dot: calculates the dot product of 2 matrices, m1 and m2 (Jama doesn't include dot product)
- hit: checks for closest t intersection with a sphere
- main: the main function, throws file exception

Hit function:
- Define index and t variables
- Check every sphere for an intersection
- Skip sphere if impossible to hit
- Calculate discriminant from quadratic with transformation matrices
- If discriminant is >= 0, then calculate near and far t
- If far t is <= current t, t = far t
- Then, if near t is <= current t, t = near t
- Return index of nearest sphere based on t intersection

Main function:

Start
- Find the current number of milliseconds since Jan 1, 1970
- Declare all variables and objects to read from input file
- Sphere and Light matrices are ArrayLists as we do not yet know how many Spheres and Lights exist yet
- Declare all transformation matrices

Read file
- Scan file input with Scanner from java dot util
- Tokenize each line with any whitespace and/or tab strings
- Put values into appropriate variables and ArrayLists
- Calculate all sphere transformation matrices to use for hits later
- Delete input string; not needed anymore
- Close input file to prevent any EOF errors

Calculate values before outputting to file
- Calculate number of spheres
- Check which spheres are impossible to hit (program then ignores calculating their mathematical values)
- Implement ambient lighting to sphere colours
- Calculate dot product minus 1 of each origin transformation matrix with itself; used when testing for hits
- Create direction matrix
- Create height and width matrices to add to direction when outputting .ppm pixels

Output to file
- Create output .ppm file
- Write starter values for .ppm file
- Scan entire 600x600 .ppm file

Set file pixel values
- Set i=0
- Set j=0
- Get hit sphere colours; output sphere colours to file
- If no hit, output background colour
- j++
- Move direction matrix one pixel to the right, then repeat above 3 steps until j=res_y
- i++
- Move direction matrix on pixel down, then repeat above 6 steps until i=res_x
- Close the file

Terminate the program
- Find the current number of milliseconds since Jan 1, 1970
- Output the difference of milliseconds between current milliseconds and milliseconds retrieved when program started; this is the runtime in milliseconds


Successful output files
- testAmbient
- testBackground
- testBehind
- testImgPlane
- testIntersection


Unsuccessful output files
- testDiffuse: No lighting nor shadows on spheres
- testIllum: No lighting nor shadows on spheres
- testParsing: No lighting nor shadows on spheres
- testReflection: No lighting nor reflections on spheres
- testSample: No lighting nor shadows on spheres
- testShadow: No lighting nor shadows on spheres
- testSpecular: No specular lighting on spheres
>>>>>>> master
