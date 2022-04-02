import java.util.Arrays;

public class plane {
	// planes is a class I use to store information about a matrix of tiles. 
	// planes can also be transformed & offset from the global positions
	// to display a plane, you are gonna wanna use the world_display class. 
	
	
	
	private tile[][] world; //matrix
	private int[] dim= {0,0};	// size of matrix
	private int[] offset= {0,0}; 	// offset is used to 'slide' planes over other planes, 
								 	// so you could have mobile vehicles in the top plane,
									// and the ground in a plane below that. 
	
	public plane(int width, int height) { // create new plane with these dimensions
		tile[][] z = new tile[width][height]; 
		world=z;
		for(int x=0; x<width; x++ ) {
			for(int y=0; y<height; y++ ) {
				tile new_tile= new tile();
				world[x][y] = new_tile;
			}	
		}
		
		
		dim[0]= width;
		dim[1]= height;
	}
	
	public void set_offset(int x, int y) {
		offset[0]=x;
		offset[1]=y;
	
	}
	public int[] get_offset() {
		return this.offset;
	}
	
	public void set_tile(tile new_tile, int x,int y) {
		world[x][y]=new_tile;
	}
	
	public tile get_tile(int x,int y) {
		return world[x][y];
	}
	
	public tile get_tile_at(int x, int y) { //global position of tile
		if(x-offset[0]>=0 & x-offset[0]<dim[0] & y-offset[1]>=0 & y-offset[1]<dim[1]) {
		return world[x-offset[0]][y-offset[1]];
		}
		return null;
	}
	
	public boolean is_there_tile_at(int x, int y) { // test whether there is a tile at (x,y) global position
		
		return (x-offset[0]>=0 & x-offset[0]<dim[0] & y-offset[1]>=0 & y-offset[1]<dim[1]) ;
	}
	
	public void set_tile_at(tile new_tile, int x, int y) { //global position of tile
		if(x-offset[0]>=0 & x-offset[0]<dim[0] & y-offset[1]>=0 & y-offset[1]<dim[1]) {
			world[x+offset[0]][y+offset[1]]=new_tile;
		}
		//System.out.println("dude, error in plane, line 55");
	}
	
	public void resize_right(int x_rel) { // I don't really see this being useful, but I still programmed it. 
		
		tile[][] z= new tile[dim[0]+x_rel][dim[1]];
		for(int x=0; x<dim[0]+x_rel; x++) {
				for(int y=0; y<dim[1]; y++) {
					z[x][y]=world[x][y];
				}
			}
		world=z;
		
	}
	
	public void resize_down(int y_rel) { // I don't really see this being useful, but I still programmed it. 
		
		tile[][] z= new tile[dim[0]][dim[1]+y_rel];
		for(int x=0; x<dim[0]; x++) {
				for(int y=0; y<dim[1]+y_rel; y++) {
					z[x][y]=world[x][y];
				}
			}
		world=z;
		
	}
	
	public void flip_h() {
		tile[][] z= new tile[dim[0]][dim[1]];
		
		for(int x=0; x<dim[0]; x++) {
			for(int y=0; y<dim[1]; y++) {
				z[x][y]=world[dim[0]-x][y];
			}
		}
		
		world=z;
		
	}
	
	public void flip_v() {
		tile[][] z= new tile[dim[0]][dim[1]];
		
		for(int x=0; x<dim[0]; x++) {
			for(int y=0; y<dim[1]; y++) {
				z[x][y]=world[x][y-dim[1]];
			}
		}
		
		world=z;
		
	}
	
	public void translate(int xt, int yt) {  	// translate & loop the tiles on this plane
		tile[][] z= new tile[dim[0]][dim[1]];
		
		for(int x=0; x<dim[0]; x++) {
			for(int y=0; y<dim[1]; y++) {
				z[(x+xt)%dim[0]][(y+yt)%dim[1]]=world[x][y];
			}
		}
		
		world=z;
		
	}
	
	 
	public void iterate_temp() { 	// go through each tile, simulate temperature changes between items in the same tile,
									// and also calculate the temperature changes between different tiles. 
		
		int[][] temps= new int[dim[0]][dim[1]]; // temps a copy of the temperature of all tiles on the plane. 
		
		for(int x=0; x<dim[0]; x++) {
			for(int y=0; y<dim[1]; y++) {
				//System.out.print(x);System.out.print(" ");System.out.println(y);
				//if(world[x][y].get_stack().length>1) {
				//	System.out.println(world[x][y].get_stack()[1]);
				//}
				
				temps[x][y]= world[x][y].get_stack()[world[x][y].get_stack().length-1].get_temp(); // actually set up temps
			}
		}
		
		
		for(int x=0; x<dim[0]; x++) { // goes through each tile
			for(int y=0; y<dim[1]; y++) {
				world[x][y].temp_exchange(world[x][y].get_stack()); // exchange temperatures for items within the same tile
				
				
				
				int[] cooler_temp = new int[0];
				
				
				
				
				for(int x2=-1; x2<2; x2++) {
					for(int y2=-1; y2<2; y2++) { // for each tile, look at the surrounding tiles
						
						if(x+x2<dim[0] && x+x2>=0 && y+y2<dim[1] && y+y2>=0 && (x2!=0 || y2!=0)) {     // makes an array of high temperatures surrounding world[x][y]
							if(world[x+x2][y+y2].get_stack().length>=0) {
								// if the surrounding tile is cooler than the center tile, add them to the list
								// then, we will go over that list later, and individually change their temperatures. 
								
								if(temps[x][y]>world[x+x2][y+y2].get_stack()[world[x+x2][y+y2].get_stack().length-1].get_temp()) {
									cooler_temp= Arrays.copyOf(cooler_temp, cooler_temp.length+2);
									cooler_temp[cooler_temp.length-2]=x+x2;
									cooler_temp[cooler_temp.length-1]=y+y2;
									
								}
							}
						}
					}
				}
					
				
				
				
				
				
				float temp_exchange=(float)matrix_engine.material_list.get_material(world[x][y].get_stack()[world[x][y].get_stack().length-1].get_material()).get_tconduct();// the speed at which temperature travels between tiles
				float loss=0;
				for(int i=0; i<(int)(cooler_temp.length/2); i++) { // goes through the array, and distributes heat to each colder tile around
					tile cold_tile=world[cooler_temp[i*2]][cooler_temp[(i*2)+1]]; // made this variable just for convienience. 
					// temp_move is how many degrees should be shifted from the central tile to the surrounding tiles.
					float temp_move =(float)( ((world[x][y].get_stack()[world[x][y].get_stack().length-1].get_temp()-cold_tile.get_stack()[cold_tile.get_stack().length-1].get_temp())*temp_exchange)/(cooler_temp.length*0.5));
					
					// hopefully, this would never happen, but it's possible for several tiles to add a ton of temperature
					// to a single, cooler tile. 
					if( (temp_move+cold_tile.get_stack()[cold_tile.get_stack().length-1].get_temp()) < temps[x][y] ){
					
					loss+=temp_move; // loss is how much temperature should be taken away from the central tile. 
					temps[cooler_temp[i*2]][cooler_temp[(i*2)+1]]=((int)(temps[cooler_temp[i*2]][cooler_temp[(i*2)+1]]+temp_move)); // just changes the temperature. 
					}
				}
				temps[x][y]=((int)(temps[x][y]-loss)); // lowers the temperature of the central tile in accordance with how much heat was distributed to other tiles. 
				
				
			}
		}
		
		for(int x=0; x<dim[0]; x++) { // sets the temperature of the plane to the newly calculated temperatures. 
			for(int y=0; y<dim[1]; y++) {
				world[x][y].get_stack()[world[x][y].get_stack().length-1].set_temp(temps[x][y]);
				
			}
		}
		
	}
	
	public void random(int x, int y) { // fills the plane with random data
		
		
		dim[0]=x;
		dim[1]=y;
		
		world = new tile[x][y];
		for(int x2=0; x2<dim[0];x2++) {
			for(int y2=0; y2<dim[0];y2++) {
				world[x2][y2]=new tile();
				world[x2][y2].random(5);
				
			}
		}
		
	}
	
	public int[] get_dim() {
		return this.dim;
	}
	
	public void fill(tile new_tile) { // fills the plane with a copy of the input tile. 
		for(int x=0; x<dim[0]; x++ ) {
			for(int y=0; y<dim[1]; y++ ) {
				world[x][y] = new_tile.copy();
			}	
		}
	}
	
	public int get_avg_temp() { // calculates the average temperature of the plane. not too useful.
		int running_total=0;
		for(int x=0; x<dim[0]; x++ ) {
			for(int y=0; y<dim[1]; y++ ) {
				running_total+=world[x][y].get_stack()[world[x][y].get_stack().length-1].get_temp();
			}	
		}
		return running_total/(dim[0]*dim[1]);
	}
	
	
	
	
	
	
	
	
}
