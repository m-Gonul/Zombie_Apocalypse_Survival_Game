package Bil211Game2.Game.Objects.Weapons;

import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.Sound;
import Bil211Game2.Game.Objects.Ammos.PistolAmmo;

public class Pistol extends Weapon{

    public Pistol(GamePanel gp) {
        super(gp);
        attack = 1;
        magazineSize = 12;
        magazineAmmo = magazineSize;
        fireRate = 120;
    }

    @Override
    public void fire(int x, int y) {
        if(magazineAmmo <= 0) return;
        
        PistolAmmo pistolAmmo = new PistolAmmo(gp);
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
        pistolAmmo.speedY = (int)(PistolAmmo.defaultSpeed*Math.sin(pistolAmmo.rad));
        pistolAmmo.speedX = (int)(PistolAmmo.defaultSpeed*Math.cos(pistolAmmo.rad));
        
        // Çarpışma alanını hareket yönüne göre ayarla - genel metodu kullan
        pistolAmmo.setupSolidArea(gp.tileSize/2, gp.tileSize/2);
        pistolAmmo.speedHandler();

        // Shoot
        gp.ammos.add(pistolAmmo);

        magazineAmmo--;

        gp.playSE(Sound.PISTOL_SOUND);
    }

    @Override
    public void reload() {
        magazineAmmo = magazineSize;
    }   
}
