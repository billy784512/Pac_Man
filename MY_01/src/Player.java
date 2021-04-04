
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends Rectangle {
	
	private static final long serialVersionUID = 1L;
	
	public boolean right, left, up, down;
	private int speed = 4;
	
	private int time = 0, targetTime = 10;
	public int imageIndexToken = 1;
	
	private int lastXOrY = 0;
	private int lastDir = 1;
	
	public Player(int x, int y) {
		setBounds(x, y, 32, 32);
	}
	
	public void tick() {
		if (right && canMove(x + speed, y)) {
			x += speed;
			lastXOrY = 0;
			lastDir = 1;
		}
		if (left && canMove(x - speed, y)) {
			x -= speed;
			lastXOrY = 0;
			lastDir = -1;
		}
		if (up && canMove(x, y - speed)) {
			y -= speed;
			lastXOrY = 1;
			lastDir = 1;
		}
		if (down && canMove(x, y + speed)) {
			y += speed;
			lastXOrY = 1;
			lastDir = -1;
		}
		
		Level level = Game.level;
		for (int i = 0; i < level.apples.size(); i++) {
			if (this.intersects(level.apples.get(i))) {
				level.apples.remove(i);
				break;
			}
		}
		
		for (int i = 0; i < Game.level.enemies.size(); i++) {
			Enemy en = Game.level.enemies.get(i);
			if (en.intersects(this)) {
				// Menu system
				Game.STATE = Game.DEFEAT;
				return;
			}
		}
		
		if (level.apples.size() == 0) {
			// Game over and the player win.
			Game.STATE = Game.VICTORY;
			return;
		}
		
		time++;
		if (lastXOrY == 0) {
			if (time == targetTime) {
				time = 0;
				imageIndexToken ++;
			}
		} else if (lastXOrY == 1) {
			if (time == targetTime) {
				time = 0;
				imageIndexToken ++;
			}
		}
		
		
	}
	
	private boolean canMove(int nextX, int nextY) {
		Rectangle bounds = new Rectangle(nextX, nextY, width, height);
		Level level = Game.level;
		
		for (int xx = 0; xx < level.tiles.length; xx++) {
			for (int yy = 0; yy < level.tiles[0].length; yy++) {
				if (level.tiles[xx][yy] != null) {
					if (bounds.intersects(level.tiles[xx][yy])) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	
	public void render(Graphics g) {
		if (lastXOrY == 0) {
			if (lastDir == 1)
				g.drawImage(Texture.player[Game.character-1][lastXOrY][imageIndexToken%2], x, y, width, height, null);
			else if (lastDir == -1)
				g.drawImage(Texture.player[Game.character-1][lastXOrY][imageIndexToken%2], x+width, y, -width, height, null);
		} else if (lastXOrY == 1) {
			if (lastDir == 1)
				g.drawImage(Texture.player[Game.character-1][lastXOrY][imageIndexToken%2], x, y, width, height, null);
			else if (lastDir == -1)
				g.drawImage(Texture.player[Game.character-1][lastXOrY][imageIndexToken%2], x, y+height, width, -height, null);
		}
	}
}