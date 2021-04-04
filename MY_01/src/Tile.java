
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

public class Tile extends Rectangle {
	
	private static final long serialVersionUID = 1L;
	
	public Tile(int x, int y) {
		setBounds(x, y, 32, 32);
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 175));
		g.fillRect(x, y, width, height);
	}
	
	
	
	
}
