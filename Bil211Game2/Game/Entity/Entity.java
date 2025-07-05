package Bil211Game2.Game.Entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import Bil211Game2.Game.Main.UtilityTool;
import Bil211Game2.Game.Objects.Ammos.Acid;
import Bil211Game2.Game.Objects.Ammos.SniperAmmo;
import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.Sound;

public abstract class Entity implements Serializable{
    GamePanel gp;

    public int worldX, worldY;
    public int speed;
    public int attackEntity = 1;

    public BufferedImage up1, up2, down1, down2, 
    left1, left2, right1, right2,
    attackUp1, attackUp2, attackDown1, attackDown2,
    attackLeft1, attackLeft2, attackRight1, attackRight2;

    public String direction = "down";

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    public int invicibleCounter = 0;
    public boolean invicible = false;
    public int type;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    int dyingCounter = 0;
    public boolean hpBarOn = false;
    int hpBarCounter = 0;

    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;

    // CHARACTER STATUS
    public int maxLife;
    public int life;

    public Entity(Bil211Game2.Game.Main.GamePanel gp2){
        this.gp = gp2;
    }

    public void draw(Graphics2D g2){
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX 
        && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
        && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY 
        && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
            
            BufferedImage image = null;

            switch(direction){
                case "up":
                    if(spriteNum == 1){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                    break;
                case "down":
                    if(spriteNum == 1){
                        image = down1;
                    }
                    if(spriteNum == 2){
                        image = down2;
                    }
                    break;
                case "left":
                    if(spriteNum == 1){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }
                    break;
                case "right":
                    if(spriteNum == 1){
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                    break;
            }

            if(type == 2 && hpBarOn){
                hpBarCounter++;
                if(hpBarCounter < 120){
                    g2.setColor(new Color(35,35,35));
                    g2.fillRect(screenX - 1, screenY-17, gp.tileSize + 2, 13);
                    g2.setColor(new Color(255,0,30));
                    g2.fillRect(screenX, screenY - 15, (int)(gp.tileSize *((double)life/maxLife)), 10);
                }
                else{
                    hpBarOn = false;
                    hpBarCounter = 0;
                }
            }

            if(invicible){
                hpBarOn = true;
                changeAlphe(g2, 0.5f);
            }
            if(dying){
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

            changeAlphe(g2, 1f);
        }
    }

    public void setAction(){};

    public void dyingAnimation(Graphics2D g2){
        int i = 5;

        if(dyingCounter <= i){
            changeAlphe(g2, 0f);
        }
        else if(dyingCounter <= i*2){
            changeAlphe(g2, 1f);
        }
        else if(dyingCounter <= i*3){
            changeAlphe(g2, 0f);
        }
        else if(dyingCounter <= i*4){
            changeAlphe(g2, 1f);
        }
        else if(dyingCounter <= i*5){
            changeAlphe(g2, 0f);
        }
        else if(dyingCounter <= i*6){
            changeAlphe(g2, 1f);
        }
        else if(dyingCounter <= i*7){
            changeAlphe(g2, 0f);
        }
        else if(dyingCounter <= i*8){
            changeAlphe(g2, 1f);
        }
    }

    public void changeAlphe(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public  void update(){
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);
        gp.cChecker.checkEntity(this, gp.monsters);

        for(int i = 0; i < gp.ammos.size(); i++){
            if(!(gp.ammos.get(i) instanceof Acid) && gp.cChecker.checkAmmo(gp.ammos.get(i), this)){
                gp.ammos.get(i).additionalFeature(this);
                shooted(gp.ammos.get(i).attack);
                if(!(gp.ammos.get(i) instanceof SniperAmmo)){
                    gp.ammos.set(i, null);
                    gp.ammos.remove(i);
                }
                gp.playSE(Sound.ZOMBIE_HURT);
            }
        }

        if(this.type == 2 && contactPlayer){
            if(!gp.player.invicible){
                gp.player.life -= attackEntity;
                gp.player.invicible = true;
                gp.startScreenShake(8, 15);
                gp.playSE(Sound.PLAYER_HURT);
            }
        }

        if(!collisionOn){
            switch(direction){
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }

        spriteCounter++;
        if(spriteCounter > 12){
            if(spriteNum == 1){
                spriteNum = 2;
            }
            else if(spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if(invicible){
            invicibleCounter++;
            if(invicibleCounter > 40){
                invicible = false;
                invicibleCounter = 0;
            }
        }
    }

    public BufferedImage setUp(String imagePath, int width, int height){
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/Bil211Game/Resources/" + imagePath + ".png"));
            image = UtilityTool.scaleImage(image, width, height);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return image;
    }

    public BufferedImage setUpNew(String imagePath, int width, int height){
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/Bil211Game2/Resources/Images/" + imagePath + ".png"));
            image = UtilityTool.scaleImage(image, width, height);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return image;
    }

    public void damageReaction(){}

    public void shooted(int damage){
        invicible = true;
        life -= damage;
        damageReaction();
    }

    public void updateDying() {
        dyingCounter++;
        int i = 5;
        
        if(dyingCounter > i*8) {
            dying = false;
            alive = false;
        }
    }
}
