package Bil211Game2.Game.Entity;

import Bil211Game2.Game.Main.GamePanel;

public class TankZombie extends Zombie{

    public TankZombie(GamePanel gp) {
        super(gp);

        type = 2;
        name = "Tank Zombie";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        baseScore = 5;
        attack = 2;
        attackEntity = 2;
    
        solidArea.x = 3;
        solidArea.y = 10;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        lootChance = 50;
        rifleAmmoChance = 60;
        shotgunAmmoChance = 30;
        sniperAmmoChance = 10;

        getImage();
    }

    public void getImage(){
        up1 = setUpNew("Zombies/Big Zombie Walking Animation Frames/Up2", gp.tileSize, gp.tileSize);
        up2 = setUpNew("Zombies/Big Zombie Walking Animation Frames/Up3", gp.tileSize, gp.tileSize);
        down1 = setUpNew("Zombies/Big Zombie Walking Animation Frames/Down2", gp.tileSize, gp.tileSize);
        down2 = setUpNew("Zombies/Big Zombie Walking Animation Frames/Down3", gp.tileSize, gp.tileSize);
        left1 = setUpNew("Zombies/Big Zombie Walking Animation Frames/Left2", gp.tileSize, gp.tileSize);
        left2 = setUpNew("Zombies/Big Zombie Walking Animation Frames/Left3", gp.tileSize, gp.tileSize);
        right1 = setUpNew("Zombies/Big Zombie Walking Animation Frames/Right2", gp.tileSize, gp.tileSize);
        right2 = setUpNew("Zombies/Big Zombie Walking Animation Frames/Right3", gp.tileSize, gp.tileSize);
    }

    public void setAction(){
        actionLockCounter++;
        if(actionLockCounter > 60){
            boolean flag = false; // TRUE : HORIZONTAL, FALSE : VERTICAL 
            if(Math.abs(gp.player.worldX - worldX) > Math.abs(gp.player.worldY - worldY)){
                flag = true;
            }

            if(flag){
                if(gp.player.worldX - worldX > 0){
                    direction = "right";
                }
                else{
                    direction = "left";
                }
            }
            else{
                if(gp.player.worldY - worldY > 0){
                    direction = "down";
                }
                else{
                    direction = "up";
                }
            }
            actionLockCounter = 0;
        }
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

    @Override
    public void kill() {
        gp.player.score += (gp.currentPhase+1)*baseScore;
        gp.animationManager.addBloodSplash(worldX, worldY);
        looting();
    }
    
}
