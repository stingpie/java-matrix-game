
public class megamulti_plane {
	
	byte[][] xy= {{0,1},{1,0},{0,-1},{-1,0}};
	
	int[] world_size= new int[2];
	multi_plane[][] world; //available for 3d-ication
	boolean[][] active_planes;
	
	void heat_iterate_radius(int[] loc, char rad) {
		for(int x=0; x<world_size[0];x++) {
			for(int y=0; y<world_size[0];y++) {
				if(active_planes[x][y] && Math.sqrt((x-loc[0])*(x-loc[0])+(y-loc[1])*(y-loc[1]))<=rad) { // if the player's been here, and it's within loading radius...
					// exchange temps within each multiplane...
					world[x][y].heat_iterate();
					// test if this plane is adjacent to another plane with the right conditions
					for(int i=0; i<4; i++) {
						if(active_planes[x+xy[i][0]][y+xy[i][1]] && Math.sqrt((x+xy[i][0]-loc[0])*(x+xy[i][0]-loc[0])+(y+xy[i][1]-loc[1])*(y+xy[i][1]-loc[1]))<=rad) { // if the player's been here, and it's within loading radius...
							//BORDER TEMPPPPPP
							
						}
					}
				}
			}
		}
	}
	
	void border_temp(multi_plane from, multi_plane to, byte dir /* North, east, south, west*/) {
		for(int i=0; i<from.get_dim()[dir%2];i++) { // allows for rectangular chunks
			tile from_tile; tile to_tile;
			switch(dir) {
			case 0: // north
				from_tile=from.get_final_layer()[i][0];
				to_tile=to.get_final_layer()[i][to.get_dim()[0]-1];
			break;
			case 1: // east
				from_tile=from.get_final_layer()[0][i];
				to_tile=to.get_final_layer()[to.get_dim()[1]-1][i];
			break;
			case 2: // south
				from_tile=from.get_final_layer()[i][to.get_dim()[0]-1];
				to_tile=to.get_final_layer()[i][0];
			break;
			case 3: // west
				from_tile=from.get_final_layer()[i][0];
				to_tile=to.get_final_layer()[i][to.get_dim()[1]-1];
			break;
			default: // just destroy everything. 
				from_tile=null; 
				to_tile=null;
			
			}
			float t_conduct1=(float)matrix_engine.material_list.get_material(from_tile.get_hot_item().get_material()).get_tconduct();// the speed at which temperature travels between tiles
			float t_conduct2=(float)matrix_engine.material_list.get_material(to_tile.get_hot_item().get_material()).get_tconduct();
			
			float t_conduct=(t_conduct1+t_conduct2)/2;
			
			if(Math.abs(to_tile.get_hot_item().get_temp()-from_tile.get_hot_item().get_temp())>2) {
				int temp_difference=to_tile.get_hot_item().get_temp()-from_tile.get_hot_item().get_temp();
				int temp_exchange=(int) (temp_difference*t_conduct);
				to_tile.get_hot_item().set_temp(to_tile.get_hot_item().get_temp()-temp_exchange);
				from_tile.get_hot_item().set_temp(from_tile.get_hot_item().get_temp()+temp_exchange);

			}
			
			switch(dir) {
			case 0: // north
				from.get_final_layer()[i][0]=from_tile;
				to.get_final_layer()[i][to.get_dim()[0]-1]=to_tile;
			break;
			case 1: // east
				from.get_final_layer()[0][i]=from_tile;
				to.get_final_layer()[to.get_dim()[1]-1][i]=to_tile;
			break;
			case 2: // south
				from.get_final_layer()[i][to.get_dim()[0]-1]=from_tile;
				to.get_final_layer()[i][0]=to_tile;
			break;
			case 3: // west
				from.get_final_layer()[i][0]=from_tile;
				to.get_final_layer()[i][to.get_dim()[1]-1]=to_tile;
			break;
			default: // just destroy everything. 
				from_tile=null; 
				to_tile=null;
			
			}
			
			
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
