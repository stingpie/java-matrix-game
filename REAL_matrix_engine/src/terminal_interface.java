
import java.io.IOException;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;


public class terminal_interface {

	Terminal terminal;
	Screen screen; 

	TextGraphics tGraphics;
	
	byte UI_line=0;
	
	
	public terminal_interface() throws IOException {
		
		DefaultTerminalFactory z= new DefaultTerminalFactory();
		
		
		terminal = z.createTerminal();
		terminal.setCursorVisible(false);
		System.out.println(terminal.getTerminalSize());
		terminal.enableSGR(SGR.BOLD);
		
		screen = new TerminalScreen(terminal);

		tGraphics = screen.newTextGraphics();
		
		System.out.println(screen.getTerminalSize());
		
		screen.startScreen();
		screen.clear();
	}

	
	
	public KeyStroke input() throws IOException {
		//System.out.println(screen.readInput());
		return screen.readInput();
	}
	
	
	
	
	
	public void draw_world(multi_plane display_plane, boolean temp_display) throws IOException  { // goes through each tile, and displays the associated graphic. 
		
		
		tile[][] final_plane = display_plane.get_final_layer();
		
		for(int x=0; x<display_plane.get_dim()[0]; x++) {
			for(int y=0; y<display_plane.get_dim()[1]; y++) {
				if(display_plane.get_final_layer()[x][y].visible) {
				
				tGraphics.enableModifiers(SGR.BOLD);
				char[] color = matrix_engine.material_list.get_material(display_plane.get_final_layer()[x][y].get_stack()[0].get_material()).get_RGB();
				
				if(temp_display) {
					int temp= display_plane.get_final_layer()[x][y].get_stack()[0].get_temp();
					float portion= (float) (1/(1+Math.pow(Math.E,-0.01*(temp-11000))));
					
					
					
					char color_red=(char) Math.max(Math.min((color[0]*(1-portion)) +((Math.pow((temp-9900)/50,-0.12))*329    )*portion,255),0);
					char color_blue=(char) Math.max(Math.min((color[2]*(1-portion)) +((Math.log((temp-9900)/50)*99 -161    )*portion),255),0);
					char color_green=(char) Math.max(Math.min((color[2]*(1-portion)) +((Math.log((temp-9900)/50)*138 -300     )*portion),255),0);
					
					
					tGraphics.setForegroundColor(new TextColor.RGB(color_red,color_blue,color_green));
				}
				else {
					tGraphics.setForegroundColor(new TextColor.RGB(color[0],color[1],color[2]));
				}
				
				
				
				
				
				
				
					
				if(display_plane.get_final_layer()[x][y].get_Priority_graphic()!=0) {tGraphics.putString(x,y,String.valueOf(display_plane.get_final_layer()[x][y].get_Priority_graphic())); }
				else {
					tGraphics.putString(x,y,String.valueOf(   (final_plane[x][y].get_stack()[final_plane[x][y].get_stack().length-1].get_graphic())));
				}
				}
				//else //System.out.print(" ");
			}
			//System.out.println();
		}
		
		//System.out.println(screen.readInput().getKeyType());
		//screen.stopScreen();
		
	}
	
	public void draw_ui(String label, Object data)  {
		tGraphics.putString(26, 2+UI_line, label + data.toString());
		UI_line++;
	}
	
	public void clear_ui(){
		tGraphics.setForegroundColor(new TextColor.RGB(0,0,0));
		tGraphics.fillRectangle(new TerminalPosition(26,2), new TerminalSize(16,22) , '█');
	}
	
	
	public void update_terminal() throws IOException {
		screen.refresh();
		UI_line=0;
	}
	
	
	
	
	
	
}
