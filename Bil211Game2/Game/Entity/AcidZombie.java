package Bil211Game2.Game.Entity;

import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.Sound;
import Bil211Game2.Game.Objects.Ammos.Acid;

public class AcidZombie extends Zombie implements Acidity{

    boolean canShoot = true;
    int shootCounter = 0;
    int defaultSpeed;
    int shootCooldown = 120;

    public AcidZombie(GamePanel gp) {
        super(gp);

        type = 2;
        name = "Acid Zombie";
        speed = 2;
        maxLife = 2;
        life = maxLife;
        baseScore = 7;
        attack = 1;
    
        solidArea.x = 3;
        solidArea.y = 10;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        defaultSpeed = speed;

        lootChance = 60;
        rifleAmmoChance = 40;
        shotgunAmmoChance = 40;
        sniperAmmoChance = 10;
        rocketAmmoChance = 10;

        getImage();
    }

    public void getImage(){
        up1 = setUpNew("Zombies/Turret Zombie Animation Frames/Up2", gp.tileSize, gp.tileSize);
        up2 = setUpNew("Zombies/Turret Zombie Animation Frames/Up3", gp.tileSize, gp.tileSize);
        down1 = setUpNew("Zombies/Turret Zombie Animation Frames/Down2", gp.tileSize, gp.tileSize);
        down2 = setUpNew("Zombies/Turret Zombie Animation Frames/Down3", gp.tileSize, gp.tileSize);
        left1 = setUpNew("Zombies/Turret Zombie Animation Frames/Left2", gp.tileSize, gp.tileSize);
        left2 = setUpNew("Zombies/Turret Zombie Animation Frames/Left3", gp.tileSize, gp.tileSize);
        right1 = setUpNew("Zombies/Turret Zombie Animation Frames/Right2", gp.tileSize, gp.tileSize);
        right2 = setUpNew("Zombies/Turret Zombie Animation Frames/Right3", gp.tileSize, gp.tileSize);
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

        if(!canShoot) {
            shootCounter++;
            if(shootCounter >= shootCooldown) {
                canShoot = true;
                shootCounter = 0;
            }
        }
        else{
            int xDistance = Math.abs(gp.player.worldX - worldX);
            int yDistance = Math.abs(gp.player.worldY - worldY);
            int totalDistance = (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
            
            // 8 tile menzil içindeyse ateş et
            if(totalDistance < 8 * gp.tileSize) {
                rangeAttack();
                canShoot = false;
                shootCounter = 0;
            }
        }
    }

    public void rangeAttack(){
        Acid acid = shootAmmo();
        if(acid != null) {
            gp.ammos.add(acid);
        }
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

    public Acid shootAmmo(){
        Acid acid = new Acid(gp);
        int playerX = gp.player.worldX;
        int playerY = gp.player.worldY;
    
        // Mermi başlangıç pozisyonunu hesapla
        int ammoX, ammoY;
    
        // Yöne bağlı olarak mermi çıkış noktasını ayarla
        switch(direction) {
            case "up":
                ammoX = worldX + gp.tileSize/2;
                ammoY = worldY - gp.tileSize/2;
                break;
            case "down":
                ammoX = worldX + gp.tileSize/2;
                ammoY = worldY + gp.tileSize;
                break;
            case "left":
                ammoX = worldX - gp.tileSize/2;
                ammoY = worldY + gp.tileSize/2;
                break;
            case "right":
                ammoX = worldX + gp.tileSize;
                ammoY = worldY + gp.tileSize/2;
                break;
            default:
                ammoX = worldX + gp.tileSize/2;
                ammoY = worldY + gp.tileSize/2;
                break;
        }
    
        acid.worldX = ammoX;
        acid.worldY = ammoY;
    
        // Hasarı ayarla
        acid.attack = attack;
    
        // Oyuncuya doğru açıyı hesapla
        acid.rad = Math.atan2(playerY - ammoY, playerX - ammoX);
    
        // Açıya göre hızı hesapla
        acid.speedY = (int)(Acid.defaultSpeed*Math.sin(acid.rad));
        acid.speedX = (int)(Acid.defaultSpeed*Math.cos(acid.rad));
        
        // Çarpışma alanını hareket yönüne göre ayarla - genel metodu kullan
        acid.setupSolidArea(gp.tileSize/2, gp.tileSize/2);
        acid.speedHandler();

        gp.playSE(Sound.ACID_ZOMBIE_ACID_SOUND);
    
        return acid;
    }

    @Override
    public void splash() {
        gp.animationManager.addAcidSplash(worldX, worldY);

        for(int i = 0; i < gp.monsters.size(); i++){
            if(gp.monsters.get(i) != null && !gp.monsters.get(i).invicible){
                Monster monster = gp.monsters.get(i);
                int monsterX = monster.worldX;
                int monsterY = monster.worldY;
                double distances = Math.sqrt((monsterX - worldX) * (monsterX - worldX) + (monsterY - worldY) * (monsterY - worldY));
                if(distances < 3 * gp.tileSize){
                    monster.damageReaction();
                    monster.invicible = true;
                    monster.life -= attack;
                }
            }
        }

        if(gp.player != null && !gp.player.invicible){
            int playerX = gp.player.worldX;
            int playerY = gp.player.worldY;
            double distances = Math.sqrt((playerX - worldX) * (playerX - worldX) + (playerY - worldY) * (playerY - worldY));
            if(distances < 3 * gp.tileSize){
                gp.player.invicible = true;
                gp.player.life -= attack;
            }
        }
    }

    @Override
    public void kill() {
        gp.player.score += (gp.currentPhase+1)*baseScore;
        splash();
        gp.animationManager.addBloodSplash(worldX, worldY);
        gp.startScreenShake(20, 20);
        looting();
    }    
}
