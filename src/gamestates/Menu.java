package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {

	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage backgroundImg;
	public Menu(Game game) {
		super(game);
		loadButtons();
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int)(222 * Game.SCALE), 0, Gamestate.PLAYING);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int)(292 * Game.SCALE), 1, Gamestate.OPTION);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int)(362 * Game.SCALE), 2, Gamestate.QUIT);

	}

	@Override
	public void update() {
		for(MenuButton mb : buttons)
			mb.update();
		
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		for(MenuButton mb : buttons)
			mb.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				mb.setMousePressed(true);
				break;
			}
		}	
		
	}

	private void resetButtons() {
		for(MenuButton mb : buttons) 
			mb.resetBools();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				if(mb.isMousePressed())
					mb.applyGamestate();
				break;
			}
		}
		resetButtons();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for(MenuButton mb : buttons) 
			mb.setMouseOver(false);
		for(MenuButton mb : buttons) 
			if(isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			Gamestate.state = Gamestate.PLAYING;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
