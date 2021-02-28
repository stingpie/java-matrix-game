import java.lang.Math;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// this is a class designed to process math concerning 3d shapes. 
// it currently calculates the volume & surface area for 4 primitive shapes
// it can also calculate multiple primitives in the same shape, but it's
// no where near accurate. 

public class shape {
	private double[] size= {0,0,0};
	//private int scale=0;
	private double surface=0;
	private boolean[] primitives= {false,false,false,false}; // rectangle, cone, sphere, cylinder 
	private double vol=0;
	// primitives give the shape "custom" surface area/volume definitions.
	// the final surface area is the sum of the primitive's area equations. 
	
	public shape(){
		
	}
	
	public shape(String file, int index) throws Exception{
		String[] z=loader(file);
		String b = z[index];
		
		string_load(b.split("PUT_SPACE_HERE"));
	
	}
	
	public shape(double x, double y, double z) {
		size[0]=x; size[1]=y; size[2]=z;
		primitives[0]=true; // assume it's a rectangular prism.
		surface =calc_surface_area(); // determine surface area
		vol=calc_volume();
		
	}
	
	public shape(double x, double y, double z, boolean rectangle, boolean cone, boolean sphere, boolean cylinder) {
		this.primitives[0]=rectangle; this.primitives[1]=cone; this.primitives[2]=sphere; this.primitives[3]=cylinder;
		size[0]=x; size[1]=y; size[2]=z;
		surface =calc_surface_area(); // determine surface area
		vol=calc_volume();
	}
	
	
	private double calc_surface_area(){
		double surface_area=0;
		
		
		int count=0;				// figure out how many primitives construct the shape. 
		for(int i=0; i<4;i++) {
			if(primitives[i]) {
				count++;
			}
		}
		
		if(primitives[0]) { // if there is a rectangular prism,
			
			for(int i=0; i<3; i++) {
				surface_area+=(size[i]*size[(i+1)%3])*2;
			} 
			
		}
		
		if(primitives[1]) { // if there is a cone,
			double rad=(size[0]/2);
			surface_area+=3.1415*rad*rad + 3.1415 * rad *Math.sqrt(rad*rad+size[1]+size[1]); // cone area
		}
		
		if(primitives[2]) {   // sphere calculation
			surface_area+=4*3.1415*(size[0]/2)*(size[0]/2);
		}
		
		if(primitives[3]) {  // if cylinder
			double rad=(size[0]/2);
			surface_area+=2*3.1415*rad*size[1]+2*3.1415*rad*rad; //cylinder equation
		}
		surface_area/=count;  // divide the surface area by the amount of primitives.
		return   surface_area;
	}
	
	private double calc_volume() { // calculates the volume of the shape
		double volume=0;
		
		int count=0;				// figure out how many primitives construct the shape. 
		for(int i=0; i<4;i++) {
			if(primitives[i]) {
				count++;
			}
		}
		
		if(primitives[0]) { //rectangle
			volume+=size[0]*size[1]*size[2];
		}
		if(primitives[1]) { //cone
			double rad=(size[0]/2);
			volume+=(3.1415*rad*rad*size[1])/3;
		}
		if(primitives[2]) { //cylinder
			double rad=(size[0]/2);
			volume+=(3.1415*rad*rad*size[1]);
		}
		if(primitives[3]) { //sphere
			double rad=(size[0]/2);
			volume+=(4*3.1415*rad*rad*rad)/3;
		}
		
		return  volume/count;
	}
	
	
	
	public double get_volume() {
		return this.vol;
	}
	
	
	
	public void set_size(double x, double y, double z) {
		size[0]=x; size[1]=y; size[2]=z;
		surface =calc_surface_area(); // determine surface area
		vol=calc_volume(); // calculate new volume
	}
	
	public double[] get_size(){
		return this.size;
	}
	
	public double get_surface() {
		return this.surface;	
	}
	
	public void set_primitives(boolean rectangle, boolean cone, boolean sphere, boolean cylinder) {
		this.primitives[0]=rectangle; this.primitives[1]=cone; this.primitives[2]=sphere; this.primitives[3]=cylinder;
		surface=calc_surface_area();
		vol=calc_volume();
	}
	
	public boolean[] get_primitives() {
		return this.primitives;
	}
	
//	public void set_scale(int new_scale) {
//		this.scale=new_scale;
//	}
//	public int get_scale() {
//		return this.scale;
//	}

	public void random() {
		
		primitives[0]= Math.random()>0.5; primitives[1]= Math.random()>0.5; primitives[2]= Math.random()>0.5; primitives[3]= Math.random()>0.5;
		set_size(Math.random()*10,Math.random()*10,Math.random()*10);
	}
	
	
	
	
	public String[] loader(String name) throws Exception {
		
		String[] whole_text=new String[0]; // create array to hold the text strings
		
		Path filePath = Paths.get(name); // set path
		Charset charset = StandardCharsets.UTF_8; // set the correct characterset
		//try {
			
			whole_text = (Files.readAllLines(filePath, charset)).toArray(new String[0]); // actually load the data into a single object
			
		//} catch (IOException ex) {  // if that fails, print I/O EXCEPTION
			//System.out.format("I/O Exception", ex);
		//}
		String running_total="";
		for(int i=0; i<whole_text.length; i++) {
			running_total+=whole_text[i]+"PUT_SPACE_HERE";
		}
		whole_text=running_total.split("&");
		
		return whole_text;
	}
	
	
	public void string_load(String[] data) throws Exception{
		for(int i=0; i<data.length;i++) {
			data[i]=data[i].replaceAll("\\s", "");
			
			if(data[i].indexOf(':')>=0) {
				int z=data[i].indexOf(':');
				switch (data[i].substring(1,z)) {
				case "primitive_cube": primitives[0] = (data[i].substring(z+1,data[i].length()-1).equalsIgnoreCase("true"));		break;
				case "primitive_cone": primitives[1] = (data[i].substring(z+1,data[i].length()-1).equalsIgnoreCase("true"));		break;
				case "primitive_sphere": primitives[2] = (data[i].substring(z+1,data[i].length()-1).equalsIgnoreCase("true"));		break;
				case "primitive_cylinder": primitives[3] = (data[i].substring(z+1,data[i].length()-1).equalsIgnoreCase("true"));	break;
				case "primitive_x": size[0] = Float.parseFloat( data[i].substring(z+1,data[i].length()-1));							break;
				case "primitive_y": size[1] = Float.parseFloat( data[i].substring(z+1,data[i].length()-1));							break;
				case "primitive_z": size[2] = Float.parseFloat( data[i].substring(z+1,data[i].length()-1));							break;
				}
			}
		}
		surface=calc_surface_area();
		vol=calc_volume();
	}
				
	public shape copy() {
		shape new_shape = new shape();
		new_shape.set_primitives(primitives[0], primitives[1], primitives[2], primitives[3]);
		new_shape.set_size(size[0], size[1], size[2]);
		return new_shape;
	}
}
