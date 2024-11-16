package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;

public class GameOverOverlay {

	private Playing playing;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.setColor(Color.white);
		g.drawString("Game Over", Game.GAME_WIDTH / 2-(int)("Game Over".length()/2*Game.SCALE),(int)(4.6875*Game.TILES_SIZE));
		g.drawString("Press esc to enter Main Menu!", Game.GAME_WIDTH / 2-(int)("Press esc to enter Main Menu!".length()*Game.SCALE), (int)(9.375*Game.TILES_SIZE));

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playing.resetAll();
			Gamestate.state = Gamestate.MENU;
		}
	}
}