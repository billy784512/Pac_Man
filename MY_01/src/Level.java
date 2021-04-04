
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Level {

	public int width, height;

	public Tile[][] tiles;
	//public List<Tile> tiles; This is I tried to use ArrayList to replace the array.
	public List<Apple> apples;
	public List<Enemy> enemies;

	public Level(String path) {
		//this.tiles = new ArrayList<Tile>();
		this.apples = new ArrayList<>();
		this.enemies = new ArrayList<>();
		
		
		try {
			File mapFile = new File(path);
			BufferedImage map = ImageIO.read(mapFile); // ImageIO.read(getClass().getResource("/map/map.png"));
			this.width = map.getWidth();
			this.height = map.getHeight();
			
			this.tiles = new Tile[width][height]; // Line 22
			
			int[] pixels = new int[this.width * this.height];
			map.getRGB(0, 0, this.width, this.height, pixels, 0, this.width);
			
			
			
			for (int xx = 0; xx < width; xx++) {
				for (int yy = 0; yy < height; yy++) {
					int val = pixels[xx + (yy * this.width)];

					if (val == 0xFF000000) {
						// Tile
						tiles[xx][yy] = new Tile(xx * 32, yy * 32);
						//tiles.add(new Tile(xx*32, yy*32));
					} else if (val == 0xFF0026FF) {
						// Player
						Game.player.x = xx * 32;
						Game.player.y = yy * 32;
					} else if (val == 0xFFFF0000) {
						// Enemy
						enemies.add(new Enemy(xx * 32, yy * 32));
					} else {
						apples.add(new Apple(xx * 32, yy * 32));
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).tick();
		}
	}
	
	
	
	
	
	public void render(Graphics g) {
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				if (tiles[x][y] != null) tiles[x][y].render(g);
			}
		}
		/*for (int i = 0; i < tiles.size(); i++) {
			tiles.get(i).render(g);
		}*/
		
		
		
		for (int i = 0; i < apples.size(); i++) {
			apples.get(i).render(g);
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).render(g);
		}
		
		
	}

	
}
