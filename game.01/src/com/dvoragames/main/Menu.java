package com.dvoragames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.dvoragame.world.World;

public class Menu {
	
	public String[] options = {"play","load","exit","?"};
	
	public int curOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up,down,enter,load,exit,help;
	
	public static boolean saveExists = false;
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		
		if(up) {
			up = false;
			curOption--;
			if(curOption < 0) {
				curOption = maxOption;
			}
		}
		if(down) {
			down = false;
			curOption++;
			if(curOption > maxOption) {
				curOption = 0;
			}
		}
		if(enter) {
			Sound.Clips.music.loop();
			enter = false;
			if(options[curOption] == "play") {
				Game.gameState = "NORMAL";
				World.restartGame("level1.png");
			}
		}
		if(load) {
			load = false;
			if(options[curOption] == "load") {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(50);
					Pause.applySave(saver);
				}
			}
		}
		if(exit) {
			exit = false;
			if(options[curOption] == "exit") {
				
			}
		}
		if(help) {
			help = false;
			if(options[curOption] == "?") {
				Game.gameState = "HELP";
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null){
						String[] transition = singleLine.split(":");
						char[] val = transition[1].toCharArray();
						transition[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i]-=encode;
							transition[1]+=val[i];
						}
						line+=transition[0];
						line+=":";
						line+=transition[1];
						line+="/";
					}
				}catch(IOException e) {
					
				}
			}catch(FileNotFoundException e) {
				
			}
		}
		return line;
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(30,30,30));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		
		g.setColor(Color.yellow);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 100));
		g.drawString(">GAME<",Game.WIDTH*Game.SCALE/5+6,Game.HEIGHT*Game.SCALE/3);
		
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
		g.drawString("Play",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2);
		
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
		g.drawString("Load",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2+70);
		
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
		g.drawString("Exit",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2+140);
	
		g.setColor(Color.GRAY);
		g.fillOval(Game.WIDTH*Game.SCALE-60, Game.HEIGHT*Game.SCALE-60, 50, 50);
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
		g.drawString("?",Game.WIDTH*Game.SCALE-47,Game.HEIGHT*Game.SCALE-20);

		
		if(options[curOption] == "play") {
			g.setColor(Color.blue);
			g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
			g.drawString("Play",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2);
			
			g.drawString(">",Game.WIDTH*Game.SCALE/3+35,Game.HEIGHT*Game.SCALE/2);
		}else if(options[curOption] == "load") {
			g.setColor(Color.blue);
			g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
			g.drawString("Load",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2+70);

			g.drawString(">",Game.WIDTH*Game.SCALE/3+35,Game.HEIGHT*Game.SCALE/2+70);
		}else if(options[curOption] == "exit") {
			g.setColor(Color.blue);
			g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
			g.drawString("Exit",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2+140);
			
			g.drawString(">",Game.WIDTH*Game.SCALE/3+35,Game.HEIGHT*Game.SCALE/2+140);
		}else if(options[curOption] == "?") {
			g.setColor(Color.blue);
			g.fillOval(Game.WIDTH*Game.SCALE-60, Game.HEIGHT*Game.SCALE-60, 50, 50);
			
			g.setColor(Color.white);
			g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
			g.drawString("?",Game.WIDTH*Game.SCALE-47,Game.HEIGHT*Game.SCALE-20);

			g.setColor(Color.blue);
			g.drawString(">",Game.WIDTH*Game.SCALE-90,Game.HEIGHT*Game.SCALE-20);

	}

	}
}
