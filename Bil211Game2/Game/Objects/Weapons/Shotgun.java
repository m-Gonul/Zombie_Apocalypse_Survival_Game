package Bil211Game2.Game.Objects.Weapons;

import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.Sound;
import Bil211Game2.Game.Objects.Ammos.ShotgunAmmo;

public class Shotgun  extends Weapon{

    public Shotgun(GamePanel gp) {
        super(gp);
        attack = 2;
        magazineSize = 5;
        magazineAmmo = magazineSize;
        fireRate = 60;
        ammo = 2*magazineSize;
    }

    @Override
    public void fire(int x, int y) {
        if(magazineAmmo <= 0) return;

        for(int i = 0; i <= 4; i++){
            ShotgunAmmo pistolAmmo = new ShotgunAmmo(gp);
            int playerX = gp.player.worldX;
            int playerY = gp.player.worldY;
        
            // Mermi başlangıç pozisyonunu hesapla
            int ammoX, ammoY;
        
            // Yöne bağlı olarak mermi çıkış noktasını ayarla
            switch(gp.player.direction) {
                case "up":
                    ammoX = playerX + gp.tileSize/2;
                    ammoY = playerY - gp.tileSize/2;
                    break;
                case "down":
                    ammoX = playerX + gp.tileSize/2;
                    ammoY = playerY + gp.tileSize + gp.tileSize/2;
                    break;
                case "left":
                    ammoX = playerX - gp.tileSize/2;
                    ammoY = playerY + gp.tileSize/2;
                    break;
                case "right":
                    ammoX = playerX + gp.tileSize;
                    ammoY = playerY + gp.tileSize/2;
                    break;
                default:
                    ammoX = playerX + gp.tileSize/2;
                    ammoY = playerY + gp.tileSize/2;
                    break;
            }
        
            pistolAmmo.worldX = ammoX;
            pistolAmmo.worldY = ammoY;
        
            // Hasarı ayarla
            pistolAmmo.attack = attack;
        
            // Oyuncuya doğru açıyı hesapla
            pistolAmmo.rad = Math.atan2(y - ammoY, x - ammoX);
        
            // Açıya göre hızı hesapla
            pistolAmmo.speedY = (int)(ShotgunAmmo.defaultSpeed*Math.sin(pistolAmmo.rad - Math.toRadians(5*i)));
            pistolAmmo.speedX = (int)(ShotgunAmmo.defaultSpeed*Math.cos(pistolAmmo.rad - Math.toRadians(5*i)));
            
            // Çarpışma alanını hareket yönüne göre ayarla - genel metodu kullan
            pistolAmmo.setupSolidArea(gp.tileSize/2, gp.tileSize/2);
            pistolAmmo.speedHandler();

            // Shoot
            gp.ammos.add(pistolAmmo);
        }

        for(int i = 1; i <= 4; i++){
            ShotgunAmmo pistolAmmo = new ShotgunAmmo(gp);
            int playerX = gp.player.worldX;
            int playerY = gp.player.worldY;
        
            // Mermi başlangıç pozisyonunu hesapla
            int ammoX, ammoY;
        
            // Yöne bağlı olarak mermi çıkış noktasını ayarla
            switch(gp.player.direction) {
                case "up":
                    ammoX = playerX + gp.tileSize/2;
                    ammoY = playerY - gp.tileSize/2;
                    break;
                case "down":
                    ammoX = playerX + gp.tileSize/2;
                    ammoY = playerY + gp.tileSize + gp.tileSize/2;
                    break;
                case "left":
                    ammoX = playerX - gp.tileSize/2;
                    ammoY = playerY + gp.tileSize/2;
                    break;
                case "right":
                    ammoX = playerX + gp.tileSize;
                    ammoY = playerY + gp.tileSize/2;
                    break;
                default:
                    ammoX = playerX + gp.tileSize/2;
                    ammoY = playerY + gp.tileSize/2;
                    break;
            }
        
            pistolAmmo.worldX = ammoX;
            pistolAmmo.worldY = ammoY;
        
            // Hasarı ayarla
            pistolAmmo.attack = attack;
        
            // Oyuncuya doğru açıyı hesapla
            pistolAmmo.rad = Math.atan2(y - ammoY, x - ammoX);
        
            // Açıya göre hızı hesapla
            pistolAmmo.speedY = (int)(ShotgunAmmo.defaultSpeed*Math.sin(pistolAmmo.rad + Math.toRadians(5*i)));
            pistolAmmo.speedX = (int)(ShotgunAmmo.defaultSpeed*Math.cos(pistolAmmo.rad + Math.toRadians(5*i)));
            
            // Çarpışma alanını hareket yönüne göre ayarla - genel metodu kullan
            pistolAmmo.setupSolidArea(gp.tileSize/2, gp.tileSize/2);
            pistolAmmo.speedHandler();

            // Shoot
            gp.ammos.add(pistolAmmo);
        }

        magazineAmmo--;

        gp.playSE(Sound.SHOTGUN_SOUND);
    }

    @Override
    public void reload() {
        if(ammo >= magazineSize - magazineAmmo){
            ammo -= magazineSize - magazineAmmo;
            magazineAmmo = magazineSize;
        }
        else{
            magazineAmmo += ammo;
            ammo = 0;
        }
    }   
}
