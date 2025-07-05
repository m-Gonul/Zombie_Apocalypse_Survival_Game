package Bil211Game2.Game.Objects.Weapons;

import java.util.Random;

import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.Sound;
import Bil211Game2.Game.Objects.Ammos.RifleAmmo;

public class Rifle extends Weapon{

    Random random;

    public Rifle(GamePanel gp) {
        super(gp);
        attack = 2;
        random = new Random();
        magazineSize = 30;
        magazineAmmo = magazineSize;
        fireRate = 600;
        ammo = 2*magazineSize;
    }

    @Override
    public void fire(int x, int y) {
        if(magazineAmmo <= 0) return;

        RifleAmmo pistolAmmo = new RifleAmmo(gp);
        int playerX = gp.player.worldX;
        int playerY = gp.player.worldY;

        int sapma = random.nextInt(15);
        boolean flag = random.nextBoolean();

        if(flag){
            sapma = (-1)*sapma;
        }
    
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
        pistolAmmo.speedY = (int)(RifleAmmo.defaultSpeed*Math.sin(pistolAmmo.rad + Math.toRadians(sapma)));
        pistolAmmo.speedX = (int)(RifleAmmo.defaultSpeed*Math.cos(pistolAmmo.rad + Math.toRadians(sapma)));
        
        // Çarpışma alanını hareket yönüne göre ayarla - genel metodu kullan
        pistolAmmo.setupSolidArea(gp.tileSize/2, gp.tileSize/2);
        pistolAmmo.speedHandler();

        // Shoot
        gp.ammos.add(pistolAmmo);

        magazineAmmo--;

        gp.playSE(Sound.RIFLE_SOUND);
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
