package Bil211Game2.Game.Entity;

import java.util.Random;

import Bil211Game2.Game.Main.GamePanel;

public class ReptileZombie extends Zombie implements Jumpable{

    boolean canJump = true;
    int jumpCounter = 0;
    int defaultSpeed;
    boolean isJumping = false;
    boolean jumpFlag = false;  // Sınıf üyesi olarak tanımlandı
    Random random = new Random();

    public ReptileZombie(GamePanel gp) {
        super(gp);

        type = 2;
        name = "Reptile Zombie";
        speed = 4;
        maxLife = 2;
        life = maxLife;
        baseScore = 3;
        attack = 1;
    
        solidArea.x = 3;
        solidArea.y = 10;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        defaultSpeed = speed;

        lootChance = 40;
        rifleAmmoChance = 80;
        shotgunAmmoChance = 20;

        getImage();
    }

    public void getImage(){
        up1 = setUpNew("Zombies/Kid Zombie Animation Frames/Up2", gp.tileSize, gp.tileSize);
        up2 = setUpNew("Zombies/Kid Zombie Animation Frames/Up3", gp.tileSize, gp.tileSize);
        down1 = setUpNew("Zombies/Kid Zombie Animation Frames/Down2", gp.tileSize, gp.tileSize);
        down2 = setUpNew("Zombies/Kid Zombie Animation Frames/Down3", gp.tileSize, gp.tileSize);
        left1 = setUpNew("Zombies/Kid Zombie Animation Frames/Left2", gp.tileSize, gp.tileSize);
        left2 = setUpNew("Zombies/Kid Zombie Animation Frames/Left3", gp.tileSize, gp.tileSize);
        right1 = setUpNew("Zombies/Kid Zombie Animation Frames/Right2", gp.tileSize, gp.tileSize);
        right2 = setUpNew("Zombies/Kid Zombie Animation Frames/Right3", gp.tileSize, gp.tileSize);
    }

    public void setAction(){
        actionLockCounter++;
        
        if(actionLockCounter > 60){
            jumpFlag = random.nextBoolean();
            
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
        
        int xDistance = Math.abs(gp.player.worldX - worldX);
        int yDistance = Math.abs(gp.player.worldY - worldY);
        int totalDistance = (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);

        if(isJumping) {
            jumpCounter++;
            
            if(jumpCounter > 5) {
                speed = defaultSpeed;
                isJumping = false;
            }
        }
        else {
            if(!canJump) {
                jumpCounter++;
                if(jumpCounter > 300) {
                    canJump = true;
                    jumpCounter = 0;
                }
            }
            else if(totalDistance < gp.tileSize * 5 && canJump && jumpFlag) {
                speed = defaultSpeed * 5;
                isJumping = true;
                canJump = false;
                jumpCounter = 0;
                jumpFlag = false;
            }
        }
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

    @Override
    public void jump() {
        // Jumpable arayüzünden gelen metot, gerekirse içeriği doldurulabilir
    }

    @Override
    public void kill() {
        gp.player.score += (gp.currentPhase+1)*baseScore;
        gp.animationManager.addBloodSplash(worldX, worldY);
        looting();
    }
}