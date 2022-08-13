// for file parsing and testing
import java.io.*;
import java.util.*;

// for checking program runtime
import java.time.Instant;

// the sphere object
class Sphere {
	double posx;
	double posy;
	double posz;
	double sclx;
	double scly;
	double sclz;
	double r;
	double g;
	double b;
	double Ka;
	double Kd;
	double Ks;
	double Kr;
	int n;
}

// the point light source
class Light {
	double posx;
	double posy;
	double posz;
	double Ir;
	double Ig;
	double Ib;
}

// main class
public class RayTracer {

	/*
		Function: dot
		Input: double array a1, double array a2
		Output: dot product of (a1,a2)
	*/
	public static double dot(double[][] a1, double[][] a2) {
		
		double product = 0.0;
		
		for (int i=0; i<3; i++) {
			product += (a1[i][0]*a2[i][0]);
		}
		return product;
	}
	
	/*
		Function: times
		Input: double array a1, double array a2
		Output: matrix multiplication of (a1,a2)
	*/
	public static double[][] times(double[][] a1, double[][] a2) {
	
		double[][] a3 = new double[4][1];
		
		for (int i=0; i<4; i++) {
			double row = 0;
			for (int j=0; j<4; j++) {
				row += a1[i][j]*a2[j][0];
			}
			a3[i][0] = row;
		}
		
		return a3;
	}
	
	/*
		Function: hit
		Input: ArrayList of general transformations,
			ArrayList of origin transformations,
			ArrayList of spheres, direction matrix, number of spheres
		Output: index of hit sphere; otherwise -1
	*/
	public static int hit(ArrayList<double[][]> TRANSFS_G,
		ArrayList<double[][]> TRANSFS_S, ArrayList<Sphere> SPHERES,
		double[][] dir, double[] C, int size) {
		
		// index of intersected sphere
		int k = -1;
		
		// t intersection value
		double t = 0.0;
		
		// check every sphere
		for (int i=0; i<size; i++) {
			
			// get transformations for origin and ray
			double[][] S = TRANSFS_S.get(i);
			double[][] c = times(TRANSFS_G.get(i),dir);
			
			// calculate values for discriminant
			double A = dot(c,c);
			double B = dot(S,c);
			
			// calculate the discriminant
			double disc = B*B - A*C[i];
			
			// if disc >= 0, calculate closest t
			if (disc >= 0) {
				double mid = -B/A;
				double edge = Math.sqrt(disc)/A;
				double t_near = mid - edge;
				double t_far = mid + edge;
				
				// close t intersection closer than previous t intersection
				if (t_near>=1.0 && (t==0.0 || t_near<t)) {
					t=t_near;
					k=i;
				}
				
				// far t intersection closer than previous t intersection
				else if (t_far>=1.0 && (t==0.0 || t_far<t)) {
					t=t_far;
					k=i;
				}
			}
		}
		
		// return sphere index
		return k;
	}

	/*
		Function: main
		Input: test file as .txt
		Output: output of test file as .ppm
	*/
	public static void main(String[] args) throws Exception {
	
		// use to compare to end of processing time
		long start = Instant.now().toEpochMilli();
		
		/* plane data has 2 values for each line:
			1. name of the data type (not used)
			2. value of the data
		*/
		double near = 0.0;
		double neg_W = 0.0;
		double pos_W = 0.0;
		double neg_H = 0.0;
		double pos_H = 0.0;
		
		/* resolution data has 3 values:
			1. name of the data type (not used)
			2. x value of the data
			3. y value of the data
		*/
		int res_x = 0;
		int res_y = 0;
		
		/* sphere data has 16 values for each line:
			1. name of the data type (not used)
			2. name of the specific sphere (not used)
			3. x position
			4. y position
			5. z position
			6. x scale
			7. y scale
			8. z scale
			9. magnitude of red
			10. magnitude of green
			11. magnitude of blue
			12. ambient coefficient
			13. diffuse coefficient
			14. specular coefficient
			15. reflective coefficient
			16. specular exponent
		*/
		ArrayList<Sphere> SPHERES = new ArrayList<Sphere>();
		
		/*
			Create ArrayLists of transformation matrices for spheres
			TRANSFS_G generally for dynamic vectors (such as direction)
			TRANSFS_S specifically for origin
			Both contain translation and scaling
		*/
		ArrayList<double[][]> TRANSFS_G = new ArrayList<double[][]>();
		ArrayList<double[][]> TRANSFS_S = new ArrayList<double[][]>();
		
		/* light data has 8 values for each line:
			1. name of the data type (not used)
			2. name of the specific light (not used)
			3. x position
			4. y position
			5. z position
			6. red intensity
			7. green intensity
			8. blue intensity
		*/
		ArrayList<Light> LIGHTS = new ArrayList<Light>();
		
		/* background data has 4 values for each line:
			1. name of the data type (not used)
			2. magnitude of red
			3. magnitude of green
			4. magnitude of blue
		*/
		double back_r = 0.0;
		double back_g = 0.0;
		double back_b = 0.0;
		
		/* ambient data has 4 values for each line:
			1. name of the data type (not used)
			2. red intensity
			3. green intensity
			4. blue intensity
		*/
		double ambient_Ir = 0.0;
		double ambient_Ig = 0.0;
		double ambient_Ib = 0.0;
		
		/* output name has 2 values
			1. name of the data type (not used)
			2. name of the output file
		*/
		String OUTPUT = "";
		
		// the most amount of tokens in a single line is 16
		String[] input = new String[16];
	
		// read input from file
		String INPUT = "Raytracer-Tests-and-Keys/" + args[0];
		Scanner in_file = new Scanner(new File(INPUT));
		
		// check every line in the file
		while (in_file.hasNextLine()) {
		
			// program tokenizes new line
			// delimiters are any strings of whitespace
			input = in_file.nextLine().split("\\s+");
			
			// checks which data type the input line is
			String check = input[0];
			
			// new line is near plane
			if (check.equals("NEAR")) {
				near = Double.parseDouble(input[1]);
			}
			
			// new line is left plane
			else if (check.equals("LEFT")) {
				neg_W = Double.parseDouble(input[1]);
			}
			
			// new line is right plane
			else if (check.equals("RIGHT")) {
				pos_W = Double.parseDouble(input[1]);
			}
			
			// new line is bottom plane
			else if (check.equals("BOTTOM")) {
				neg_H = Double.parseDouble(input[1]);
			}
			
			// new line is top plane
			else if (check.equals("TOP")) {
				pos_H = Double.parseDouble(input[1]);
			}
			
			// new line is resolution window
			else if (check.equals("RES")) {
				res_x = Integer.parseInt(input[1]);
				res_y = Integer.parseInt(input[2]);
			}
			
			// new line is a sphere
			else if (check.equals("SPHERE")) {
			
				double test_pos = Double.parseDouble(input[4]);
				double test_scl = Double.parseDouble(input[7]);
				
				if (test_pos-test_scl > -1) continue;
				
				// create new sphere
				Sphere SPHERE = new Sphere();
				
				// centre position
				SPHERE.posx = Double.parseDouble(input[2]);
				SPHERE.posy = Double.parseDouble(input[3]);
				SPHERE.posz = test_pos;
				
				// size
				SPHERE.sclx = Double.parseDouble(input[5]);
				SPHERE.scly = Double.parseDouble(input[6]);
				SPHERE.sclz = test_scl;
				
				// calculate rightmost transformation column
				double transf_x = -SPHERE.posx/SPHERE.sclx;
				double transf_y = -SPHERE.posy/SPHERE.scly;
				double transf_z = -test_pos/test_scl;
				
				// put into ArrayList of general transformation
				double[][] TRANSF_G =
					{{1.0/SPHERE.sclx,0.0,0.0,transf_x},
					{0.0,1.0/SPHERE.scly,0.0,transf_y},
					{0.0,0.0,1.0/test_scl,transf_z},
					{0.0,0.0,0.0,1.0}};
				TRANSFS_G.add(TRANSF_G);
				
				// put into ArrayList of origin transformation
				double[][] TRANSF_S = {{transf_x},
					{transf_y},{transf_z},{1.0}};
				TRANSFS_S.add(TRANSF_S);
				
				// colours
				SPHERE.r = Double.parseDouble(input[8])*255;
				SPHERE.g = Double.parseDouble(input[9])*255;
				SPHERE.b = Double.parseDouble(input[10])*255;
				
				// lighting
				SPHERE.Ka = Double.parseDouble(input[11]);
				SPHERE.Kd = Double.parseDouble(input[12]);
				SPHERE.Ks = Double.parseDouble(input[13]);
				SPHERE.Kr = Double.parseDouble(input[14]);
				SPHERE.n = Integer.parseInt(input[15]);
				
				// add new sphere
				SPHERES.add(SPHERE);
			}
			
			// new line is a light
			else if (check.equals("LIGHT")) {
			
				// create new light
				Light LIGHT = new Light();
				
				// position
				LIGHT.posx = Double.parseDouble(input[2]);
				LIGHT.posy = Double.parseDouble(input[3]);
				LIGHT.posz = Double.parseDouble(input[4]);
				
				// lighting colour
				LIGHT.Ir = Double.parseDouble(input[5])*255;
				LIGHT.Ig = Double.parseDouble(input[6])*255;
				LIGHT.Ib = Double.parseDouble(input[7])*255;
				
				// add new light
				LIGHTS.add(LIGHT);
			}
			
			// new line is the background colour
			else if (check.equals("BACK")) {
				back_r = Double.parseDouble(input[1])*255;
				back_g = Double.parseDouble(input[2])*255;
				back_b = Double.parseDouble(input[3])*255;
			}
			
			// new line is the ambient lighting
			else if (check.equals("AMBIENT")) {
				ambient_Ir = Double.parseDouble(input[1]);
				ambient_Ig = Double.parseDouble(input[2]);
				ambient_Ib = Double.parseDouble(input[3]);
			}
			
			// new line is the output file name
			else OUTPUT = input[1];
		}
		
		// close the input txt file
		in_file.close();
		
		// get number of spheres
		int size = SPHERES.size();
		
		// account for ambient light
		for (int i=0; i<size; i++) {
			SPHERES.get(i).r *= ambient_Ir;
			SPHERES.get(i).g *= ambient_Ig;
			SPHERES.get(i).b *= ambient_Ib;
		}
		
		// calculate dot product minus 1 of each origin transformation matrix
		// with itself; put it in an array for testing hits
		double[] C = new double[size];
		for (int i=0; i<size; i++) {
			double[][] S = TRANSFS_S.get(i);
			C[i] = dot(S,S) - 1.0;
		}
		
		// create direction matrix (starts at top right, points at the near plane)
		double[][] dir = {{neg_W},{pos_H},{-near},{0}};
		
		// create W and H values to update direction matrix
		double W = pos_W/299.5;
		double H = neg_H/299.5;
		
		// create the output file
		FileWriter out_file = new FileWriter(OUTPUT);
		
		// starter values for ppm
		out_file.write("P3\n" + res_x + " " + res_y + "\n" + "255\n");
		
		// write pixel colours to ppm
		for (int i=0; i<res_x; i++) {
			for (int j=0; j<res_y; j++) {
				
				// test if a hit exists
				int k = hit(TRANSFS_G,TRANSFS_S,SPHERES,dir,C,size);
				
				// if hit, write colour of sphere k to the pixel
				if (k>-1) {
					out_file.write(SPHERES.get(k).r + " " +
						SPHERES.get(k).g + " " +
						SPHERES.get(k).b + "\t");
				}
				
				// otherwise, write background colour
				else out_file.write(back_r + " " + back_g + " " + back_b + "\t");
				
				// go to next pixel in the current line
				dir[0][0] += W;
			}
			
			// reset direction vector to the left plane
			dir[0][0] = neg_W;
			
			// set direction vector to the next line of pixels
			dir[1][0] += H;
			
			// start new line for next line of pixels
			out_file.write("\n");
		}
		
		// close the output ppm file
		out_file.close();
		
		// print runtime of program in milliseconds
		long end = Instant.now().toEpochMilli();
		System.out.println("Time: " + (end - start) + " ms");
	}
}