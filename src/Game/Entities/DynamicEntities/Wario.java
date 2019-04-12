package Game.Entities.DynamicEntities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Wario extends Player{

	public boolean hit = false;
	public boolean grabbed =false;

	public Wario(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler, Images.warioSmallWalkRight[0]
				,new Animation(175,Images.warioSmallWalkLeft)
				, new Animation(175,Images.warioSmallWalkRight)
				, new Animation(150,Images.warioBigWalkLeft)
				, new Animation(150,Images.warioBigWalkRight)
				, new Animation(115,Images.warioBigRunLeft)
				, new Animation(115,Images.warioBigRunRight));
		if(isBig){
			this.y-=8;
			this.height+=8;
			setDimension(new Dimension(width, this.height));
		}
	}

	@Override
	public void tick(){
	    if(!grabbed) {
            super.tick();
            if (!this.hit) {
                if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_CONTROL) && !handler.getKeyManager().up2 && !handler.getKeyManager().down2) {
                    this.jump();
                }

                if (handler.getKeyManager().right2 && !handler.getKeyManager().up2 && !handler.getKeyManager().down2) {
                    if (handler.getKeyManager().runbutt2) {
                        velX = 6;
                        running = true;
                    } else {
                        velX = 3;
                        running = false;
                    }
                    if (facing.equals("Left")) {
                        changeDirrection = true;
                    }
                    facing = "Right";
                    moving = true;
                } else if (handler.getKeyManager().left2 && !handler.getKeyManager().up2 && !handler.getKeyManager().down2) {
                    if (handler.getKeyManager().runbutt2) {
                        velX = -6;
                        running = true;
                    } else {
                        velX = -3;
                        running = false;
                    }
                    if (facing.equals("Right")) {
                        changeDirrection = true;
                    }
                    facing = "Left";
                    moving = true;
                } else {
                    velX = 0;
                    moving = false;
                }
                if (jumping && velY <= 0) {
                    jumping = false;
                    falling = true;
                } else if (jumping) {
                    velY = velY - gravityAcc;
                    y = (int) (y - velY);
                }

                if (falling) {
                    y = (int) (y + velY);
                    velY = velY + gravityAcc;
                }
                x += velX;
            } else {
                this.setX(this.getX() - 30);
                this.setY(this.getY() - 30);
            }
        }
	}

	public void drawWario(Graphics2D g2) {
		if(!grabbed) {
			if (!isBig) {
				if (handler.getKeyManager().up2) {
					if (facing.equals("Left")) {
						g2.drawImage(Images.warioSmallJumpLeft[2], x, y, width, height, null);
					} else {
						g2.drawImage(Images.warioSmallJumpRight[2], x, y, width, height, null);
					}
				} else if (handler.getKeyManager().down2) {
					if (facing.equals("Left")) {
						g2.drawImage(Images.warioSmallJumpLeft[3], x, y, width, height, null);
					} else {
						g2.drawImage(Images.warioSmallJumpRight[3], x, y, width, height, null);
					}
				} else if (!jumping && !falling) {
					if (facing.equals("Left") && moving) {
						g2.drawImage(playerSmallLeftAnimation.getCurrentFrame(), x, y, width, height, null);
					} else if (facing.equals("Right") && moving) {
						g2.drawImage(playerSmallRightAnimation.getCurrentFrame(), x, y, width, height, null);
					}
					if (facing.equals("Left") && !moving) {
						g2.drawImage(Images.warioSmallWalkLeft[0], x, y, width, height, null);
					} else if (facing.equals("Right") && !moving) {
						g2.drawImage(Images.warioSmallWalkRight[0], x, y, width, height, null);
					}
				} else {
					if (jumping) {
						if (facing.equals("Left")) {
							g2.drawImage(Images.warioSmallJumpLeft[0], x, y, width, height, null);
						} else {
							g2.drawImage(Images.warioSmallJumpRight[0], x, y, width, height, null);
							
						}

					} else {
						if (facing.equals("Left")) {
							g2.drawImage(Images.warioSmallJumpLeft[1], x, y, width, height, null);
						} else {
							g2.drawImage(Images.warioSmallJumpRight[1], x, y, width, height, null);
						}
					}
				}
			} else {
				if (!changeDirrection) {
					if (handler.getKeyManager().up2) {
						if (facing.equals("Left")) {
							g2.drawImage(Images.warioBigJumpLeft[4], x, y, width, height, null);
						} else {
							g2.drawImage(Images.warioBigJumpRight[4], x, y, width, height, null);
						}
					} else if (handler.getKeyManager().down2) {
						if (facing.equals("Left")) {
							g2.drawImage(Images.warioBigJumpLeft[3], x, y, width, height, null);
						} else {
							g2.drawImage(Images.warioBigJumpRight[3], x, y, width, height, null);
						}
					} else if (!jumping && !falling) {
						if (facing.equals("Left") && moving && running) {
							g2.drawImage(playerBigLeftRunAnimation.getCurrentFrame(), x, y, width, height, null);
						} else if (facing.equals("Left") && moving && !running) {
							g2.drawImage(playerBigLeftWalkAnimation.getCurrentFrame(), x, y, width, height, null);
						} else if (facing.equals("Left") && !moving) {
							g2.drawImage(Images.warioBigWalkLeft[0], x, y, width, height, null);
						} else if (facing.equals("Right") && moving && running) {
							g2.drawImage(playerBigRightRunAnimation.getCurrentFrame(), x, y, width, height, null);
						} else if (facing.equals("Right") && moving && !running) {
							g2.drawImage(playerBigRightWalkAnimation.getCurrentFrame(), x, y, width, height, null);
						} else if (facing.equals("Right") && !moving) {
							g2.drawImage(Images.warioBigWalkRight[0], x, y, width, height, null);
						}
					} else {
						if (jumping) {
							if (facing.equals("Left")) {
								g2.drawImage(Images.warioBigJumpLeft[0], x, y, width, height, null);
							} else {
								g2.drawImage(Images.warioBigJumpRight[0], x, y, width, height, null);
							}

						} else {
							if (facing.equals("Left")) {
								g2.drawImage(Images.warioBigJumpLeft[1], x, y, width, height, null);
							} else {
								g2.drawImage(Images.warioBigJumpRight[1], x, y, width, height, null);
							}
						}
					}
				} else {
					if (!running) {
						changeDirrection = false;
						changeDirectionCounter = 0;
						drawWario(g2);
					}
					if (facing.equals("Right")) {
						g2.drawImage(Images.warioBigJumpRight[4], x, y, width, height, null);
					} else {
						g2.drawImage(Images.warioBigJumpLeft[4], x, y, width, height, null);
					}
				}
			}
		}
	}
	public boolean getHit() {
		return this.hit;
	}
	public void setHit(Boolean hit) {
		this.hit = hit;
	}
}
