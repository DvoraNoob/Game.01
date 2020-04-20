package com.dvoragames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.dvoragame.world.World;

public class Pause {
	
	public String[] options = {"resume","save","exit","?"};
	
	public int curOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up,down,enter,resume,save,exit,help;
	
	public void tick() {
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
			enter = false;
			if(options[curOption] == "resume") {
				Game.gameState = "NORMAL";
			}
		}
		if(save) {
			save = false;
			if(options[curOption] == "save") {
				String[] opt1 = {"level","vida"};
				int[] opt2 = {Game.curLevel,(int) Game.player.vida};
				Pause.saveGame(opt1, opt2, 50);
				System.out.println("Salvo");
			}
		}
		if(exit) {
			exit = false;
			if(options[curOption] == "exit") {
				Game.gameState = "MENU";
			}
		}
		if(help) {
			help = false;
			if(options[curOption] == "?") {
				Game.gameState = "HELP";
			}
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for (int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) 
			{
				case "level":
					World.restartGame("level"+spl2[1]+".png");
					Game.curLevel = Integer.parseInt(spl2[1]);
					Game.gameState = "NORMAL";
					break;
				case "vida":
					Game.player.vida = Integer.parseInt(spl2[1]);
					break;
			}
		}
	}
	
	public static void saveGame(String[] val1, int[] val2,int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter (new FileWriter("save.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n<value.length;n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1) {
					write.newLine();
				}
			}catch(IOException e) {
				
			}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {
			
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,200));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);		
		
		g.setColor(Color.yellow);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 100));
		g.drawString("PAUSE",Game.WIDTH*Game.SCALE/4+33,Game.HEIGHT*Game.SCALE/3);
		
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
		g.drawString("Resume",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2);
		
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
		g.drawString("Save",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2+70);
		
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
		g.drawString("Exit",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2+140);
	
		g.setColor(Color.GRAY);
		g.fillOval(Game.WIDTH*Game.SCALE-60, Game.HEIGHT*Game.SCALE-60, 50, 50);
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
		g.drawString("?",Game.WIDTH*Game.SCALE-47,Game.HEIGHT*Game.SCALE-20);
		
		if(options[curOption] == "resume") {
			g.setColor(Color.blue);
			g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
			g.drawString("Resume",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2);
			
			g.drawString(">",Game.WIDTH*Game.SCALE/3+35,Game.HEIGHT*Game.SCALE/2);
		}else if(options[curOption] == "save") {
			g.setColor(Color.blue);
			g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
			g.drawString("Save",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/2+70);

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
