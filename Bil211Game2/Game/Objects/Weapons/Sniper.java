package Bil211Game2.Game.Objects.Weapons;

import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.Sound;
import Bil211Game2.Game.Objects.Ammos.SniperAmmo;

public class Sniper extends Weapon{

    public Sniper(GamePanel gp) {
        super(gp);
        attack = 5;
        magazineSize = 5;
        magazineAmmo = magazineSize;
        fireRate = 30;
        ammo = 2*magazineSize;
    }

    @Override
    public void fire(int x, int y) {
        if(magazineAmmo <= 0) return;

        SniperAmmo pistolAmmo = new SniperAmmo(gp);
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
        pistolAmmo.speedY = (int)(SniperAmmo.defaultSpeed*Math.sin(pistolAmmo.rad));
        pistolAmmo.speedX = (int)(SniperAmmo.defaultSpeed*Math.cos(pistolAmmo.rad));
        
        // Çarpışma alanını hareket yönüne göre ayarla - genel metodu kullan
        pistolAmmo.setupSolidArea(gp.tileSize/2, gp.tileSize/2);
        pistolAmmo.speedHandler();

        // Shoot
        gp.ammos.add(pistolAmmo);

        magazineAmmo--;

        gp.playSE(Sound.SNIPER_SOUND);
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
