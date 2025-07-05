// Asit sıçrama animasyonu
package Bil211Game2.Game.Objects.Animations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Bil211Game2.Game.Main.GamePanel;

public class AcidSplashAnimation extends Animation {
    
    public AcidSplashAnimation(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, gp.tileSize * 5); // Asit sıçraması daha geniş alana etki eder
        
        lifespan = 8; // 8 karelik animasyon
        delay = 2; // Her 2 update döngüsünde bir kare değişecek
        frames = new BufferedImage[lifespan];
        
        // Asit sıçrama karelerini yükle
        loadAnimationFrames();
    }
    
    private void loadAnimationFrames() {
        frames[0] = setUpImage("Zombie-Tileset---_0358_Capa-359");
        frames[1] = setUpImage("Zombie-Tileset---_0359_Capa-360");
        frames[2] = setUpImage("Zombie-Tileset---_0360_Capa-361");
        frames[3] = setUpImage("Zombie-Tileset---_0361_Capa-362");
        frames[4] = setUpImage("Zombie-Tileset---_0362_Capa-363");
        frames[5] = setUpImage("Zombie-Tileset---_0363_Capa-364");
    }
    
    @Override
    public void draw(Graphics2D g2) {
        // Asit sıçrama efekti için renk ve alpha değerini ayarla
        if (!finished && currentFrame < frames.length) {
            float alpha = 0.8f;
            
            // Son üç karede saydamlaştır
            if (currentFrame >= lifespan - 3) {
                alpha = (float) (lifespan - currentFrame) / 3 * 0.8f;
            }
            
            // Yeşil renk tonu eklemek için
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            
            // Orijinal görüntüyü çiz
            super.draw(g2);
            
            // Alpha değerini sıfırla
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
}