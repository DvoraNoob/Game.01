package com.dvoragames.main;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.dvoragame.world.Minimap;
import com.dvoragame.world.World;
import com.dvoragames.entities.Enemy;
import com.dvoragames.entities.Entity;
import com.dvoragames.entities.Player;
import com.dvoragames.entities.Shoot;
import com.dvoragames.graphics.Spritesheet;
import com.dvoragames.graphics.UI;


public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	public static int curLevel = 1;
	public int max_Level = 2;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Shoot> shoot;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static UI ui;
	
	public static Minimap minimap;
		
	public static Menu menu;
	public static Pause pause;
	public static Help help;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public static String curMenu = "MENU";

	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		//Inicializando Objs
		
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);

		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		shoot = new ArrayList<Shoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		ui = new UI();
		player = new Player(0,0,48,48,spritesheet.getSprite(80, 80, 48, 48));
		entities.add(player);
		world = new World("/level1.png");

		
		minimap = new Minimap();
		
		menu = new Menu();
		pause = new Pause();
		help = new Help();
		
	}
	
	public void initFrame() {
		frame = new JFrame("Criaçao de Janela");
		frame.add(this);
		frame.setResizable(false);//destiva o redimencionamento pelo user
		frame.pack();
		Image imagem = null;
		try {
			imagem = ImageIO.read(getClass().getResource("/icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(getClass().getResource("/icon.png"));
		Cursor c = toolkit.createCustomCursor(image, new Point(0,0), "img");
		frame.setCursor(c);
		frame.setIconImage(imagem);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);//desativa o posicionamento inicial da janela central
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//desliga a produçao de graficos ao fechar a janela
		frame.setVisible(true);//ao iniciar a janela abre
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {

		if(gameState == "NORMAL") {
			restartGame = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			for(int i = 0; i < shoot.size(); i++) {
				shoot.get(i).tick();
			}
			
			if (enemies.size() == 0) {
				curLevel++;
				if(curLevel > max_Level) {
					curLevel = 1;
				}
				String newWorld = "level"+curLevel+".png";
				World.restartGame(newWorld);
			}
		}else if(gameState == "GAMEOVER") {
			framesGameOver++;
			if(framesGameOver == 30) {
				framesGameOver = 0;
				if(showMessageGameOver) {
					showMessageGameOver = false;
				}else {
					showMessageGameOver = true;
				}
			}
			
			if(restartGame) {
				restartGame = false;
				gameState  = "NORMAL";
		
				curLevel = 1;
				String newWorld = "level"+curLevel+".png";
				World.restartGame(newWorld);
			}
		}else if(gameState == "MENU") {
			menu.tick();
		}else if(gameState == "PAUSE") {
			pause.tick();
		}else if(gameState == "HELP") {
			help.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();//Otimizar a renderizaçao
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Renderização do jogo
		
		//Graphics g2 = (Graphics2D) g;
		world.render(g);
		Collections.sort(entities , Entity.entitySorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < shoot.size(); i++) {
			shoot.get(i).render(g);
		}
		ui.render(g);
		
		/***/ 
		g.dispose();
		g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		
		if(gameState == "GAMEOVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,200));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g2.setFont(new Font("arial", Font.CENTER_BASELINE, 50));
			g2.setColor(Color.white);
			g2.drawString("GAME OVER", WIDTH*SCALE/3, HEIGHT*SCALE/2);
			g2.setFont(new Font("arial", Font.CENTER_BASELINE, 20));
			g2.setColor(Color.white);
			if(showMessageGameOver) {
				g2.drawString("Press Enter to restart", WIDTH*SCALE/3, HEIGHT*SCALE/1-20);
			}
		}else if(gameState == "MENU") {
			menu.render(g);
		}else if(gameState == "NORMAL") {
			minimap.render(g);
		}else if(gameState == "PAUSE") {
			pause.render(g);
		}else if(gameState == "HELP") {
			help.render(g);
		}
		bs.show();
	}
	
	public void run() {
		//Fps amostragem correta
		long lastTime = System.nanoTime();//long = pega o ultimo momento nanoTime = mais precisao
		double amountOfTicks = 60.0;//60 updates por seg
		double ns = 1000000000/amountOfTicks;//momento certo do update do jogo
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta>=1) {
				requestFocus();
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || 
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			
			if(gameState == "MENU") {
				menu.up = true;
			}else if(gameState == "PAUSE") {
				pause.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}else if(gameState == "PAUSE") {
				pause.down = true;
			}
		}
		
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			player.isShoot = true;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			restartGame = true;
			//Menu
			if(gameState == "MENU") {
				curMenu = "MENU";
				menu.enter = true;
			}
			if(gameState == "MENU") {
				menu.exit = true;
			}
			if(gameState =="MENU") {
				menu.help = true;
			}
			if(gameState == "MENU") {
				menu.load = true;
			}
			
			//Pause Menu
			if(gameState == "PAUSE") {
				pause.enter = true;
			}
			if(gameState == "PAUSE") {
				pause.exit = true;
			}
			if(gameState =="PAUSE") {
				pause.help = true;
			}
			if(gameState == "PAUSE") {
				pause.save = true;
			}
			//Help
			if(gameState == "HELP") {
				help.back = true;
			}
		}
		
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE || 
				e.getKeyCode() == KeyEvent.VK_P) {
			if(gameState == "NORMAL") {
				gameState = "PAUSE";
				curMenu = "PAUSE";
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || 
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		player.mx = (e.getX()/3);
		player.my = (e.getY()/3);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(gameState == "NORMAL") {
			player.mouseMove = true;
			player.mx = (e.getX()/3);
			player.my = (e.getY()/3);
		}
	}
	
}

