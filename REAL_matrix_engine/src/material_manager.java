import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*; 


public class material_manager {
	Dictionary<Integer,material> dict = new Hashtable<Integer, material>();  
	
	material_manager(String name)throws Exception{


		String[] bruh=loader(name);
		for(int F=0; F<11; F++) {
			String b = bruh[F];
			
			String[] data=b.split("PUT_SPACE_HERE");
			
			material new_mat= new material();
			
			for(int i=0; i<data.length;i++) {
				data[i]=data[i].replaceAll("\\s", "");
				
				
				
				
				if(data[i].indexOf(':')>=0) {
					int z=data[i].indexOf(':');
					//System.out.println(data[i].substring(z+1,data[i].length()-1));
					switch (data[i].substring(1,z)) {
					case "melt_temp": new_mat.melting_temp=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("melting: "+ Integer.toString(new_mat.melting_temp));
					break;
					case "boil_temp": new_mat.boiling_temp=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   boil");
					break;
					case "elastic": new_mat.elastic=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   elastic");
					break;
					case "plastic": new_mat.plastic=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   plastic");
					break;
					case "pourosity": new_mat.pourosity=Float.parseFloat(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   pourousity");
					break;
					case "density": new_mat.density=Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   density");
					break;
					case "tconduct": new_mat.tconduct=Float.parseFloat(data[i].substring(z+1,data[i].length()-1)); //System.out.println("   density");
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
							new_mat.blend_material(temp2);
							
						}
						
					}
					break;
					case "R":new_mat.RGB[0] = (char)Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); break;
					case "G":new_mat.RGB[1] = (char)Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); break;
					case "B":new_mat.RGB[2] = (char)Integer.parseInt(data[i].substring(z+1,data[i].length()-1)); break;
					
					
					}
					
				}
			//System.out.println(new_mat.elastic);
			
			//System.out.println(dict.get(dict.size()-1).boiling_temp);
			}
		dict.put(dict.size(),new_mat);
		}
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

	public int add_material(material new_material) {
		dict.put(dict.size(), new_material);
		return dict.size()-1;
		
	}
	
	public int add_material() {
		dict.put(dict.size(), new material());
		return dict.size()-1;
	}
	
	public material get_material(int key) {
		//System.out.println(dict.toString());
		return dict.get(key);
	}
	
	public void copy_material(int original_key, int duplicate_key) {
		dict.put(duplicate_key ,dict.get(original_key));
	}
	
	public void remove_material(int key) { // doing this is kinda dangerous, unless you know what you're doing. 
		dict.remove(key);
	}
	
	public void set_material(int key, material new_material) {
		dict.remove(key);
		dict.put(key, new_material);
	}
	
	

}
