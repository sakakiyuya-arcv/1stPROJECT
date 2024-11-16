package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import entities.Player;
import entities.PlayerData;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;



public class Playing extends State implements Statemethods{
	private Player player1, player2;
	private LevelManager levelManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private boolean gameOver;
	private boolean paused = false;
	public BufferedImage winningPlayerImg,winningPlayerImg1,winningPlayerImg2;
	private int xLvlOffset;
	private int lefBorder = (int)(0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
	public PlayerData player1Data, player2Data;
	
	public Playing(Game game) {
		super(game);
		player1Data = PlayerData.SAMURAI_2;
		player2Data = PlayerData.DEMON_SAMURAI;
		initClasses();
	}
	
	private void initClasses() {
		levelManager = new LevelManager(game);
		player1 = new Player(378*Game.SCALE, 200*Game.SCALE, player1Data,this);
		player2 = new Player(756*Game.SCALE, 200*Game.SCALE,player2Data,this);
		player1.enemy=player2;
		player2.enemy=player1;
		player1.loadLvlData(levelManager.getCurrentLevel().getLevlData());
		player2.loadLvlData(levelManager.getCurrentLevel().getLevlData());
		player1.inRight = true;
		player2.inLeft = true;
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		
		player1.i=0;
		player2.i=1;
		player1.j=-1;
		player2.j=1;
		player1.setHealthBarXStart((int) (10 * Game.SCALE));
		player2.setHealthBarXStart((int) (950 * Game.SCALE));
		player1.setPowerBarXStart((int) (30 * Game.SCALE));
		player2.setPowerBarXStart((int) (930 * Game.SCALE));

		winningPlayerImg1 = LoadSave.GetSpriteAtlas(LoadSave.PLAYER1_WIN);
		winningPlayerImg2 = LoadSave.GetSpriteAtlas(LoadSave.PLAYER2_WIN);
	}
	
	@Override
	public void update() {
		if(!paused) {
			levelManager.update();
			player1.update();
			player2.update();
			checkCloseToBorder();
			} else {
			pauseOverlay.update();
		}
	}

	private void checkCloseToBorder() {
		int playerX = (int) player1.getHibox().x;
		int diff = playerX - xLvlOffset;
		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < lefBorder)
			xLvlOffset += diff - lefBorder;
		if(xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if( xLvlOffset < 0)
			xLvlOffset = 0;
	}

	@Override
	public void draw(Graphics g) {
		levelManager.draw(g, xLvlOffset);
		player1.render(g, xLvlOffset);
		player2.render(g, xLvlOffset);

		if(paused) {
			 g.setColor(new Color(0,0,0,150));
			 g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}else if (gameOver) 
			gameOverOverlay.draw(g);
		
	}

	public void resetAll() {
		player1.resetAll();
		player2.resetAll();
		player1.inRight = true;
		player2.inLeft = true;
		gameOver = false;
		paused = false;
	}
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!gameOver)
			if(paused)
				pauseOverlay.mousePressed(e);
		
	}
	public void mouseDragged(MouseEvent e) {
		if(!gameOver)
			if(paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!gameOver)
			if(paused)
				pauseOverlay.mouseReleased(e);
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!gameOver)
			if(paused)
				pauseOverlay.mouseMoved(e);
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gameOver)
			gameOverOverlay.keyPressed(e);
		else
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player1.setLeft(true);
			break;
		case KeyEvent.VK_D:
			player1.setRight(true);
			break;
		case KeyEvent.VK_W:
			if(player1.checkInAir()) {
			player1.setDoubleJump(true);
			} else {
				player1.setJump(true);
			}
			break;	
		case KeyEvent.VK_S:
			if(player1Data== PlayerData.DEMON_SAMURAI)
			player1.setFalme();
			break;
		case KeyEvent.VK_J:
			player1.setAttack1(true);
			break;
		case KeyEvent.VK_K:
			player1.setAttack2(true);
			break;
		case KeyEvent.VK_L:
			player1.setAttack3(true);
			break;
		case KeyEvent.VK_I:
			player1.setSpecialAttack();
			break;	
			
		case KeyEvent.VK_ESCAPE:
			paused = !paused;
			break;
			
		case KeyEvent.VK_LEFT:
			player2.setLeft(true);
			break;
		case KeyEvent.VK_RIGHT:
			player2.setRight(true);
			break;
		case KeyEvent.VK_UP:
			if(player2.checkInAir()) {
				player2.setDoubleJump(true);
				} else {
					player2.setJump(true);
				}
			break;	
		case KeyEvent.VK_DOWN:
			if(player2Data== PlayerData.DEMON_SAMURAI)
			player2.setFalme();
			break;
		case KeyEvent.VK_NUMPAD1:
			player2.setAttack1(true);
			break;	
		case KeyEvent.VK_NUMPAD2:
			player2.setAttack2(true);
			break;	
		case KeyEvent.VK_NUMPAD3:
			player2.setAttack3(true);
			break;	
		case KeyEvent.VK_NUMPAD0:
			player2.setSpecialAttack();
			break;	
		
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!gameOver)
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player1.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player1.setRight(false);
			break;
		case KeyEvent.VK_W:{
			player1.setJump(false);
			player1.setDoubleJump(false);
		}
			break;
			
		case KeyEvent.VK_LEFT:
			player2.setLeft(false);
			break;
		case KeyEvent.VK_RIGHT:
			player2.setRight(false);
			break;
		case KeyEvent.VK_UP:{
			player2.setJump(false);
			player2.setDoubleJump(false);
		}
			break;
	}

		
	}
	public void windowFocusLost() {
		player1.resetDirBooleans();
		player2.resetDirBooleans();

	}

	public Player getPlayer1() {
		return player1;
	}
	public Player getPlayer2() {
		return player2;
	}
	public void unpauseGame() {
		paused = false;
	}
}
