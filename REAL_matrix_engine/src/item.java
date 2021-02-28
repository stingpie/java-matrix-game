import java.lang.Math;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class item {
	// items are placed in stacks on tiles. Hopefully, there will one day be a character to use them. 
	// has a bunch of modifiable properties, it's really intended to be complicated. 
	
	private shape_form form; 	// form defines the structural properties of the item.
	private material mat; 		// mat defines the material properties of the item. 
	private int temp=10000;
	private long ID; 			// unused. Maybe it'll be useful later? 
	double weight =0;			// calculated by recalculate. density times volume
	private char graphic=' ';	// what graphic should be displayed while looking at this item. 
	private item[] aggregate= new item[1]; // aggregates multiple items into a single item
	private boolean priority=false;
	
	
	public item() {
		ID=(long)(Math.random()*2147483647);
		form = new shape_form();
		mat = new material();
		
	}
	
	
	public item(String file, int index) throws Exception { // first of the loaders. It loads a set of parameters from a file. 
		String[] z=loader(file);
		String b = z[index];
		
		string_load(b.split("PUT_SPACE_HERE"));
		
		
	}
	
	public item(shape_form new_form, material new_mat) {
		ID=(long)Math.random();
		form=new_form;
		mat=new_mat;
		weight = form.basic_form.get_volume() * mat.get_density();
	}
	
	public void temp_change() { // change the temperature, but also change the graphic. 
		if(temp > mat.get_boiling_temp()) {
			mat.change_density(-9);
			mat.change_elastic(-10);
			calc_weight();
			graphic='#';
		}else if (temp > mat.get_melting_temp()) {
			//System.out.println(temp);
			if(! form.puddle) {
				form.make_puddle(); // turn into puddle
			}
			mat.change_density(2);
			mat.change_elastic(-10);
			calc_weight();
			graphic='O';
			
		}else {mat.change_elastic(0); mat.change_density(0); calc_weight(); if(form.puddle){ graphic='*';  } }
	}
	
	public void aggregate_item(item new_item) { // adds a new material to the aggregate
		//System.out.println(new_item);
		//System.out.println(aggregate[0]);
		if(get_aggregate()[0]==null) {
			get_aggregate()[0]=new item();
			
		}
		set_aggregate(Arrays.copyOf(get_aggregate(), get_aggregate().length+1));
		get_aggregate()[get_aggregate().length-1]=new_item;
		
		float aggregate_weight=0;
		float aggregate_volume=0;
		
		for(int i=1; i<get_aggregate().length-1; i++) {  // set the properties of this aggregate to be the best attributes of it's components ('cept material properties)
			get_form().set_cavity(Math.max(get_form().get_cavity(),get_aggregate()[i].get_form().get_cavity()));
			get_form().set_spikeness(Math.max(get_form().get_spikeness(),get_aggregate()[i].get_form().get_spikeness()));
			get_form().set_spikenum(Math.max(get_form().get_spikenum(),get_aggregate()[i].get_form().get_spikenum()));
			get_form().set_intricacies(Math.max(get_form().get_intricacies(),get_aggregate()[i].get_form().get_intricacies()));
			
			aggregate_weight+=get_aggregate()[i].get_weight();
			aggregate_volume+=get_aggregate()[i].get_form().get_basic_form().get_volume();
		}
		
		shape new_form = new shape();
		shape_form new_shape_form = new shape_form();
		new_shape_form.set_basic_form(new_form);
		set_new_form(new_shape_form);
		//System.out.println(aggregate_weight);
		weight=aggregate_weight;
		get_form().get_basic_form().set_primitives(true, false, false, false);
		get_form().get_basic_form().set_size(Math.cbrt(aggregate_volume), Math.cbrt(aggregate_volume), Math.cbrt(aggregate_volume));
			
		
	}
	
	
	public int deliberate_attack_dmg(int force) { // not useful right now. It's mostly just for testing form & mat. 
		int random=(int)(Math.random()*100);
		//System.out.println(random);
		//System.out.println((form.get_spikenum()*100) / (form.basic_form.get_surface()/2));
		
		if(random < (form.get_spikenum()*100) / (form.basic_form.get_surface()/2)) { // if hitting another creature, test to see of a spike hit them. 
			//System.out.println("spike!");
			return (int)(force * (form.get_spikeness()+1) * weight);  // calculate the damage of a spike 
			
		}
		//System.out.println("no spike!");
		return (int) (force*weight); // calculate bludgeon damage
		
	}
	public int accidental_attack_dmg(int force) {  // not useful right now. It's mostly just for testing form & mat. 
		System.out.println((form.get_spikenum()*100) / (form.basic_form.get_surface()));
		
		if((int)(Math.random()*100) <(form.get_spikenum()*100) / (form.basic_form.get_surface())) { // if hitting another creature, test to see of a spike hit them. 
			System.out.println("spike!");
			return (int)(force * (form.get_spikeness()+1) * weight);  // calculate the damage of a spike 
		}
		System.out.println("no spike!");
		return (int) (force*weight); // calculate bludgeon damage
	}
	
	public void set_form(shape_form new_form) {
		form=new_form;
		weight = form.basic_form.get_volume() * mat.get_density();
	}
	
	public void set_new_form(shape_form new_form) {
		form=new_form;
		//weight = form.basic_form.get_volume() * mat.get_density();
	}
	
	public void calc_weight() {
		weight = form.basic_form.get_volume() * mat.get_density();
	}
	
	public void set_material(material new_material) {
		mat=new_material;
		weight = form.basic_form.get_volume() * mat.get_density();
	}
	
	public shape_form get_form() {
		return form;
	}
	
	public material get_material() {
		return mat;
	}
	
	public double get_weight() {
		return weight;
	}
	
	public void recalculate() { // redo the temperature calculations
		weight = form.basic_form.get_volume() * mat.get_density();
		
	}
	public int get_temp() {
		return temp;
	}
	public void set_temp(int temp) {
		this.temp = temp;
	}
	
	public String toString() {
		return Integer.toString(temp);
	}
	
	
	public long get_ID() {
		return ID;
	}
	public char get_graphic() {
		return graphic;
	}
	public void set_graphic(char graphic) {
		this.graphic = graphic;
	}
	
	public void random() { // randomly configure this item. 
		form.random();
		mat.random();
		graphic=(char)(Math.random()*95 +32); // range for normal, non-whitespace ascii
		temp=(int)(Math.random()*10000+5000); 
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
			//System.out.println(data[i]);
			if(data[i].indexOf(':')>=0) {
				int z=data[i].indexOf(':');
				switch (data[i].substring(1,z)) {
				case "mat": mat= new material("materials.txt", Integer.parseInt(data[i].substring(z+1,data[i].length()-1))); 		break;
				case "form" : form= new shape_form("shape_form.txt",Integer.parseInt(data[i].substring(z+1,data[i].length()-1)) ); 	break;
				case "temp" : temp=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); break;
				case "graphic": graphic=(char)((data[i].substring(z+1,data[i].length()-1)).toCharArray())[0]; break;
				case "aggregate": if(! data[i].substring(z+1,data[i].length()-1).equals("false")) { //if not false, this is a aggregate
					String[] temp = data[i].substring(z+1,data[i].length()-1).split(",");
					int[] aggregate_list= new int[temp.length];
					for(int j=0; j< temp.length; j++) {
						aggregate_list[j]=Integer.parseInt(temp[j]);
					}
					for(int j=0; j< aggregate_list.length-1; j++) {
						item temp2=new item("items.txt",aggregate_list[j]);
						temp2.calc_weight();
						//System.out.println(temp2);
						//System.out.println(aggregate.length);
						aggregate_item(temp2);
						
					}
					
				}
				break;
				case "priority" :/*System.out.println("DEBUG"); System.out.println(data[i].substring(z+1,data[i].length()-1));*/ priority = data[i].substring(z+1,data[i].length()-1).equals("true");
					
					
				
				break;
				}
			}
		}
	}


	public boolean is_priority() {
		return priority;
	}
	
	public item copy() { // make a duplicate of this item. 
		item new_item= new item();
		new_item.set_form(form.copy());
		new_item.set_material(mat);
		new_item.set_temp(temp);
		new_item.set_graphic(graphic);
		return new_item;
		
	}


	public item[] get_aggregate() {
		return aggregate;
	}


	public void set_aggregate(item[] aggregate) {
		this.aggregate = aggregate;
	}
				
	
}
