package entities;

import static entities.PlayerData.*;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity {
	private BufferedImage[][] animations;
	private int tick,aniTick, aniIndex, aniSpeed = 25;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false, death = false, shout = false, flame = false;
	private boolean left, up, right, down,jump,doubleJump, attack1=false, attack2=false, attack3=false, specialAttack=false;
	private float playerSpeed ;
	private int[][] lvlData;
	private float xDrawOffset;
	private float yDrawOffset;
	
	private float airSpeed = 0f;
	private float gravity = 0.05f*Game.SCALE;
	private float jumpSpeed = -2.8125f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5625f * Game.SCALE;
	private boolean inAir = false;
	private boolean canDoubleJump = false;
	private static final long FLAMETIMES = 20000;
	private long setFlameTime = 0;
	private PlayerData playerData;
	private int flipX = 0;
	private int flipW =1;
	public boolean  inRight, inLeft;
	public boolean win = false, lose = false;
	public long endtime;
	

		private int maxHealth=100;
		private int currentHealth = maxHealth;
		public int i;
		public int j;
		private int flipHX = (int)(192*Game.SCALE);
		private int flipHW =-1;
		// StatusBarUI

		private BufferedImage[] healthRegen;
		private int healthRegenIndex;
		private boolean isHealthRegen;
		private BufferedImage[] healthBarAni;
		private int healthBarAniIndex;
		private int healthBarAniState;
		private int healthBarWidth = (int) (64 * 3 * Game.SCALE);
		private int healthBarHeight = (int) (16 * 3 * Game.SCALE);
		private int healthBarXStart = (int) (10 * Game.SCALE);
		private int healthBarYStart = (int) (10 * Game.SCALE);
		private float healthPercent;
		private int previousHealth = currentHealth;

		private BufferedImage[] manaSparkling;
		private BufferedImage[] manaRegen;
		private BufferedImage[] manaFading;
		private int powerBarRegenAniIndex;
		private int powerBarFadingAniIndex;
		private int powerBarRegenAniState;
		private int powerBarFadingAniState;
		private int powerBarSparklingIndex;
		private int powerBarWidth = (int) (64 * 3 * Game.SCALE);
		private int powerBarHeight = (int) (16 * 3 * Game.SCALE);
		private int powerBarSparklingHeight = (int) (18 * 3 * Game.SCALE);
		private int powerBarXStart = (int) (10 * Game.SCALE);
		private int powerBarYStart = (int) (powerBarHeight + 4 * Game.SCALE );
		private int powerMaxValue = 200;
		private int powerValue = 0;
		private float powerPercent;
		private int previousPowerValue;
		private boolean isPowerIncrease;
		private boolean isPowerBarSparkling;
		private boolean	isPowerBarFading;
		
		private float powerGrowSpeed = 0.5f;
		private int powerGrowTick;
	//atackbox
		private Rectangle2D.Float specialAttackBox,attackBox1,attackBox2,attackBox3;
		private boolean attackChecked;
		private boolean hurt;
		public Player enemy;
		private Playing playing;

		public Player(float x, float y, PlayerData playerData, Playing playing) {
			super(x, y,playerData.width,playerData.height);
			this.playerData = playerData;
			this.xDrawOffset = this.playerData.xOffset ;
			this.yDrawOffset = this.playerData.yOffset ;
			this.playing = playing;
			this.playerSpeed = playerData.playerSpeed;
			this.isHealthRegen = true;
			this.maxHealth = 100;
			this.currentHealth = maxHealth;
			loadAnimations();
			initHitBox(x  , y , (int)(playerData.width / this.playerData.hitboxX ), (int)(playerData.height / this.playerData.hitboxY));
			initAttackBox();
		}

		public void initAttackBox() {
			specialAttackBox = new Rectangle2D.Float(x,y,(int)(playerData.SpecialAttackBoxWidth*Game.SCALE),(int)(playerData.SpecialAttackBoxHeight*Game.SCALE-4));
			attackBox1 = new Rectangle2D.Float(x,y,(int)(playerData.attackBox1Width*Game.SCALE),(int)(playerData.attackBox1Height*Game.SCALE-4));
			attackBox2 = new Rectangle2D.Float(x,y,(int)(playerData.attackBox2Width*Game.SCALE),(int)(playerData.attackBox2Height*Game.SCALE-4));
			attackBox3 = new Rectangle2D.Float(x,y,(int)(playerData.attackBox3Width*Game.SCALE),(int)(playerData.attackBox3Height*Game.SCALE-4));
		}
		
		public void updateAttackBox() {
			if(right) {
				
				attackBox1.x = hitbox.x + hitbox.width + (int)(Game.SCALE*1);
				attackBox2.x = hitbox.x + hitbox.width + (int)(Game.SCALE*1);
				attackBox3.x = hitbox.x + hitbox.width + (int)(Game.SCALE*1);
				specialAttackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE*1);
				
			} 
			else if(left) {
				
				attackBox1.x = hitbox.x - hitbox.width -(int)(Game.SCALE*playerData.at1i);
				attackBox2.x = hitbox.x - hitbox.width -(int)(Game.SCALE*playerData.at2i);
				attackBox3.x = hitbox.x - hitbox.width -(int)(Game.SCALE*playerData.at3i);
				specialAttackBox.x = hitbox.x - hitbox.width -(int)(Game.SCALE*playerData.sti);
				
			} 
			else if(playerAction == SPECIAL_ATTACK && playerData!= PlayerData.DEMON_SAMURAI) {
				
				if(inRight) {
					
					attackBox1.x = hitbox.x + hitbox.width + (int)(Game.SCALE*1);
					attackBox2.x = hitbox.x + hitbox.width + (int)(Game.SCALE*1);
					attackBox3.x = hitbox.x + hitbox.width + (int)(Game.SCALE*1);
					specialAttackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE*1);
					
				} 
				else if(inLeft) {
					
					attackBox1.x = hitbox.x - hitbox.width -(int)(Game.SCALE*playerData.at1i);
					attackBox2.x = hitbox.x - hitbox.width -(int)(Game.SCALE*playerData.at2i);
					attackBox3.x = hitbox.x - hitbox.width -(int)(Game.SCALE*playerData.at3i);
					specialAttackBox.x = hitbox.x - hitbox.width -(int)(Game.SCALE*playerData.sti);
	
				}
			}
			
			attackBox1.y = hitbox.y + (Game.SCALE * playerData.at1j);
			attackBox2.y = hitbox.y + (Game.SCALE * playerData.at2j);
			attackBox3.y = hitbox.y + (Game.SCALE * playerData.at3j);
			specialAttackBox.y = hitbox.y + (Game.SCALE * (playerData.stj));
			
		}

		public void update() {
			updateHealthBar();
			updatePowerBar();
			if(traped(hitbox, lvlData)) {
				if(!hurt) {
				hurt = true;
				changeHealth(-5);		
				}
			}
			if (currentHealth <= 0) {
				if(!lose)
				endtime = System.currentTimeMillis();
				enemy.win = true;
				if(enemy==playing.getPlayer1())
					playing.winningPlayerImg = playing.winningPlayerImg1;
				else 
					playing.winningPlayerImg = playing.winningPlayerImg2;
				death = true;
				healthBarAniIndex = 44;
				healthBarAniState = 48;
			}
			updateAttackBox();
			updatePos();
			if (attacking)
				checkEnemyHit();
			if(!lose)
				updateAnimationTick();
			if(lose)
				checkGameOver();
			setAnimation();
			checkFlame();
			}

		private void checkEnemyHit() {
			if(specialAttack) {
				if(playerData==WOLF_SAMURAI||(playerData==DEMON_SAMURAI&&aniIndex>=8)||(playerData==SAMURAI_2&&aniIndex>=6))
					if (specialAttackBox.intersects(enemy.hitbox)) 
						if(!enemy.hurt&&!attackChecked) {
							enemy.changeHealth(-20);
							if(flame)
								enemy.changeHealth(-5);
							enemy.hurt=true;
							attackChecked=true;
						} 
			}
			else {
				if(attack1)
					if (attackBox1.intersects(enemy.hitbox)) 
						if(!enemy.hurt&&!attackChecked) {
						enemy.changeHealth(-10);
						if(flame)
							enemy.changeHealth(-5);
						enemy.hurt=true;
						attackChecked=true;
					}
				if(attack2)
					if (attackBox2.intersects(enemy.hitbox)) 
						if(!enemy.hurt&&!attackChecked) {
						enemy.changeHealth(-10);
						if(flame)
							enemy.changeHealth(-5);
						enemy.hurt=true;
						attackChecked=true;
					}
				if(attack3)
					if (attackBox3.intersects(enemy.hitbox)) 
						if(!enemy.hurt&&!attackChecked) {
						enemy.changeHealth(-10);
						if(flame)
							enemy.changeHealth(-5);
						enemy.hurt=true;
						attackChecked=true;
					}
			}

		}
		
		public void changeHealth(int value) {
			currentHealth += value;
			if (currentHealth <= 0)
				currentHealth = 0;
			else if (currentHealth >= maxHealth)
				currentHealth = maxHealth;
		}

		public void render(Graphics g, int lvlOffset) {
			if(flame&&!death&&(playerAction != SHOUT))
				g.drawImage(animations[playerAction+11][aniIndex], (int)(hitbox.x-xDrawOffset) - lvlOffset + flipX, (int)(hitbox.y-yDrawOffset), width*flipW, height, null);
			else
			g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x-xDrawOffset) - lvlOffset + flipX, (int)(hitbox.y-yDrawOffset), width*flipW, height, null);
//			drawHitBox(g);
//			drawAttackBox1(g,lvlOffset);
//			drawAttackBox2(g,lvlOffset);
//			drawAttackBox3(g,lvlOffset);
//			drawSpecialAttackBox(g,lvlOffset);
			drawUI(g);
			if(lose)
				g.drawImage(playing.winningPlayerImg, Game.GAME_WIDTH/2-(int)(320*Game.SCALE), Game.GAME_HEIGHT/2-(int)(180*Game.SCALE), (int)(640*Game.SCALE), (int)(360*Game.SCALE), null);		}
		
		private void drawSpecialAttackBox(Graphics g, int lvlOffsetX) {
			
			g.setColor(Color.red);
			g.drawRect((int)(specialAttackBox.x-lvlOffsetX),(int) specialAttackBox.y,(int)specialAttackBox.width,(int)specialAttackBox.height);
		}

		private void drawAttackBox1(Graphics g, int lvlOffsetX) {
			g.setColor(Color.green);
			g.drawRect((int) attackBox1.x-lvlOffsetX,(int) attackBox1.y,(int)attackBox1.width,(int)attackBox1.height);
			
		}
		private void drawAttackBox2(Graphics g, int lvlOffsetX) {
			g.setColor(Color.blue);
			g.drawRect((int) attackBox2.x-lvlOffsetX,(int) attackBox2.y,(int)attackBox2.width,(int)attackBox2.height);
			
		}
		private void drawAttackBox3(Graphics g, int lvlOffsetX) {
			g.setColor(Color.black);
			g.drawRect((int) attackBox3.x-lvlOffsetX,(int) attackBox3.y,(int)attackBox3.width,(int)attackBox3.height);
			
		}

		private void drawUI(Graphics g) {
			
			// Initialize
			if (isHealthRegen) {
				g.drawImage(healthRegen[healthRegenIndex], healthBarXStart + i * flipHX, healthBarYStart, healthBarWidth * j * flipHW, healthBarHeight, null);
			}
			
			// Health bar
			else 
				g.drawImage(healthBarAni[healthBarAniIndex], healthBarXStart + i * flipHX, healthBarYStart, healthBarWidth* j * flipHW, healthBarHeight, null);

			// Initialize
			if (powerValue == 0)
				g.drawImage(manaRegen[0], powerBarXStart+i * flipHX, powerBarYStart, powerBarWidth* j * flipHW, powerBarHeight, null);
			// Power Bar
			else if (isPowerBarSparkling)
				g.drawImage(manaSparkling[powerBarSparklingIndex], powerBarXStart+i * flipHX, powerBarYStart, powerBarWidth* j * flipHW, powerBarSparklingHeight, null);
			else if (isPowerIncrease)
				g.drawImage(manaRegen[powerBarRegenAniIndex], powerBarXStart+i * flipHX, powerBarYStart, powerBarWidth* j * flipHW, powerBarHeight, null);
			else
				g.drawImage(manaFading[powerBarFadingAniIndex], powerBarXStart+i * flipHX, powerBarYStart, powerBarWidth* j * flipHW, powerBarHeight, null);
		}
	
		private void updateHealthBar() {
			if (previousHealth == currentHealth)
				return;
			healthPercent = currentHealth / (float) maxHealth;
			healthBarAniState = 48 - (int) (healthPercent / 0.125f) * 6;
			previousHealth = currentHealth;
		}
		private void updatePowerBar() {

//			if (specialAttack)
//				powerValueBeforePowerAttack = powerValue + 60;
			
			powerGrowTick++;
			if (powerGrowTick >= powerGrowSpeed) {
				powerGrowTick = 0;
				changePower(1);
			}

			
			
			if (powerValue == previousPowerValue) {
				// Max
				if (powerValue == powerMaxValue) {
					isPowerBarSparkling = true;
				}
				
				else {
					isPowerBarSparkling = false;
					powerBarSparklingIndex = 0;
				}
				
				return;
			}
			
			
			
			// Increase
			if (powerValue - previousPowerValue > 0 && !isPowerBarFading) {
					isPowerIncrease = true;
					powerPercent = powerValue / (float) powerMaxValue;
					powerBarRegenAniState = (int) (powerPercent / 0.125f) * 5;
					powerBarFadingAniState = manaFading.length - powerBarRegenAniState;
					powerBarFadingAniIndex = manaFading.length - powerBarRegenAniIndex;
			}
			
			// Decrease 
			else {
				isPowerIncrease = false;
				isPowerBarFading = true;
				powerPercent = powerValue / (float) powerMaxValue;
				powerBarFadingAniState = manaFading.length - (int) (powerPercent / 0.125f) * 5;
				if (powerBarFadingAniIndex >= powerBarFadingAniState - 1) {
					powerBarRegenAniState = manaRegen.length - powerBarFadingAniState;
					powerBarRegenAniIndex = manaRegen.length - powerBarFadingAniIndex;
					isPowerBarFading = false;
				}
			}
			
			previousPowerValue = powerValue;
			
		}
		
		public void changePower(int value) {
			powerValue += value;
			powerValue = Math.max(Math.min(powerValue, powerMaxValue), 0);
		}

	private void updateAnimationTick() {
		aniTick++;
		tick++;
		if(tick>=airSpeed/5) {
			if(!isPowerIncrease) {
				if (powerBarFadingAniIndex < powerBarFadingAniState - 1)
					powerBarFadingAniIndex++;
				tick = 0;
			}
		}
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (isHealthRegen)
				if (healthRegenIndex < healthRegen.length - 1)
					healthRegenIndex++;
				else 
					isHealthRegen = false;
			
			if (healthBarAniIndex < healthBarAniState)
				healthBarAniIndex++;
			
			if (isPowerBarSparkling) {
				if (powerBarSparklingIndex < manaSparkling.length - 1)
					powerBarSparklingIndex++;
				else 
					powerBarSparklingIndex = 0;
			}
			
			if (isPowerIncrease) {
				if (powerBarRegenAniIndex < powerBarRegenAniState - 1)
					powerBarRegenAniIndex++;
			}
			

			if(aniIndex>=GetSpriteAmount(playerData, playerAction)-1) {
				if((playerAction == SPECIAL_ATTACK)&&(flame==true)) {
					aniIndex = 0;
					attacking = false;
					specialAttack = false;
				}	
				else if (aniIndex >= GetSpriteAmount(this.playerData,playerAction)) {
						aniIndex = 0;
						hurt = false;
						attackChecked = false;
						attacking = false;
						attack1 = false;
						attack2 = false;
						attack3 = false;
						specialAttack = false;
						shout = false;
					}
				if(playerAction == DEATH) {
					lose = true;
				}
			}
		}
	}
	private void checkGameOver() {
		if(System.currentTimeMillis()-endtime>2000)
			playing.setGameOver(true);
	}

	public void setFalme() {
		if(setFlameTime == 0 || (System.currentTimeMillis() - setFlameTime >= 3 * FLAMETIMES )) {
		flame = true;
		shout = true;
		setFlameTime = System.currentTimeMillis();		
		}
	}
	private void checkFlame() {
		if(flame)
		if(System.currentTimeMillis() - setFlameTime>FLAMETIMES)
			flame = false;
	}

	private void setAnimation() {
		int startAni = playerAction;

		if (moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;
		if(inAir) {
			if(airSpeed <0)
				playerAction = JUMP;
			else {
				playerAction = FALLING;
			}
		}

		if(hurt)
			playerAction = HIT;
		else 
		if (attack1)
			playerAction = ATTACK_1;
		else
		if (attack2)
			playerAction = ATTACK_2;
		else
		if (attack3)
			playerAction = ATTACK_3;
		else
		if (specialAttack)
			playerAction = SPECIAL_ATTACK;
		else
		if(shout)
			playerAction = SHOUT;
		if(death)
			playerAction = DEATH;

		if (startAni != playerAction)
			resetAniTick();
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		float xSpedd = 0;
		if(death)
			return;
		if(inLeft) {
			if(playerData == WOLF_SAMURAI) {
				flipX = 0;
				flipW = 1;
			}
			else {
			flipX = width;
			flipW = -1;
			}
		}
		if(inRight) {
			if(playerData == WOLF_SAMURAI) {
				flipX = width;
				flipW = -1;
			}
			else {
			flipX = 0;
			flipW = 1;
			}
		}
		if (left) 
			xSpedd -= playerSpeed;
		
		if (right) 
			xSpedd += playerSpeed;

		if(playerAction==SPECIAL_ATTACK&&aniIndex>=6&&(playerData == WOLF_SAMURAI&&aniIndex<10||playerData == SAMURAI_2&&aniIndex<9)) {
			if(inRight)
				xSpedd += 1.5 *playerSpeed;
			else
				xSpedd -= 1.5 *playerSpeed;

			if(!inAir) 
				if(!IsEntityOnFloor(hitbox, lvlData)) {
					inAir = true;		
					}
			if(inAir) {
				if(CanMoveHere(hitbox.x, hitbox.y+airSpeed, hitbox.width, hitbox.height, lvlData)||CheckOnObject(hitbox, lvlData, airSpeed)) {
					hitbox.y += airSpeed;
					airSpeed += gravity;
					updateXPos(xSpedd);
				} else {
					hitbox.y = GetEntityYPosUnderRoofOrAboveFlorr(hitbox, airSpeed, playerData);
					if(airSpeed > 0)
						resetInAir();
					else 
						airSpeed = fallSpeedAfterCollision;
					updateXPos(xSpedd);
				}
			} 
				updateXPos(xSpedd);
		}
		
		if(jump) 
			jump();
		
		if(doubleJump)
			doubleJump();
		if(!inAir && (attacking||shout)&&(!(playerAction==SPECIAL_ATTACK&&5<=aniIndex&&aniIndex<=8&&(playerData == DEMON_SAMURAI))))
			return;
		
		if(!left&&!right&&!inAir)
			return;
		
		if(!inAir) 
			if(!IsEntityOnFloor(hitbox, lvlData)) {
				inAir = true;		
				}
		if(inAir) {
			if(CanMoveHere(hitbox.x, hitbox.y+airSpeed, hitbox.width, hitbox.height, lvlData)||CheckOnObject(hitbox, lvlData, airSpeed)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpedd);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFlorr(hitbox, airSpeed, playerData);
				if(airSpeed > 0)
					resetInAir();
				else 
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpedd);
			}
		} else 
			updateXPos(xSpedd);
		moving = true;

	}

	private void doubleJump() {
		if(!canDoubleJump)
			return;
		airSpeed = jumpSpeed;
		canDoubleJump = false;
	}


	private void jump() {
		if(inAir)
			return;
		canDoubleJump = true;
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpedd) {
		if(CanMoveHere(hitbox.x+xSpedd, hitbox.y, hitbox.width, hitbox.height, lvlData)||CheckNextToObject(hitbox, lvlData, xSpedd)) {
		hitbox.x += xSpedd;
	    } else {
		     hitbox.x = GetEntityXPosNextToWall(hitbox, xSpedd);
	    }
    }

	private void loadAnimations() {
	
			BufferedImage temp = LoadSave.GetSpriteAtlas(playerData.img);

			animations = new BufferedImage[playerData.rowNum][playerData.columnNum];
			for (int j = 0; j < animations.length; j++)
				for (int i = 0; i < animations[j].length; i++)
					animations[j][i] = temp.getSubimage(i * playerData.imgWidth, j * playerData.imgHeight, playerData.imgWidth, playerData.imgHeight);

			temp = LoadSave.GetSpriteAtlas(LoadSave.HEALTH_BAR);
			healthBarAni = new BufferedImage[50];
			for (int i = 0; i < healthBarAni.length; i++) 
				healthBarAni[i] = temp.getSubimage(0, i * 16, 64, 16);
			
			temp = LoadSave.GetSpriteAtlas(LoadSave.HEALTH_REGEN);
			healthRegen = new BufferedImage[13];
			for (int i = 0; i < healthRegen.length; i++) 
				healthRegen[i] = temp.getSubimage(0, i * 16, 64, 16);
			
			temp = LoadSave.GetSpriteAtlas(LoadSave.MANA_REGEN);
			manaRegen = new BufferedImage[40];
			for (int i = 0; i < manaRegen.length; i++)
				manaRegen[i] = temp.getSubimage(0, i * 16, 64, 16);
			
			temp = LoadSave.GetSpriteAtlas(LoadSave.MANA_FADING);
			manaFading = new BufferedImage[40];
			for (int i = 0; i < manaFading.length; i++)
				manaFading[i] = temp.getSubimage(0, i * 16, 64, 16);
			
			temp = LoadSave.GetSpriteAtlas(LoadSave.MANA_SPARKLING);
			manaSparkling = new BufferedImage[10];
			for (int i = 0; i < manaSparkling.length; i++)
				manaSparkling[i] = temp.getSubimage(0, i * 18, 64, 18);
	
	}
	
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true ;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}

	public void setAttack1(boolean attack1) {
		this.attack1 = attack1;
		if(this.attack1)
			attacking = true;
	}
	public void setAttack2(boolean attack2) {
		this.attack2 = attack2;
		if(this.attack2)
			attacking = true;
	}
	public void setAttack3(boolean attack3) {
		this.attack3 = attack3;
		if(this.attack3)
			attacking = true;
	}
	public void setSpecialAttack() {
		if(powerValue == powerMaxValue) {
			powerGrowSpeed = 50;
			specialAttack = true;
			attacking = true;
			changePower(-powerMaxValue);
			}
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
		if(left) {
			inLeft = true;
			inRight = false;
		}
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
		if(right)
			inRight = true;
			inLeft = false;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	public void setDoubleJump(boolean doubleJump) {
		this.doubleJump = doubleJump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;		
	}
	public boolean checkInAir() {
		return inAir;
	}
	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		specialAttack = false;
		attack1 = false;
		attack2 = false;
		attack3 = false;
		moving = false;
		hurt = false;
		death = false;
		flame = false;
		setFlameTime = 0;
		playerAction = IDLE;
		
		currentHealth = maxHealth;
		healthRegenIndex = 0;
		isHealthRegen = true;
		healthBarAniIndex = 0;
		healthBarAniState = 0;
		powerValue = 0;
		previousPowerValue = 0;
		powerBarRegenAniIndex = 0;
		powerBarFadingAniIndex = 0;
		powerBarSparklingIndex = 0;
		isPowerIncrease = false;
		isPowerBarFading = false;
		isPowerBarSparkling = false;
		powerGrowSpeed = 0.5f;
		
		attackChecked = false;
		win = false;
		lose = false;
		hitbox.x = x;
		hitbox.y = y;
		attackBox1.x=x;
		attackBox1.y = y;
		attackBox2.x=x;
		attackBox2.y = y;
		attackBox3.x=x;
		attackBox3.y = y;
		specialAttackBox.x=x;
		specialAttackBox.y=y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}
	public int getHitboxX() {
		return (int) hitbox.x;
	}
	
	public int getHitboxY() {
		return (int) hitbox.y;
	}
	
	public void setHealthBarXStart(int x) {
		this.healthBarXStart = x;
	}
	
	public void setPowerBarXStart(int x) {
		this.powerBarXStart = x;
	}

}
