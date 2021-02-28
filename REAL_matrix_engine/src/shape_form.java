import java.lang.Math;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class shape_form {
	private float spikeness=0;
	private float scale=1;
	private int spikenum=0;
	private int intricacies=0;
	private int cavity=0; // how much space is INSIDE the form (this would be a cardboard box thing)
	boolean puddle=false;
	
	String design= "";
	shape basic_form=new shape();
	
	public shape_form(String file, int index) throws Exception{
		String[] z=loader(file);
		String b = z[index];
		
		string_load(b.split("PUT_SPACE_HERE"));
	}
	
	
	public shape_form() {
		basic_form=new shape();
		make_cube();
	}
	
	// puddle form:
	public void make_puddle(/*int volume*/) {
		double sides= (float) Math.cbrt(basic_form.get_volume());
		//System.out.println(sides);
		//System.out.println(basic_form.get_volume());
		
		basic_form.set_primitives(false, false, false, true);
		
		//System.out.println((int)(4*sides/Math.PI));
		basic_form.set_size((4*sides/Math.PI)*0.97445385/*<-- seems to be a magic number */, 1, 1);
		
		//System.out.println(basic_form.get_volume());
		
		spikeness=0;
		scale=1;
		spikenum=0;
		intricacies=0;
		cavity=0;
		puddle=true;
	}
	
	//Very basic, primitive shapes
	public void make_cube() { // gives this item the properties of a flat cube.
		basic_form.set_primitives(true,false,false,false); // set form to rectangular prism
		basic_form.set_size(1,1,1); // 1x1x1 cube
		set_spikenum(8); // cubes have eight vertexes
		set_spikeness((float) Math.sqrt(3*Math.pow(0.5,2)));  //the height of a vertex of a cube from a perfect sphere
		set_intricacies(0); // flat & boring
		cavity=0; // solid
		puddle=false;
	}
	
	public void make_sphere() { // gives this item the properties of a flat sphere.
		basic_form.set_primitives(false,false,true,false); // set form to sphere
		basic_form.set_size(1,1,1); // 1x1x1 sphere
		set_spikenum(0); // NO SPIKES ON SPHERES 
		set_spikeness(0);  
		set_intricacies(0); // flat & boring
		cavity=0; // solid
		puddle=false;
	}
	
	public void make_cone() { // gives this item the properties of a flat cone.
		basic_form.set_primitives(false,true,false,false); // set form to cone
		basic_form.set_size(1,1,1); // 1x1x1 cone
		set_spikenum(1); //the cone IS a spike
		set_spikeness(1); // the spike is the entire length of the cone  
		set_intricacies(0); // flat & boring
		cavity=0; // solid
		puddle=false;
	}
	
	public void make_cylinder() { // gives this item the properties of a flat cylinder.
		basic_form.set_primitives(false,false,false,true); // set form to cylinder
		basic_form.set_size(1,1,1); // 1x1x1 cylinder
		set_spikenum(0); // no spike
		set_spikeness(0);   
		set_intricacies(0); // flat & boring
		cavity=0; // solid
		puddle=false;
	}
	
	
	public void set_scale(float new_scale) {
		scale=new_scale;
		basic_form.set_size((basic_form.get_size()[0]*scale),(basic_form.get_size()[1]*scale),(basic_form.get_size()[2]*scale));  // resize basic_shape
	}
	
	public float get_scale() {
		return this.scale;
	}


	public float get_spikeness() {
		return spikeness*scale;
	}

	public void set_spikeness(float new_spikeness) {
		this.spikeness = new_spikeness;
	}

	public int get_spikenum() {
		return spikenum;
	}

	public void set_spikenum(int spikenum) {
		this.spikenum = spikenum;
	}


	public int get_intricacies() {
		return intricacies;
	}

	public void set_intricacies(int intricacies) {
		this.intricacies = intricacies;
	}


	public int get_cavity() {
		return (int)(cavity*scale);
	}

	public void set_cavity(int cavity) {
		this.cavity = cavity;
	}
	
	public void set_basic_form(shape new_form) {
		this.basic_form=new_form;
	}
	public shape get_basic_form() {
		return this.basic_form;
	}
	
	
	
	public void random() {
		basic_form.random();
		spikeness=(float)Math.random()*3;
		scale=1;
		spikenum=(int)(Math.random()*10);
		intricacies=(int)(Math.random()*10);
		cavity=(int)(Math.random()*basic_form.get_volume()); // how much space is INSIDE the form (this would be a cardboard box thing)
	}
	
	
	public void load_from(String name, int index) throws Exception {
		
		String[] z=loader(name);
		String b = z[index];
		
		string_load(b.split("PUT_SPACE_HERE"));
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
				case "spike":  spikeness=Integer.parseInt(data[i].substring(z+1,data[i].length()-1));
					break;
				case "scale": scale=Integer.parseInt(data[i].substring(z+1,data[i].length()-1));
					break;	
				case "spike_num": spikenum=Integer.parseInt(data[i].substring(z+1,data[i].length()-1));
					break;
				case "intricate": intricacies=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); 
					break;
				case "cavity": cavity=Integer.parseInt(data[i].substring(z+1,data[i].length()-1));
					break;
				case "design": design=data[i].substring(z+1,data[i].length()-1);
					break;
				case "form": basic_form=new shape("shapes.txt",Integer.parseInt(data[i].substring(z+1,data[i].length()-1)));
					break;
				}
			}
		}
	}
	
	
	
	
	public shape_form copy() {
		shape_form new_form = new shape_form();
		new_form.set_spikeness(spikeness);
		new_form.set_spikenum(spikenum);
		new_form.set_scale(scale);
		new_form.set_intricacies(intricacies);
		new_form.set_cavity(cavity);
		new_form.set_basic_form(basic_form.copy());
		return new_form;
	}
}
