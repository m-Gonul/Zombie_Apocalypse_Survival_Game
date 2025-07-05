package Bil211Game2.Game.Objects.Ammos;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import Bil211Game2.Game.Entity.Entity;
import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.UtilityTool;

public abstract class Ammo implements Serializable{
    public int speedX = 1;
    public int speedY = 1;
    public int worldX;
    public int worldY;
    public BufferedImage image;
    public int attack = 0;
    public double rad = 0;
    public boolean collisionOn = true;
    public boolean contact = false;
    public int lifespan = 120; 
    public int lifetime = 0; 
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    
    GamePanel gp;

    public Ammo(GamePanel gp){
        this.gp = gp;
        
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
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
            image = ImageIO.read(getClass().getResourceAsStream("/Bil211Game2/Resources/Images/Items/Ammos/" + imagePath + ".png"));
            image = UtilityTool.scaleImage(image, width, height);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return image;
    }

    // Mermiyi çizmek için genel metot
    public void draw(Graphics2D g2) {
        if(image != null) {
        // Ekran konumunu hesapla
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
        // Ekranda görünüyor mu kontrol et
        if(worldX + gp.tileSize/2 > gp.player.worldX - gp.player.screenX 
        && worldX - gp.tileSize/2 < gp.player.worldX + gp.player.screenX
        && worldY + gp.tileSize/2 > gp.player.worldY - gp.player.screenY 
        && worldY - gp.tileSize/2 < gp.player.worldY + gp.player.screenY) {
                
            // Görüntüyü döndürmek için AffineTransform kullan
            AffineTransform at = new AffineTransform();
                
            // Çizim merkezini hesapla
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            screenX -= imageWidth / 2;
            screenY -= imageHeight / 2;
                
            // Dönüş için bileşenleri ayarla
            at.translate(screenX + imageWidth/2, screenY + imageHeight/2);
            at.rotate(rad);
            at.translate(-imageWidth/2, -imageHeight/2);
                
            // Döndürülmüş görüntüyü çiz
            g2.drawImage(image, at, null);
                
            // Debug mod - Çarpışma alanını görselleştirme (isteğe bağlı)
            //g2.setColor(Color.RED);
            //g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            }
        }
    }

    public void update() {
        // Mermiyi hızına göre hareket ettir
        worldX += speedX;
        worldY += speedY;
        
        // Ömür sayacını artır
        lifetime++;
        
        // Ömür süresini kontrol et
        if(lifetime >= lifespan) {
            contact = true;
        }
    }

    // Çarpışma alanını hareket yönüne göre ayarla
    public void setupSolidArea(int width, int height) {
        // Çarpışma alanı boyutlarını ayarla
        solidArea.width = width;
        solidArea.height = height;

        // Varsayılan değerleri kaydet
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void speedHandler(){
        solidArea.x = (image.getWidth() - solidArea.width) / 2;
        solidArea.y = (image.getHeight() - solidArea.height) / 2;

        // Güncellenen değerleri solidAreaDefault değişkenlerine ata!
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void additionalFeature(Entity entity){
    }
}
