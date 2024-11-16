package entities;



import main.Game;
import utilz.LoadSave;

public class PlayerData {
	
	public String img;
	public int width;
	public int height;
	public int rowNum;
	public int columnNum;
	public int imgWidth;
	public int imgHeight;
	public int xOffset;
	public int yOffset;
	public float hitboxX, hitboxY;
	
	public int attackBox1Width;
	public int attackBox1Height;
	public int at1i;
	public int at1j;
	public int attackBox2Width;
	public int attackBox2Height;
	public int at2i;
	public int at2j;
	public int attackBox3Width;
	public int attackBox3Height;
	public int at3i;
	public int at3j;
	public int SpecialAttackBoxWidth;
	public int SpecialAttackBoxHeight;
	public int sti;
	public int stj;
	public float playerSpeed;
	public float playerScale;
	
	public PlayerData(String string, float playerSpeed, float playerScale,
			int width, int height, int a, int b, int c, int d, 
			int e, int f, float hitboxX, float hitboxY,
			int attackbox1Width,int attackbox1Height,int at1i,int at1j,
			int attackbox2Width,int attackbox2Height,int at2i,int at2j,
			int attackbox3Width,int attackbox3Height,int at3i,int at3j,
			int SpecialAttackBoxWidth,int SpecialAttackBoxHeight, int sti, int stj) {
		this.img = string;
		this.playerSpeed = playerSpeed;
		
		this.width = (int)(width*playerScale);
		this.height = (int)(height*playerScale);
		this.rowNum = a;
		this.columnNum = b;
		this.imgWidth = c;
		this.imgHeight = d;
		
		this.xOffset = (int)(e*playerScale);
		this.yOffset = (int)(f*playerScale);
		this.hitboxX = hitboxX;
		this.hitboxY = hitboxY;
		
		this.attackBox1Height=(int)(attackbox1Height*playerScale);
		this.attackBox1Width=(int)(attackbox1Width*playerScale);
		this.at1i=(int)(at1i*playerScale);
		this.at1j=(int)(at1j*playerScale);
		
		this.attackBox2Height=(int)(attackbox2Height*playerScale);
		this.attackBox2Width=(int)(attackbox2Width*playerScale);
		this.at2i=(int)(at2i*playerScale);
		this.at2j=(int)(at2j*playerScale);
		
		this.attackBox3Height=(int)(attackbox3Height*playerScale);
		this.attackBox3Width=(int)(attackbox3Width*playerScale);
		this.at3i=(int)(at3i*playerScale);
		this.at3j=(int)(at3j*playerScale);
		
		this.SpecialAttackBoxWidth=(int)(SpecialAttackBoxWidth*playerScale);
		this.SpecialAttackBoxHeight=(int)(SpecialAttackBoxHeight*playerScale);
		this.sti=(int)(sti*playerScale);
		this.stj=(int)(stj*playerScale);
	}
	
	public static final PlayerData DEMON_SAMURAI = new PlayerData(LoadSave.DEMON_SAMURAI_ATLAS,1.0f * Game.SCALE,1.2f,
			(int)(64*2*Game.SCALE),(int)(54*2*Game.SCALE), 
			20, 26, 128, 108,
			(int)(53.6*Game.SCALE),(int)(48*Game.SCALE),6f,2.4f,
			35,36,15,14,
			40,48,20,5,
			39,55,19,-1,
			43,96,23,-46);
	public static final PlayerData WOLF_SAMURAI = new PlayerData(LoadSave.WOLF_SAMURAI_ATLAS, 1.2f * Game.SCALE,1.2f,
			(int)(96*2*Game.SCALE),(int)(32*2*Game.SCALE), 10, 12, 192, 64,
			(int)(77.4*Game.SCALE),(int)(26.66*Game.SCALE),5.2f,2.4f,
			25,48,-12,-18,
			24,41,-13,-23,
			23,61,-15,-23,
			25,45,-12,-15);
	public static final PlayerData SAMURAI_2 = new PlayerData(LoadSave.SAMURAI_2_ATLAS, 1.0f * Game.SCALE,1.2f,
			(int)(172.8*Game.SCALE),(int)(69.12*Game.SCALE), 10, 11, 250, 110,
			(int)(74*Game.SCALE),(int)(21*Game.SCALE),7f,1.52f,
			49,60,25,-20,
			25,70,2,-20,
			32,50,9,0,
			75,30,52,10);


	public static final int IDLE = 0;
	public static final int RUNNING = 1;
	public static final int JUMP = 2;
	public static final int FALLING = 3;
	public static final int HIT = 4;
	public static final int ATTACK_1 = 5;
	public static final int ATTACK_2 = 6;
	public static final int ATTACK_3 = 7;
	public static final int SPECIAL_ATTACK =8;
	public static final int DEATH = 9;
	public static final int SHOUT = 10;

	public static int GetSpriteAmount(PlayerData playerData, int playerAction) {
		if(playerData == DEMON_SAMURAI)
			switch (playerAction) {
		case IDLE:
			return 6;
		case RUNNING:
			return 8;
		case JUMP:
			return 3;
		case HIT:
			return 4;
		case ATTACK_1:
		case ATTACK_3:
			return 7;
		case ATTACK_2:
			return 5;
		case SPECIAL_ATTACK:
			return 12;
		case DEATH:
			return 26;
		case SHOUT:
			return 17;
		case FALLING:
			return 1;
		}
		if(playerData == WOLF_SAMURAI)
			switch (playerAction) {
		case IDLE:
		case RUNNING:
		case ATTACK_1:
		case HIT:
			return 6;
		case JUMP:
			return 3;
		case ATTACK_2:
			return 4;
		case ATTACK_3:
			return 5;
		case SPECIAL_ATTACK:
			return 12;
		case DEATH:
			return 8;
		case FALLING:
			return 1;
		}
		if(playerData == SAMURAI_2)
			switch (playerAction) {
			case IDLE:
				return 5;
			case RUNNING:
				return 7;
			case JUMP:
				return 3;
			case HIT:
				return 3;
			case ATTACK_1:
			case ATTACK_2:
				return 5;
			case ATTACK_3:
				return 10;
			case SPECIAL_ATTACK:
				return 11;
			case DEATH:
				return 10;
			case FALLING:
				return 1;
		}
		return 1;
	}	
}
