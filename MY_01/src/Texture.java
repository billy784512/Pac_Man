
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	
	public static BufferedImage[][][] player;
	public static BufferedImage[] enemy;
	
	public BufferedImage spriteSheet;
	
	public Texture() {
		
		File file = new File("res/sprite/sprites.png");
		try {
			spriteSheet = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player = new BufferedImage[2][2][2];
		
		// Character 1
		player[0][0][0] = this.getSprite(0, 0);
		player[0][0][1] = this.getSprite(16, 0);
		player[0][1][0] = this.getSprite(0, 0);
		player[0][1][1] = this.getSprite(32, 0);
		
		// Character 2
		player[1][0][0] = this.getSprite(0, 32);
		player[1][0][1] = this.getSprite(16, 32);
		player[1][1][0] = this.getSprite(0, 32);
		player[1][1][1] = this.getSprite(32, 32);
		
		
		enemy = new BufferedImage[2];
		
		// Enemy 1
		enemy[0] = this.getSprite(0, 16);
		
		// Enemy 2
		enemy[1] = this.getSprite(0, 48);
		
	}
	
	
	public BufferedImage getSprite(int xx, int yy) {
		return spriteSheet.getSubimage(xx, yy, 16, 16);
	}
	
	
	
	
	
}
