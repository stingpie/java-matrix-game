
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
	
	
	
	
	
	public void draw_world(multi_plane display_plane) throws IOException  { // goes through each tile, and displays the associated graphic. 
		
		
		tile[][] final_plane = display_plane.get_final_layer();
		
		for(int x=0; x<display_plane.get_dim()[0]; x++) {
			for(int y=0; y<display_plane.get_dim()[1]; y++) {
				if(display_plane.get_final_layer()[x][y].visible) {
				//System.out.print(final_layer[x][y].get_stack()[final_layer[x][y].get_stack().length-1].get_graphic());
				tGraphics.enableModifiers(SGR.BOLD);
				char[] color = display_plane.get_final_layer()[x][y].get_stack()[0].get_material().get_RGB();
				//System.out.println(Integer.valueOf(color[0]));
				tGraphics.setForegroundColor(new TextColor.RGB(color[0],color[1],color[2]));
					
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
	
	public void clear_ui() {
		tGraphics.setForegroundColor(new TextColor.RGB(0,0,0));
		tGraphics.fillRectangle(new TerminalPosition(26,2), new TerminalSize(16,22) , '█');
	}
	
	
	public void update_terminal() throws IOException {
		screen.refresh();
		UI_line=0;
	}
	
	
	
	
	
	
}
