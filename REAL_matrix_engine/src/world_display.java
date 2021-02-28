import java.io.IOException;
import java.util.Arrays;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class world_display {
	// world display takes in several "layers" of planes, and ultimately decides which tile to display.
	// I might rename this class, because I would like inter-planar heat transmission, and this class
	// is basically a wrapper class for planes. 
	
	
	
	private plane[] display_order= {};// an array that holds each plane. the last item is displayed as ontop. 
	private tile[][] final_layer; // a matrix of tiles that are to be displayed. 
	private int[] dim; // the size of the layer. 
	
	
	
	
	Terminal terminal;
	Screen screen; 

	TextGraphics tGraphics;
	
	
	world_display() throws IOException {
		terminal = new DefaultTerminalFactory().createTerminal();
		screen = new TerminalScreen(terminal);

		tGraphics = screen.newTextGraphics();
		
		screen.startScreen();
		screen.clear();
	}
	
	world_display(plane layer) throws IOException{
		
		terminal = new DefaultTerminalFactory().createTerminal();
		screen = new TerminalScreen(terminal);

		tGraphics = screen.newTextGraphics();
		
		display_order = new plane[1];
		display_order[0]=layer;
		dim=layer.get_dim();
		merge_planes();
		
		screen.startScreen();
		screen.clear();
	}
	
	public String input() throws IOException {
		return screen.readInput().getKeyType().toString();
	}
	

	public void draw_world() throws IOException  { // goes through each tile, and displays the associated graphic. 
		
		
		
	
		for(int x=0; x<dim[0]; x++) {
			for(int y=0; y<dim[1]; y++) {
				if(final_layer[x][y].visible) {
				//System.out.print(final_layer[x][y].get_stack()[final_layer[x][y].get_stack().length-1].get_graphic());
				if(final_layer[x][y].get_Priority_graphic()!=0) {screen.setCharacter(x,y,TextCharacter.fromCharacter(final_layer[x][y].get_Priority_graphic())[0]); }
				else {
					screen.setCharacter(x,y,TextCharacter.fromCharacter((final_layer[x][y].get_stack()[final_layer[x][y].get_stack().length-1].get_graphic()))[0]);
				}
				}
				//else //System.out.print(" ");
			}
			//System.out.println();
		}
		screen.refresh();
		//System.out.println(screen.readInput().getKeyType());
		//screen.stopScreen();
		
	}
	
	
	
	
	
	 
	public void merge_planes() { // merges the planes together so they can be put into the final layer. 
		final_layer=new tile[dim[0]][dim[1]];
		for(int i=0; i<display_order.length; i++) {
			for(int x=0; x<dim[0]; x++) {
				for(int y=0; y<dim[1]; y++) {
					if( display_order[i].get_tile_at(x,y)!=null) { // if the tile exists
						
						tile z =display_order[i].get_tile_at(x,y);
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
		display_order = Arrays.copyOf(display_order, display_order.length+1);
		display_order[display_order.length-1]=new_plane;
		merge_planes();
		
	}
	
	public void remove_plane(int plane_num) {
		plane[] new_display_order = Arrays.copyOf(display_order, display_order.length-1);
		for(int i=0; i<plane_num; i++) {
			new_display_order[i]=display_order[i];
		}
		
		for(int i=plane_num+1; i<display_order.length-1; i++) {
			new_display_order[i-1]=display_order[i];
		}
		
		display_order=new_display_order;
		merge_planes();
	}
	
	public plane[] get_display_order() {
		return display_order;
	}
	
	public void set_display_order(plane[] new_display_order) {
		this.display_order=new_display_order;
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
}
