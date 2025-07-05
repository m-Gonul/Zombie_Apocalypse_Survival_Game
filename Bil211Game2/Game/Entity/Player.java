package Bil211Game2.Game.Entity;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Bil211Game2.Game.Main.KeyHandler;
import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.MouseHandler;
import Bil211Game2.Game.Main.Sound;
import Bil211Game2.Game.Main.UtilityTool;
import Bil211Game2.Game.Objects.Ammos.Acid;
import Bil211Game2.Game.Objects.Ammos.SniperAmmo;
import Bil211Game2.Game.Objects.Weapons.Pistol;
import Bil211Game2.Game.Objects.Weapons.Rifle;
import Bil211Game2.Game.Objects.Weapons.RocketLauncher;
import Bil211Game2.Game.Objects.Weapons.Shotgun;
import Bil211Game2.Game.Objects.Weapons.Sniper;
import Bil211Game2.Game.Objects.Weapons.Weapon;

public class Player extends Entity{
    KeyHandler keyH;
    MouseHandler mouseH;
    int standCounter = 0;
    public boolean standing = false;
    int attackCounter = 0;
    boolean canAttact = false;
    public int score = 0;

    public final int screenX, screenY;

    public int currentWeaponIndex;
    public Weapon[] weapons = new Weapon[5];

    private Monster closestZombie = null;
    private int zombieDetectionTimer = 0;
    private final int ZOMBIE_DETECTION_DURATION = 180;
    private boolean zombieDetectionActive = false;

    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseH) {
        super(gp);
        this.keyH = keyH;
        this.mouseH = mouseH;
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        // OVERRIDE SOLID AREA
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = 36;
        attackArea.height = 36;

        currentWeaponIndex = 0;
        weapons[0] = new Pistol(gp);
        weapons[1] = new Rifle(gp);
        weapons[2] = new Shotgun(gp);
        weapons[3] = new Sniper(gp);
        weapons[4] = new RocketLauncher(gp);

        setDefaultValues();
        getPlayerImage();
        //getPlayerAttackImage();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // Player Status
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage(){
        up1 = setUpNew("Player/Player Character Walking Animation Frames/Up2", gp.tileSize, gp.tileSize);
        up2 = setUpNew("Player/Player Character Walking Animation Frames/Up3", gp.tileSize, gp.tileSize);
        down1 = setUpNew("Player/Player Character Walking Animation Frames/Down2", gp.tileSize, gp.tileSize);
        down2 = setUpNew("Player/Player Character Walking Animation Frames/Down3", gp.tileSize, gp.tileSize);
        left1 = setUpNew("Player/Player Character Walking Animation Frames/Left2", gp.tileSize, gp.tileSize);
        left2 = setUpNew("Player/Player Character Walking Animation Frames/Left3", gp.tileSize, gp.tileSize);
        right1 = setUpNew("Player/Player Character Walking Animation Frames/Right2", gp.tileSize, gp.tileSize);
        right2 = setUpNew("Player/Player Character Walking Animation Frames/Right3", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage(){
        attackUp1 = setUpNew("Player/Player Character Walking Animation Frames/Up2", gp.tileSize, gp.tileSize);
        attackUp2 = setUpNew("Player/Player Character Walking Animation Frames/Up3", gp.tileSize, gp.tileSize);
        attackDown1 = setUpNew("Player/Player Character Walking Animation Frames/Up3", gp.tileSize, gp.tileSize);
        attackDown2 = setUpNew("Player/Player Character Walking Animation Frames/Up3", gp.tileSize, gp.tileSize);
        attackLeft1 = setUpNew("Player/Player Character Walking Animation Frames/Up3", gp.tileSize, gp.tileSize);
        attackLeft2 = setUpNew("Player/Player Character Walking Animation Frames/Up3", gp.tileSize, gp.tileSize);
        attackRight1 = setUpNew("Player/Player Character Walking Animation Frames/Up3", gp.tileSize, gp.tileSize);
        attackRight2 = setUpNew("Player/Player Character Walking Animation Frames/Up3", gp.tileSize, gp.tileSize);
    }
    
    @Override
    public void update(){

        if(life <= 0){
            gp.gameState = gp.finishState;
            return;
        }

        if(keyH.HPressed && !zombieDetectionActive) {
            findClosestZombie();
            zombieDetectionTimer = 0;
            zombieDetectionActive = true;
        }

        if(zombieDetectionActive) {
            zombieDetectionTimer++;
            if(zombieDetectionTimer > ZOMBIE_DETECTION_DURATION) {
                zombieDetectionActive = false;
                closestZombie = null;
            }
        }

        attackCounter++;
        if(mouseH.mouseLeftPressed){
            if(attackCounter > 60/((double)weapons[currentWeaponIndex].fireRate/60)){
                canAttact = true;
                attackCounter = 0;
            }
            else{
                canAttact = false;
            }
        }
        else{
            canAttact = false;
        }

        if(canAttact){
            attacking(mouseH.event.getX(), mouseH.event.getY());
        }


        if(keyH.upPressed||keyH.downPressed||keyH.leftPressed||keyH.rightPressed){
            if(keyH.upPressed){
                direction = "up";
            }
            else if(keyH.downPressed){
                direction = "down";
            }
            else if(keyH.rightPressed){
                direction = "right";
            }
            else if(keyH.leftPressed){
                direction = "left";
            }
            standing = true;

            collisionOn = false;
            gp.cChecker.checkTile(this);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monsters);
            contactMonster(monsterIndex);

            gp.eHandler.checkEvent();

            for(int i = 0; i < gp.ammos.size(); i++){
                if(gp.ammos.get(i) instanceof Acid && gp.cChecker.checkAmmo(gp.ammos.get(i), this)){
                    life -= gp.ammos.get(i).attack;
                    invicible = true;
                    gp.ammos.set(i, null);
                    gp.ammos.remove(i);
                    gp.startScreenShake(12, 15);
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
        }
        else{
            for(int i = 0; i < gp.ammos.size(); i++){
                if(gp.ammos.get(i) instanceof Acid && gp.cChecker.checkAmmo(gp.ammos.get(i), this)){
                    life -= gp.ammos.get(i).attack;
                    invicible = true;
                    if(!(gp.ammos.get(i) instanceof SniperAmmo)){
                        gp.ammos.set(i, null);
                        gp.ammos.remove(i);
                    }
                    gp.playSE(Sound.PLAYER_HURT);
                }
            }
            standing = false;
            standCounter++;

            if(standCounter == 20){
                spriteNum = 1;
                standCounter = 0;
            }
            
        }

        if(invicible){
            invicibleCounter++;
            if(invicibleCounter > 60){
                invicible = false;
                invicibleCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch(direction){
            case "up":
                if(attacking){
                    tempScreenY = tempScreenY - gp.tileSize;
                    if(spriteNum == 1){
                        image = attackUp1;
                    }
                    if(spriteNum == 2){
                        image = attackUp2;
                    }
                }
                else{
                    if(spriteNum == 1){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                }
                break;
            case "down":
                if(attacking){
                    if(spriteNum == 1){
                        image = attackDown1;
                    }
                    if(spriteNum == 2){
                        image = attackDown2;
                    }
                }
                else{
                    if(spriteNum == 1){
                        image = down1;
                    }
                    if(spriteNum == 2){
                        image = down2;
                    }
                }
                break;
            case "left":
                if(attacking){
                    tempScreenX = tempScreenX - gp.tileSize;
                    if(spriteNum == 1){
                        image = attackLeft1;
                    }
                    if(spriteNum == 2){
                        image = attackLeft2;
                    }
                }
                else{
                    if(spriteNum == 1){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }
                }
                break;
            case "right":
                if(attacking){
                    if(spriteNum == 1){
                        image = attackRight1;
                    }
                    if(spriteNum == 2){
                        image = attackRight2;
                    }
                }
                else{
                    if(spriteNum == 1){
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                }
                break;
        }

        if(invicible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        if(zombieDetectionActive && closestZombie != null) {
            drawDirectionArrow(g2);
        }
    }

    public void contactMonster(int index){
        if(index != 999){
            if(!invicible){
                life--;
                invicible = true;
                gp.startScreenShake(8, 15);
            }
        }
    }

    public void attacking(int x, int y){
        weapons[currentWeaponIndex].fire(worldX + x - gp.screenWidth/2, worldY + y - gp.screenHeight/2);
    }

    public void changeWeapon(int i){
        if(gp.gameState == gp.playState){
            if(i == 0){
                currentWeaponIndex = 0;
            }
            else if(i == 1 && gp.currentPhase > gp.phase1){
                currentWeaponIndex = 1;
            }
            else if(i == 2 && gp.currentPhase > gp.phase4){
                currentWeaponIndex = 2;
            }
            else if(i == 3 && gp.currentPhase > gp.phase6){
                currentWeaponIndex = 3;
            }
            else if(i == 4 && gp.currentPhase > gp.phase10){
                currentWeaponIndex = 4;
            }
        }
    }

    public void damage(int i){
        if(i != 999){
            if(!gp.monsters.get(i).invicible){
                gp.monsters.get(i).life--;
                gp.monsters.get(i).invicible = true;
                gp.monsters.get(i).damageReaction();

                if(gp.monsters.get(i).life <= 0){
                    gp.monsters.get(i).kill();
                    gp.monsters.get(i).dying = true;
                }
            }   
        }
        
    }

    private void findClosestZombie() {
        double closestDistance = Double.MAX_VALUE;
        closestZombie = null;
        
        for(int i = 0; i < gp.monsters.size(); i++) {
            Monster monster = gp.monsters.get(i);
            if(monster != null && monster.alive && !monster.dying) {
                double distance = UtilityTool.distanceCalculator(
                    worldX, worldY, monster.worldX, monster.worldY);
                
                if(distance < closestDistance) {
                    closestDistance = distance;
                    closestZombie = monster;
                }
            }
        }
    }

    public Monster getClosestZombie() {
        return zombieDetectionActive ? closestZombie : null;
    }

    private void drawDirectionArrow(Graphics2D g2) {
        if(closestZombie == null) return;
        
        // Zombi ile oyuncu arasındaki açıyı hesapla
        int zombieX = closestZombie.worldX - gp.player.worldX;
        int zombieY = closestZombie.worldY - gp.player.worldY;
        double angle = Math.atan2(zombieY, zombieX);
        
        // Ok için parametreler
        int arrowLength = gp.tileSize;
        int arrowHeadSize = gp.tileSize / 3;
        
        // Okun başlangıç noktasını hesapla (oyuncunun merkezi)
        int startX = screenX + gp.tileSize / 2;
        int startY = screenY + gp.tileSize / 2;
        
        // Okun bitiş noktasını hesapla
        int endX = startX + (int)(Math.cos(angle) * arrowLength);
        int endY = startY + (int)(Math.sin(angle) * arrowLength);
        
        // Ok çiz
        g2.setColor(new Color(255, 0, 0, 180));
        
        // Ok başı çiz
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        
        xPoints[0] = endX;
        yPoints[0] = endY;
        
        xPoints[1] = endX - (int)(Math.cos(angle + Math.PI/6) * arrowHeadSize);
        yPoints[1] = endY - (int)(Math.sin(angle + Math.PI/6) * arrowHeadSize);
        
        xPoints[2] = endX - (int)(Math.cos(angle - Math.PI/6) * arrowHeadSize);
        yPoints[2] = endY - (int)(Math.sin(angle - Math.PI/6) * arrowHeadSize);
        
        g2.fillPolygon(xPoints, yPoints, 3);
        
        // Etrafında parlayan bir halka çiz (radar efekti)
        float pulseSize = 1.0f + 0.2f * (float)Math.sin(System.currentTimeMillis() * 0.005);
        int ringSize = (int)((gp.tileSize + gp.tileSize/3) * pulseSize);
        
        g2.setColor(new Color(255, 0, 0, 50)); // Çok hafif kırmızı
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(startX - ringSize/2, startY - ringSize/2, ringSize, ringSize);
    }
}
