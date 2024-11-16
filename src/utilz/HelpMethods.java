package utilz;

import java.awt.geom.Rectangle2D;

import entities.PlayerData;
import main.Game;

public class HelpMethods {
	
	public static boolean CanMoveHere(float x, float y, float witdth, float height, int[][] lvlData) {
		if(!IsSolid(x, y, lvlData))
			if(!IsSolid(x+witdth, y+height, lvlData))
				if(!IsSolid(x+witdth, y, lvlData))
					if(!IsSolid(x, y+height, lvlData))
						if(!IsSolid(x+witdth, y+height/2, lvlData))
							if(!IsSolid(x, y+height/2, lvlData))
						return true;
		return false;
					
	}
	
//	public static boolean IsSolid(float x, float y, int[][] lvlData) {
//		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
//		if(x < 0 || x >= maxWidth)
//			return true;
//		if(y < 0 || y >= Game.GAME_HEIGHT)
//			return true;
//		
//		float xIndex = x / Game.TILES_SIZE;
//		float yIndex = y / Game.TILES_SIZE;
//		
//		int value = lvlData[(int)yIndex][(int)xIndex];
//
//		if(value >= 48 || value < 0 || value != 11)
//			return true;
//		return false;
//	}
	public static boolean IsSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		if(x < 0 || x >= maxWidth)
			return true;
		if(y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		int value = lvlData[(int)yIndex][(int)xIndex];

		if(value >= 65 || value < 0 || (value != 11&&value != 0))
			return true;
		return false;
	}
	
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
		if(xSpeed > 0) {
			int tilePos = currentTile * Game.TILES_SIZE;
			int xOffset = (int)(Game.TILES_SIZE-hitbox.width%Game.TILES_SIZE);
			return tilePos + xOffset -1;
		}if(xSpeed<0)
			return
				currentTile * Game.TILES_SIZE +hitbox.x%Game.TILES_SIZE;
		else 
			return currentTile * Game.TILES_SIZE;
	}
	public static float GetEntityYPosUnderRoofOrAboveFlorr(Rectangle2D.Float hitbox, float airSpeed, PlayerData playerData) {

		int currentTile = (int)((hitbox.y+hitbox.height)/Game.TILES_SIZE);
		if(airSpeed > 0) {
			int tileYPos = currentTile * Game.TILES_SIZE;
//			int yOffset = (int)(Game.TILES_SIZE-hitbox.height);
				return tileYPos + Game.TILES_SIZE -hitbox.height - 1;
		} else
			return (int)((hitbox.y)/Game.TILES_SIZE) * Game.TILES_SIZE;
		
	}
	public static boolean IsTilse12(float x, float y, int[][] lvlData) {
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		int value = lvlData[(int)yIndex][(int)xIndex];
		if(value==12)
			return true;
		else 
			return false;
	}
	public static boolean CheckOnObject(Rectangle2D.Float hitbox, int[][] lvlData, float airspeed) {
		if(hitbox.height+hitbox.y+airspeed<Game.GAME_HEIGHT) {
		if(airspeed<0)
			if(IsTilse12(hitbox.x, hitbox.y+airspeed, lvlData)||
			   IsTilse12(hitbox.x+hitbox.width, hitbox.y+airspeed, lvlData)||
			   IsTilse12(hitbox.x+hitbox.width, hitbox.y+airspeed+hitbox.height, lvlData)||
			   IsTilse12(hitbox.x, hitbox.y+airspeed+hitbox.height, lvlData)||
			   IsTilse12(hitbox.x, hitbox.y+airspeed+hitbox.height/3, lvlData)||
			   IsTilse12(hitbox.x+hitbox.width, hitbox.y+airspeed+hitbox.height/3, lvlData)||
			   IsTilse12(hitbox.x, hitbox.y+airspeed+2*hitbox.height/3, lvlData)||
			   IsTilse12(hitbox.x+hitbox.width, hitbox.y+airspeed+2*hitbox.height/3, lvlData))
			return true;
		 if(airspeed>=0) 
			if(IsTilse12(hitbox.x+hitbox.width, hitbox.y+airspeed, lvlData)||
			   IsTilse12(hitbox.x, hitbox.y+airspeed, lvlData)||
			   IsTilse12(hitbox.x, hitbox.y+airspeed+hitbox.height/3, lvlData)||
			   IsTilse12(hitbox.x+hitbox.width, hitbox.y+airspeed+hitbox.height/3, lvlData)||
			   IsTilse12(hitbox.x, hitbox.y+airspeed+2*hitbox.height/3, lvlData)||
			   IsTilse12(hitbox.x+hitbox.width, hitbox.y+airspeed+2*hitbox.height/3, lvlData))
				return true;
		}
		return false;
	}
	public static boolean CheckNextToObject(Rectangle2D.Float hitbox, int[][] lvlData, float xSpeed) {
		if(hitbox.x+xSpeed+hitbox.width<Game.GAME_WIDTH)
		if(IsTilse12(hitbox.x+xSpeed, hitbox.y, lvlData)||
		   IsTilse12(hitbox.x+xSpeed+hitbox.width, hitbox.y, lvlData)||
		   IsTilse12(hitbox.x+xSpeed+hitbox.width, hitbox.y+hitbox.height, lvlData)||
		   IsTilse12(hitbox.x+xSpeed, hitbox.y+hitbox.height, lvlData)||
		   IsTilse12(hitbox.x+xSpeed, hitbox.y+hitbox.height/3, lvlData)||
		   IsTilse12(hitbox.x+xSpeed+hitbox.width, hitbox.y+hitbox.height/3, lvlData)||
		   IsTilse12(hitbox.x+xSpeed, hitbox.y+2*hitbox.height/3, lvlData)||
		   IsTilse12(hitbox.x+xSpeed+hitbox.width, hitbox.y+2*hitbox.height/3, lvlData))
			return true;
		return false;
	}
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if(!IsSolid(hitbox.x, hitbox.y+hitbox.height+1, lvlData))
				if(!IsSolid(hitbox.x +hitbox.width, hitbox.y+hitbox.height+1, lvlData))
					return false;
		return true;
	}
	public static boolean traped(Rectangle2D.Float hitbox, int[][] lvlData) {
		float xIndex1 = hitbox.x / Game.TILES_SIZE;
		float yIndex1 = (hitbox.y+hitbox.height) / Game.TILES_SIZE;
		float xIndex2 = (hitbox.x+hitbox.width) / Game.TILES_SIZE;
		float yIndex2 = (hitbox.y+hitbox.height) / Game.TILES_SIZE;
		int value1 = lvlData[(int)yIndex1][(int)xIndex1];
		int value2 = lvlData[(int)yIndex2][(int)xIndex2];
		if(value1==0||value2==0)
			return true;
		else 
			return false;
	}

}
