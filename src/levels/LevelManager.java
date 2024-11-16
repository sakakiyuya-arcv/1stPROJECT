package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utilz.LoadSave;

public class LevelManager {

	private Game game;
	private BufferedImage[] levelSprite;
	private Level levelOne;
	private BufferedImage treesImg,tree1,objectsImage,templeGate,well,
backgroundImg1,backgroundImg2,backgroundImg3;

	public LevelManager(Game game) {
		this.game = game;
		importOutSideSprites();
		levelOne = new Level(LoadSave.GetLevelData());
		backgroundImg1 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMG1);
		backgroundImg2 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMG2);
		backgroundImg3 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMG3);
		treesImg = LoadSave.GetSpriteAtlas(LoadSave.TREES);
		objectsImage = LoadSave.GetSpriteAtlas(LoadSave.OBJECTS);
		tree1 = treesImg.getSubimage(0, 0, 140, 160);
		templeGate = objectsImage.getSubimage(31, 0, 98, 96);
		well = objectsImage.getSubimage(136, 0, 88, 96);
	}

	private void importOutSideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[65];
		for(int j=0;j<5;j++)
			for(int i=0;i<13;i++) {
				int index = j*13+i;
				levelSprite[index]=img.getSubimage(i*32, j*32, 32, 32);
			}
	}
	public void draw(Graphics g, int lvlOffset) {
		g.drawImage(backgroundImg3, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT , null);
		g.drawImage(backgroundImg2, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT , null);
		g.drawImage(backgroundImg1, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT , null);
		g.drawImage(templeGate, Game.GAME_WIDTH/2-2*Game.TILES_SIZE-(int)(28.5*Game.SCALE), Game.GAME_HEIGHT - 6*Game.TILES_SIZE-(int)(17*Game.SCALE), 4*Game.TILES_SIZE+(int)(20*Game.SCALE),4*Game.TILES_SIZE+(int)(23*Game.SCALE), null);
		for(int j=0;j< Game.TILES_IN_HEIGHT;j++)
			for(int i=0;i< levelOne.getLevlData()[0].length;i++) {
				int index = levelOne.getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], Game.TILES_SIZE*i - lvlOffset,Game.TILES_SIZE*j,Game.TILES_SIZE,Game.TILES_SIZE, null);
			}
		g.drawImage(tree1, (int)(-34*Game.SCALE), Game.GAME_HEIGHT - Game.TILES_SIZE*4-(int)(217*Game.SCALE), (int)(194*Game.SCALE), (int)(222*Game.SCALE), null);
		g.drawImage(well, Game.GAME_WIDTH-(int)(170*Game.SCALE), Game.GAME_HEIGHT - 2*Game.TILES_SIZE-(int)(143*Game.SCALE), (int)(136*Game.SCALE), (int)(148*Game.SCALE), null);

	}
	public void update() {
		
	}
	public Level getCurrentLevel() {
		return levelOne;
	}
}
