
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {
	
	private static final long serialVersionUID = 1L; // I guess it is used to check the version are the same. 待問
	private static final int WIDTH = 609, HEIGHT = 609;
	private static final String TITLE = "Pac-Man";
	private String mapPath, name;
	private boolean isRunning = false;
	private Thread thread; // The second thread besides the main thread.
	
	public static Player player;
	public static Level level;
	
	public static final int PAUSE_SCREEN = 0, GAME = 1, DEFEAT = 2, VICTORY = 3;
	public static int STATE = -1;
	
	public static Timer timer;
	public static JFrame frame;
	
	public static int character;
	
	public boolean isEnter = false;
	
	private int time = 0;
	private int targetFrames = 30;
	private boolean showText = true;
	
	
	public Game(int map, int character, String name) {
		this.character = character;
		this.name = name;
		Dimension dimension = new Dimension(Game.WIDTH, Game.HEIGHT);
		// Dimension: A object means a set of (x, y).
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		
		addKeyListener(this);
		
		new Texture();
		
		switch (map) {
		case 1:
			this.mapPath = "res/map/map.png";
			break;
		case 2:
			this.mapPath = "res/map/map2.png";
			break;
		default:
			this.mapPath = "res/map/map.png";
		}
		STATE = PAUSE_SCREEN;		
	}
	
	/* synchronized make the variables shared by more than one Thread only can be used 
	 * once a time, to prevent errors. 
	 * For example, if there're two methods have been set with synchronized, and two Threads
	 * run these two methods at the same time. Then, only the first run synchronized method
	 * can be run and use the shared variables, while the other synchronized method will wait,
	 * and can be used after the first one have done.
	 * By the way, if there are two synchronized variables and they are not a same variable, 
	 * they won't need to wait each other.*/
	public synchronized void start() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		thread = new Thread(this); // Let the thread will start() with the run() of Game.
		thread.start();
	}
	
	public synchronized void stop() {
		if (!isRunning) return;
		isRunning = false;
		try {
			thread.join(); // No matter what are you doing, do this thread and finish it first.
		} catch (InterruptedException e) {
			e.printStackTrace(); 
			/* InterruptedException:
			 * When some thread is interrupted, it will be thrown.
			 * 
			 * printStackTrace():
			 * Exceptions are stacked layer by layer, sometimes it make us find the root of the
			 * exception hardly.
			 * So we use this method to print the trace of these exceptions, so that we can
			 * find the exception and fix it easier, because we know the trace and the root.*/
		}
		
	}
	
	
	private void tick() {
		if (STATE == GAME) {
			player.tick();
			level.tick();
		} else if (STATE != GAME) {
			time++;
			if (time == targetFrames) {
				time = 0;
				if (showText) {
					showText = false;
				} else {
					showText = true;
				}
			}
			if (isEnter) {
				isEnter = false;
				player = new Player(Game.WIDTH/2, Game.HEIGHT/2);
				level = new Level(this.mapPath);
				timer = new Timer(name);
				timer.setLocation(frame.getX() + frame.getWidth(), frame.getY());
				timer.start();
				this.requestFocus();
				STATE = GAME;
			}
		}
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		} // I think I have to know what did he do here. 待研究
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		if (STATE == GAME) {
			player.render(g);
			level.render(g);
		} else if (STATE == PAUSE_SCREEN) {
			final int BOX_WIDTH = 500;
			final int BOX_HEIGHT = 200;
			int xx = Game.WIDTH/2 - BOX_WIDTH/2;
			int yy = Game.HEIGHT/2 - BOX_HEIGHT/2;
			g.setColor(new Color(0, 0, 175));
			g.fillRect(xx, yy, BOX_WIDTH, BOX_HEIGHT);
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			if (showText) g.drawString("Press 'Enter' to start the game.", xx+55	, yy+105);
		} else if (STATE == DEFEAT) {
			final int BOX_WIDTH = 500;
			final int BOX_HEIGHT = 200;
			int xx = Game.WIDTH/2 - BOX_WIDTH/2;
			int yy = Game.HEIGHT/2 - BOX_HEIGHT/2;
			g.setColor(new Color(0, 0, 175));
			g.fillRect(xx, yy, BOX_WIDTH, BOX_HEIGHT);
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			timer.stop();
			if (showText) {
				g.drawString("Game over !", xx+185, yy+90);
				g.drawString("Press 'Enter' to start a new game.", xx+45, yy+120);
			}
		} else if (STATE == VICTORY) {
			final int BOX_WIDTH = 500;
			final int BOX_HEIGHT = 200;
			int xx = Game.WIDTH/2 - BOX_WIDTH/2;
			int yy = Game.HEIGHT/2 - BOX_HEIGHT/2;
			g.setColor(new Color(0, 0, 175));
			g.fillRect(xx, yy, BOX_WIDTH, BOX_HEIGHT);
			
			g.setColor(Color.white);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
			timer.stop();
			if (showText) {
				g.drawString("Victory !", xx+200, yy+90);
				g.drawString("Press 'Enter' to start a new game.", xx+45, yy+120);
			}
		}
		
		g.dispose();
		bs.show();
		
	}
	
	public void run() {
		this.requestFocus();
		// int fps = 0;
		double timer = System.currentTimeMillis(); // Return current time in the unit of millisecond.
		long lastTime = System.nanoTime(); // Return system time.
		double targetTick = 60.0;
		double delta = 0;
		double ns = 1000000000 / targetTick;
		
		/* Every time when 1 second passed, the delta will become 60. Then, the program go into
		 * the inner while loop, and then repeatedly call tick() and render() methods, which
		 * means create a new graph and show it on the screen. And when every time the program
		 * do these things, the fps will ++. At the end, after a second passed, this program
		 * will print the fps, which counts how many time the program renew the picture of this
		 * game, on the console.*/
		
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while (delta >= 1) {
				tick();
				render();
				// fps++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				// don't need to print fps now.
				// System.out.println(fps);
				// fps = 0;
				timer += 1000;
			}
		}
		
		stop();
		
	}
	
	
	public static void theReallyStartingPoint(int map,int character, String name) {
		Game game = new Game(map, character, name);
		frame = new JFrame();
		frame.setTitle(Game.TITLE);
		frame.add(game);
		frame.setResizable(false);
		frame.pack(); // Set the size of the frame so that it can tightly pack all object in it.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Centering the frame when it show.
		frame.setVisible(true);

		game.start();
	}
	
	
	public void keyPressed(KeyEvent e) {
		
		if (STATE == GAME) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = true;
			if (e.getKeyCode() == KeyEvent.VK_LEFT) player.left = true;
			if (e.getKeyCode() == KeyEvent.VK_UP) player.up = true;
			if (e.getKeyCode() == KeyEvent.VK_DOWN) player.down = true;
		} else if (!isEnter) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) isEnter = true;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (STATE == GAME) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = false;
			if (e.getKeyCode() == KeyEvent.VK_LEFT) player.left = false;
			if (e.getKeyCode() == KeyEvent.VK_UP) player.up = false;
			if (e.getKeyCode() == KeyEvent.VK_DOWN) player.down = false;
		}
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	
}