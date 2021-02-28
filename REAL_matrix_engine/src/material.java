//import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class material {
	private int melting_temp=10200;
	private int boiling_temp=10500;
	private int elastic=10;
	private int plastic=10;
	private int density=10;
	private float pourosity=0; //how much liquid the material can absorb (0 - 1)
	private material[] composite= new material[1];  // what this meta-material is built out of. 
	
	
	
	
	public material() {
		//composite[0]= new material(false);
	}
	
	public material(String file, int index) throws Exception {
		String[] z=loader(file);
		String b = z[index];
		
		string_load(b.split("PUT_SPACE_HERE"));
	}
	
	
	public material(material new_material) {  // useful for copying characteristics
		melting_temp=new_material.get_melting_temp();
		boiling_temp=new_material.get_boiling_temp();
		elastic=new_material.get_elastic();
		plastic=new_material.get_plastic();
		density=new_material.get_density();
		pourosity=new_material.get_pourosity();
		composite=new_material.get_composite();
	}
	
	
	public material(int mt, int bt, int el, int pl, int dn, int pr) { // create a new material with parameters
		melting_temp=mt;
		boiling_temp=bt;
		elastic=el;
		plastic=pl;
		density=dn;
		pourosity=pr;
	}
	
	
	
	
	
	public void blend_material(material new_material) { // adds a new material to the composite
		
		//System.out.println(composite[0]);
		if(composite[0]==null) {
			composite[0]=new material(0,0,0,0,0,0);
			
		}
		composite = Arrays.copyOf(composite, composite.length+1);
		composite[composite.length-1]=new_material;
		
		melting_temp=0;
		boiling_temp=0;
		elastic=0;
		plastic=0;
		density=0;
		pourosity=0;
		for(int i=0; i<composite.length; i++) {  // set the properties of this meta-material to be the average of it's components
			melting_temp+=composite[i].get_melting_temp();
			boiling_temp+=composite[i].get_boiling_temp();
			elastic += composite[i].get_elastic();
			plastic += composite[i].get_plastic();
			density += composite[i].get_density();
			pourosity += composite[i].get_pourosity();
		}

			melting_temp/=composite.length-1;
			boiling_temp/=composite.length-1;
			elastic/=composite.length-1;
			plastic/=composite.length-1;
			density/=composite.length-1;
			pourosity/=composite.length-1;
		
	}
	
	
	
	public material[] get_composite() {
		return composite;
	}
	
	public void change_melting_temp(int new_temp) {
		melting_temp=new_temp;
	}
	public void change_boiling_temp(int new_temp) {
		boiling_temp=new_temp;
	}
	public int get_melting_temp() {
		return melting_temp;
	}
	public int get_boiling_temp() {
		return boiling_temp;
	}
	public int get_plastic() {
		return plastic;
	}
	public void change_plastic(int new_plastic) {
		this.plastic= new_plastic;
	}
	public int get_elastic() {
		return elastic;
	}
	public void change_elastic(int new_elastic) {
		this.elastic= new_elastic;
	}
	public int get_density() {
		return density;
	}
	public void change_density(int new_density) {
		this.density= new_density;
	}
	public float get_pourosity() {
		return pourosity;
	}
	public void change_pourosity(int new_pourosity) {
		this.pourosity = new_pourosity;
	}
	
	public void random() {
		melting_temp=(int)(Math.random()*2000 +9000);
		boiling_temp=melting_temp+(int)(Math.random()*2000);
		elastic=(int)(Math.random()*200);
		plastic=(int)(Math.random()*200);
		density=(int)(Math.random()*200);
		pourosity=(float)(Math.random());
		
		
		
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
	
	
	
	
	public void string_load(String[] data) throws Exception{// takes in correctly formated tags
		
		for(int i=0; i<data.length;i++) {
			data[i]=data[i].replaceAll("\\s", "");
			
			if(data[i].indexOf(':')>=0) {
				int z=data[i].indexOf(':');
				//System.out.println(data[i].substring(z+1,data[i].length()-1));
				switch (data[i].substring(1,z)) {
				case "melt_temp": melting_temp=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   melt");
				break;
				case "boil_temp": boiling_temp=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   boil");
				break;
				case "elastic": elastic=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   elastic");
				break;
				case "plastic": plastic=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   plastic");
				break;
				case "pourosity": pourosity=Float.parseFloat(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   pourousity");
				break;
				case "density": density=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   density");
				break;
				case "composite": if(! data[i].substring(z+1,data[i].length()-1).equals("false")) { //if not false, this is a composite
					String[] temp = data[i].substring(z+1,data[i].length()-1).split(",");
					int[] composite_list= new int[temp.length];
					for(int j=0; j< temp.length; j++) {
						composite_list[j]=Integer.parseInt(temp[j]);
					}
					for(int j=0; j< composite_list.length; j++) {
						material temp2=new material();
						temp2.load_from("materials.txt",composite_list[j]);
						//System.out.println(temp2);
						//System.out.println(composite.length);
						blend_material(temp2);
						
					}
					
				}
				break;
				}
			}
		}
		
		
		
		
		
	}
	
	
}
