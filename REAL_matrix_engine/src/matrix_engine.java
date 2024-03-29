import com.googlecode.lanterna.input.KeyStroke;



public class matrix_engine{
	static material_manager material_list;
		
	
	public static void main(String[] args) throws Exception{
		
		material_list= new material_manager("materials.txt");
		
		plane player_plane = new plane(1,1);
		player_plane.get_tile(0,0).add_item(new item("items.txt", 3));
		
		
		
		
		
		
		tile ice_floor_1= new tile();
		ice_floor_1.add_item(new item("items.txt",2));
		ice_floor_1.get_stack()[0].set_material(3);
		ice_floor_1.get_stack()[0].set_temp(10000);
		plane bottom_plane = new plane(25,25);
		
		bottom_plane.fill(ice_floor_1);
		
		tile ice_floor_2= new tile();
		ice_floor_2.add_item(new item("items.txt",2));
		
		ice_floor_2.get_stack()[0].set_material(3);
		bottom_plane.set_tile(ice_floor_2, 12, 12);
		ice_floor_2.get_stack()[0].calc_weight();
		//System.out.println(ice_floor_2.get_stack()[0].get_weight());
		
		
		multi_plane final_pic = new multi_plane(bottom_plane);
		
		final_pic.add_plane(player_plane);
		
		KeyStroke action;
		final_pic.merge_planes();
		final_pic.get_plane_group()[0].iterate_temp();
		
		terminal_interface display = new terminal_interface();
		//final_pic.draw_world();
		
		display.draw_world(final_pic, true);
		display.draw_ui("Heat: " , player_plane.get_avg_temp());
		display.update_terminal();
		
		
		
		item pike = new item("items.txt", 0);
		
		item wall = new item("items.txt", 8);
		item[] wall_stack = new item[1];
		wall_stack[0]=wall;
		
		bottom_plane.get_tile(12, 12).add_item(wall);
		
		for(int i=0; i<500; i++) {
			//System.out.println();System.out.println();

			
			action =display.input();
			if(action.getKeyType().toString()=="EOF") {break;}
			
			
			if(action.getKeyType().toString()=="ArrowDown" || (action.getKeyType().toString()=="Character" && action.getCharacter()=='2')) {final_pic.try_to_move(player_plane, 0, 1,100000); }
			if(action.getKeyType().toString()=="ArrowUp" || (action.getKeyType().toString()=="Character" && action.getCharacter()=='8')) {final_pic.try_to_move(player_plane, 0, -1,100000); }
			if(action.getKeyType().toString()=="ArrowLeft" || (action.getKeyType().toString()=="Character" && action.getCharacter()=='4')) {final_pic.try_to_move(player_plane, -1, 0,100000); }
			if(action.getKeyType().toString()=="ArrowRight" || (action.getKeyType().toString()=="Character" && action.getCharacter()=='6')) {final_pic.try_to_move(player_plane, 1, 0,100000); }
			
			if(action.getKeyType().toString()=="Character" && action.getCharacter()=='7') {final_pic.try_to_move(player_plane, -1, -1,100000); }
			if(action.getKeyType().toString()=="Character" && action.getCharacter()=='9') {final_pic.try_to_move(player_plane, 1, -1,100000); }
			if(action.getKeyType().toString()=="Character" && action.getCharacter()=='3') {final_pic.try_to_move(player_plane, 1, 1,100000); }
			if(action.getKeyType().toString()=="Character" && action.getCharacter()=='1') {final_pic.try_to_move(player_plane, -1, 1,100000); }
			
			
			
			final_pic.merge_planes();
			final_pic.heat_iterate();
			
			//player_plane.get_tile(0, 0).get_stack()[0].set_temp(10700);
			
			
			//final_pic.draw_world();
			if( (i % 6) == 5 ) {
				player_plane.get_tile(0,0).get_stack()[0].apply_damage((pike).deliberate_attack_dmg(10000));
				//System.out.println(ice_floor_2.get_stack()[0].deliberate_attack_dmg(10000));
			}
			display.clear_ui();
			
			
			player_plane.get_tile(0,0).get_stack()[0].set_temp(10098);
			
			
			display.draw_world(final_pic, true);
			display.draw_ui("Heat: " , player_plane.get_avg_temp());
			display.draw_ui("Whole damage: " , player_plane.get_tile(0,0).get_stack()[0].get_damage());
			display.draw_ui("Head: " , player_plane.get_tile(0,0).get_stack()[0].get_aggregate()[5].get_damage());
			display.draw_ui("left leg: " , player_plane.get_tile(0,0).get_stack()[0].get_aggregate()[3].get_damage());
			display.draw_ui("right leg: " , player_plane.get_tile(0,0).get_stack()[0].get_aggregate()[2].get_damage());
			display.draw_ui("left arm: " , player_plane.get_tile(0,0).get_stack()[0].get_aggregate()[1].get_damage());
			display.draw_ui("right arm: " , player_plane.get_tile(0,0).get_stack()[0].get_aggregate()[0].get_damage());
			display.draw_ui("torso: " , player_plane.get_tile(0,0).get_stack()[0].get_aggregate()[4].get_damage());
			
			
			if( (i % 6) == 5 ) {
				display.draw_ui("Ouch!", "");
			}
			
			if((i % 20) == 19 ) {
				material_list.get_material(pike.get_material()).change_density(i*5+100);
				System.out.println(pike.get_weight());
				pike.calc_weight();
			}
			
			
			
			display.update_terminal();
			
		}
			
	}
	
}
