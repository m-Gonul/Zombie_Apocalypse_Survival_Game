package Bil211Game2.Game.Objects.Animations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Bil211Game2.Game.Main.GamePanel;

public class ExplosionAnimation extends Animation {
    
    public ExplosionAnimation(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, gp.tileSize * 3); // Patlama boyutu normal tile'ın 3 katı
        
        lifespan = 6; // 6 karelik animasyon
        delay = 3; // Her 3 update döngüsünde bir kare değişecek
        frames = new BufferedImage[lifespan];
        
        // Patlama karelerini yükle
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
        // Patlama efekti için alpha değerini ayarla (sona doğru saydamlaşsın)
        if (!finished && currentFrame < frames.length) {
            float alpha = 1.0f;
            
            // Son iki karede saydamlaştır
            if (currentFrame >= lifespan - 2) {
                alpha = (float) (lifespan - currentFrame) / 2;
            }
            
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.draw(g2);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
}