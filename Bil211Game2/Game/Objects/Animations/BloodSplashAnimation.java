package Bil211Game2.Game.Objects.Animations;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.UtilityTool;

public class BloodSplashAnimation extends Animation {
    
    public BloodSplashAnimation(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, gp.tileSize); // Kan sıçraması boyutu
        
        lifespan = 5; // 5 karelik animasyon
        delay = 3; // Her 3 update döngüsünde bir kare değişecek
        frames = new BufferedImage[lifespan];
        
        // Kan sıçrama karelerini yükle
        loadAnimationFrames();
    }
    
    private void loadAnimationFrames() {
        // Mevcut explosion animasyonunu kan efekti için kullanabiliriz 
        // Daha sonra özel kan görselleri eklenebilir
        frames[0] = setUpImage("Zombie-Tileset---_0379_Capa-380");
        frames[1] = setUpImage("Zombie-Tileset---_0380_Capa-381");
        frames[2] = setUpImage("Zombie-Tileset---_0381_Capa-382");
        frames[3] = setUpImage("Zombie-Tileset---_0382_Capa-383");
        frames[4] = setUpImage("Zombie-Tileset---_0383_Capa-384");
    }
    
    @Override
    public void draw(Graphics2D g2) {
        // Kan efekti için kırmızı renk tonu ayarla
        if (!finished && currentFrame < frames.length) {
            float alpha = 0.8f;
            
            // Son frame'lerde soluklaştır
            if (currentFrame >= lifespan - 2) {
                alpha = (float) (lifespan - currentFrame) / 2 * 0.8f;
            }
            
            // Orijinal alpha değerini kaydet
            AlphaComposite originalComposite = (AlphaComposite) g2.getComposite();
            
            // Kırmızı renk tonu eklemek için
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            
            // Kırmızı overlay
            Color originalColor = g2.getColor();
            g2.setColor(new Color(180, 0, 0, 100));
            
            // Önce orijinal frame'i çiz
            int screenX = worldX - gp.player.worldX + gp.player.screenX - size/2 + gp.tileSize/2;
            int screenY = worldY - gp.player.worldY + gp.player.screenY - size/2 + gp.tileSize/2;
            g2.drawImage(frames[currentFrame], screenX, screenY, size, size, null);
            
            // Kırmızı overlay ekle
            g2.fillOval(screenX, screenY, size, size);
            
            // Renk ve alpha değerlerini sıfırla
            g2.setColor(originalColor);
            g2.setComposite(originalComposite);
        }
    }

    protected BufferedImage setUpImage(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Bil211Game2/Resources/Images/Items/Blood Animation Frames/" + imagePath + ".png"));
            image = UtilityTool.scaleImage(image, size, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}