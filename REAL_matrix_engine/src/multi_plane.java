import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;
//import java.util.



public class multi_plane {
	// multi-plane takes in several "layers" of planes, and ultimately decides which tile to display.
	// also serves as the input method. (maybe it'll change later?)
	
	
	
	private plane[] plane_group= {};// an array that holds each plane. the last item is displayed as ontop. 
	private tile[][] final_layer; // a matrix of tiles that are to be displayed. 
	private int[] dim; // the size of the layer. 
	
	
	
	
//	Terminal terminal;
//	Screen screen; 
//
//	TextGraphics tGraphics;
	
	
	multi_plane() throws IOException {
//		terminal = new DefaultTerminalFactory().createTerminal();
//		screen = new TerminalScreen(terminal);
//
//		tGraphics = screen.newTextGraphics();
//		
//		screen.startScreen();
//		screen.clear();
	}
	
	multi_plane(plane layer) throws IOException{
		
//		terminal = new DefaultTerminalFactory().createTerminal();
//		screen = new TerminalScreen(terminal);
//
//		tGraphics = screen.newTextGraphics();
		
		plane_group = new plane[1];
		plane_group[0]=layer;
		dim=layer.get_dim();
		merge_planes();
		
//		screen.startScreen();
//		screen.clear();
	}
	
//	public KeyStroke input() throws IOException {
//		//System.out.println(screen.readInput());
//		return screen.readInput();
//	}
//	
//
//	public void draw_world() throws IOException  { // goes through each tile, and displays the associated graphic. 
//		
//		
//		
//	
//		for(int x=0; x<dim[0]; x++) {
//			for(int y=0; y<dim[1]; y++) {
//				if(final_layer[x][y].visible) {
//				//System.out.print(final_layer[x][y].get_stack()[final_layer[x][y].get_stack().length-1].get_graphic());
//				if(final_layer[x][y].get_Priority_graphic()!=0) {screen.setCharacter(x,y,TextCharacter.fromCharacter(final_layer[x][y].get_Priority_graphic())[0]); }
//				else {
//					screen.setCharacter(x,y,TextCharacter.fromCharacter((final_layer[x][y].get_stack()[final_layer[x][y].get_stack().length-1].get_graphic()))[0]);
//				}
//				}
//				//else //System.out.print(" ");
//			}
//			//System.out.println();
//		}
//		screen.refresh();
//		//System.out.println(screen.readInput().getKeyType());
//		//screen.stopScreen();
//		
//	}
	
	
	public void heat_iterate() {
		
		for(int i=0; i<plane_group.length; i++) {
			
			plane_group[i].iterate_temp();
			
			for(int x=0; x<plane_group[i].get_dim()[0]; x++) {
				for(int y=0; y<plane_group[i].get_dim()[1]; y++) {
					
					
					if(plane_group[(i+1)%plane_group.length].get_tile_at(x+plane_group[i].get_offset()[0], y+plane_group[i].get_offset()[1]) != null) {
						float split=(float)matrix_engine.material_list.get_material(plane_group[i].get_tile(x, y).get_stack()[plane_group[i].get_tile(x, y).get_stack().length-1].get_material()).get_tconduct();
						float temp_split_one= (plane_group[i].get_tile(x, y).get_stack()[plane_group[i].get_tile(x, y).get_stack().length-1].get_temp());
						float temp_split_two= (plane_group[(i+1)%plane_group.length].get_tile_at(x+plane_group[i].get_offset()[0], y+plane_group[i].get_offset()[1]).get_stack()[plane_group[(i+1)%plane_group.length].get_tile_at(x+plane_group[i].get_offset()[0], y+plane_group[i].get_offset()[1]).get_stack().length-1].get_temp());
						
						plane_group[i].get_tile(x, y).get_stack()[plane_group[i].get_tile(x, y).get_stack().length-1].set_temp((int)((temp_split_two-temp_split_one)*split + temp_split_one));
						plane_group[(i+1)%plane_group.length].get_tile_at(x+plane_group[i].get_offset()[0], y+plane_group[i].get_offset()[1]).get_stack()[plane_group[(i+1)%plane_group.length].get_tile_at(x+plane_group[i].get_offset()[0], y+plane_group[i].get_offset()[1]).get_stack().length-1].set_temp((int)((-temp_split_two+temp_split_one)*split + temp_split_two));
					}
				}
			}
		}
		
	}
	
	public void merge_planes() { // merges the planes together so they can be put into the final layer. 
		final_layer=new tile[dim[0]][dim[1]];
		for(int i=0; i<plane_group.length; i++) {
			for(int x=0; x<dim[0]; x++) {
				for(int y=0; y<dim[1]; y++) {
					if( plane_group[i].get_tile_at(x,y)!=null) { // if the tile exists
						
						tile z =plane_group[i].get_tile_at(x,y);
						final_layer[x][y]=z; // set the final layer to this tile
					}
					else {/* final_layer[x][y]=new tile(false);*/}
					
				}
			}
			
			for(int x=0; x<dim[0]; x++) {
				for(int y=0; y<dim[1]; y++) {
					if( final_layer[x][y]==null) { // if the tile DOES NOT exist
						
						final_layer[x][y]= new tile(false);
					}
					
				}
			}
		}
	}
	
	public void add_plane(plane new_plane) {
		plane_group = Arrays.copyOf(plane_group, plane_group.length+1);
		plane_group[plane_group.length-1]=new_plane;
		merge_planes();
		
	}
	
	public void remove_plane(int plane_num) {
		plane[] new_plane_group = Arrays.copyOf(plane_group, plane_group.length-1);
		for(int i=0; i<plane_num; i++) {
			new_plane_group[i]=plane_group[i];
		}
		
		for(int i=plane_num+1; i<plane_group.length-1; i++) {
			new_plane_group[i-1]=plane_group[i];
		}
		
		plane_group=new_plane_group;
		merge_planes();
	}
	
	public plane[] get_plane_group() {
		return plane_group;
	}
	
	public void set_plane_group( plane[] new_plane_group) {
		this.plane_group=new_plane_group;
		merge_planes();
	}

	public int[] get_dim() {
		return dim;
	}

	public void set_dim(int x, int y) {
		this.dim[0] = x;
		this.dim[1] = y;
		merge_planes();
	}

	public tile[][] get_final_layer() {
		return final_layer;
	}
	
	
	
	
	
	public void try_to_move(plane plane_to_move, int x, int y, int force) {
		int[] plane_pos = plane_to_move.get_offset();
		int[] move_pos = plane_pos;
		move_pos[0]+=x; move_pos[1] += y; 
		if(can_be_pushed(force, plane_pos[0],plane_pos[1],x,y)) {
			push_tile(plane_pos[0],plane_pos[1],x,y);
		}
		plane_to_move.set_offset(move_pos[0], move_pos[1]); // move the intended plane. 
				
		
	}
	
	public boolean can_be_pushed(int force, int tile_x, int tile_y, int rel_x, int rel_y) {
		boolean pushable=true;
		
		if(final_layer[tile_x+rel_x][tile_y+rel_y].get_max_push()==0) {
			return true;
		}
		
		
		if(final_layer[tile_x+rel_x][tile_y+rel_y].get_max_push()<=force) {// if the adjacent tile has a BIG object on it
			if( can_be_pushed(force-final_layer[tile_x+rel_x][tile_y+rel_y].get_max_push(), tile_x+rel_x, tile_y+rel_y,rel_x, rel_y)) {
				pushable=true;
				
			}else {
				pushable=false;
			}
		}
		return pushable;
		
		

		
	}
	
	
	public void push_tile(int tile_x, int tile_y, int x, int y ) {
		//item[] stack=final_layer[tile_x][tile_y].get_big_items(); // get the big items stack from the tile
//		if(final_layer[tile_x][tile_y].get_big_items()==null) return;
//		item[] temp= new item[final_layer[tile_x][tile_y].get_big_items().length];
//		
//		temp=final_layer[tile_x][tile_y].get_big_items();
//		System.out.println(temp.length);
//		
//		if(final_layer[tile_x][tile_y+x].get_big_items()!=null) {
//		
		
		//System.out.println("LINE 229");
		Stack<item> bulky_items = new Stack<item>();  
		Stack<Integer> bulky_items_loc = new Stack<Integer>();  
		//int bulky_items=0;
		for(int i=0; i<final_layer[tile_x][tile_y].get_stack().length; i++) {
			if(final_layer[tile_x][tile_y].get_stack()[i].get_form().get_basic_form().get_volume()>10000 &&  final_layer[tile_x][tile_y].get_stack()[i].get_push_size()>10000  ) {
				//System.out.println("size?????");
				bulky_items.push(final_layer[tile_x][tile_y].get_stack()[i]);
				bulky_items_loc.push(i);
			}
		}//System.out.println("LINE 239");
		if(bulky_items.size()==0) {
			return;
		}
		//item[] temp2=Arrays.copyOf(final_layer[tile_x][tile_y+x].get_stack(),final_layer[tile_x][tile_y+x].get_stack().length);
		//System.out.println("LINE 244");
		Stack<item> nonbulky_items = new Stack<item>();
		//System.out.println(bulky_items.size());
		//final_layer[tile_x][tile_y].set_stack(new item[final_layer[tile_x][tile_y].get_stack().length-bulky_items.size()]);
		//System.out.println("LINE 248");
		for(int i=0; i<final_layer[tile_x][tile_y].get_stack().length; i++) {
			//System.out.println("LINE 250");System.out.println(bulky_items_loc.peek()-1);
			if(i==bulky_items_loc.peek()-1) {
				//System.out.println("LINE 252");
				bulky_items_loc.pop();
				//System.out.println("LINE 254");
				//System.out.print(" peek weight:");System.out.println(bulky_items.peek().get_weight());
				final_layer[tile_x+x][tile_y+y].add_item(bulky_items.pop());
			
			}
			else {
				//System.out.println("LINE 260"); System.out.println(final_layer[tile_x][tile_y].get_stack()[i]);
				nonbulky_items.push(final_layer[tile_x][tile_y].get_stack()[i]);
			}
			//System.out.println(final_layer[tile_x][tile_y].get_stack()[0]);
			if(bulky_items.size()==0) {
				//System.out.println("LINE 265");
				//System.out.println(final_layer[tile_x][tile_y].get_stack()[1]);
				//System.out.println(final_layer[tile_x][tile_y].get_stack()[0]);
				//System.out.println(final_layer[tile_x+x][tile_y+y].get_stack()[2]);
				break;
			}
		}
		//System.out.println("LINE 272");
		final_layer[tile_x][tile_y].clear_stack();
		//System.out.println("LINE 274");
		for(int i=0; i<nonbulky_items.size();i++) {
			//System.out.println("LINE 276");
			final_layer[tile_x][tile_y].add_item(nonbulky_items.pop());
			//System.out.println("LINE 278");
		}
		//System.out.println("LINE 280");
		//System.out.println(final_layer[tile_x][tile_y].get_stack()[1]);
		//System.out.println(final_layer[tile_x][tile_y].get_stack()[0]);
		//System.out.println(final_layer[tile_x+x][tile_y+y].get_stack()[2]);
		
		
//			item[] temp2=Arrays.copyOf(     final_layer[tile_x][tile_y+x].get_big_items(), final_layer[tile_x][tile_y+x].get_big_items().length+final_layer[tile_x][tile_y].get_big_items().length);
//			
//			for(int i=0; i<temp.length; i++) {
//				temp2[i+final_layer[tile_x][tile_y+x].get_big_items().length]=temp[i];
//			}
			
//		}else {
//			final_layer[tile_x][tile_y+x].set_big_items(temp);
//			
//		}
		
		
//		int temp_len=final_layer[tile_x][tile_y].get_stack().length;
//		
//		for(int i=0; i<temp_len; i++) {
//			if(final_layer[tile_x][tile_y].get_stack()[i].get_push_size()>1) {
//				final_layer[tile_x][tile_y].pop(i);
//				
//			}
//		}
//		//final_layer[tile_x][tile_y].set_big_items(new item[0]);
//		final_layer[tile_x][tile_y].set_max_push(0);
				
				
		//System.out.println(final_layer[tile_x][tile_y+x].get_big_items().length);
		
		
		//final_layer[tile_x][tile_y].set_big_items(z); // empty the big items stack from the previous tile
		//final_layer[tile_x][tile_y].add_item(stack); // add the items to the next tile.
		
	}
	
	
}
