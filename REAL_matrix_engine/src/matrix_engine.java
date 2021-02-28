

public class matrix_engine {
	
	public static void main(String[] args) throws Exception{
		
		plane player_plane = new plane(1,1);
		player_plane.get_tile(0,0).add_item(new item("items.txt", 3));
		
		
		System.out.println(player_plane.get_tile(0,0).get_stack()[0].get_form().get_basic_form().get_volume());
		for(int i=0; i<4; i++) {
			System.out.println(player_plane.get_tile(0,0).get_stack()[0].get_aggregate()[i].get_form().get_basic_form().get_volume());
		}
		
		
		
		tile ice_floor_1= new tile();
		ice_floor_1.add_item(new item("items.txt",2));
		ice_floor_1.get_stack()[0].set_material(new material("materials.txt",3));
		ice_floor_1.get_stack()[0].set_temp(10000);
		plane bottom_plane = new plane(25,25);
		
		bottom_plane.fill(ice_floor_1);
		
		tile ice_floor_2= new tile();
		ice_floor_2.add_item(new item("items.txt",2));
		
		ice_floor_2.get_stack()[0].set_material(new material("materials.txt",3));
		bottom_plane.set_tile(ice_floor_2, 12, 12);
		
		ice_floor_2.get_stack()[0].set_temp(18000);
		
		world_display final_pic = new world_display(bottom_plane);
		
		final_pic.add_plane(player_plane);
		
		String action;
		final_pic.merge_planes();
		final_pic.get_display_order()[0].iterate_temp();
		
		final_pic.draw_world();
		for(int i=0; i<65; i++) {
			//System.out.println();System.out.println();
			
			action =final_pic.input();
			if(action=="ArrowDown") {player_plane.set_offset(player_plane.get_offset()[0],player_plane.get_offset()[1]+1); }
			if(action=="ArrowUp") {player_plane.set_offset(player_plane.get_offset()[0],player_plane.get_offset()[1]-1); }
			if(action=="ArrowLeft") {player_plane.set_offset(player_plane.get_offset()[0]-1,player_plane.get_offset()[1]); }
			if(action=="ArrowRight") {player_plane.set_offset(player_plane.get_offset()[0]+1,player_plane.get_offset()[1]); }
			
			final_pic.merge_planes();
			final_pic.get_display_order()[0].iterate_temp();
			
			final_pic.draw_world();
			
		}
			
	}
	
}
