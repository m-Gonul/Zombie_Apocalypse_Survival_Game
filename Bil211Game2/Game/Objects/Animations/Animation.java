package Bil211Game2.Game.Objects.Animations;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.UtilityTool;

public class Animation {
    protected GamePanel gp;
    protected int worldX, worldY;
    protected int lifespan; // Animasyonun toplam kareleri
    protected int currentFrame;
    protected boolean finished;
    protected BufferedImage[] frames;
    protected int delay; // Her kare arasındaki gecikme (kaç update döngüsü)
    protected int delayCounter;
    protected int size; // Animasyon boyutu
    
    public Animation(GamePanel gp, int worldX, int worldY, int size) {
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
        this.size = size;
        currentFrame = 0;
        delayCounter = 0;
        finished = false;
    }
    
    public void update() {
        delayCounter++;
        
        if (delayCounter >= delay) {
            currentFrame++;
            delayCounter = 0;
            
            if (currentFrame >= lifespan) {
                finished = true;
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        if (!finished && currentFrame < frames.length) {
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            // Eğer ekranda görünüyorsa çiz
            if (worldX + size > gp.player.worldX - gp.player.screenX 
                && worldX - size < gp.player.worldX + gp.player.screenX
                && worldY + size > gp.player.worldY - gp.player.screenY 
                && worldY - size < gp.player.worldY + gp.player.screenY) {
                
                // Animasyonun ortada olması için pozisyonu ayarla
                screenX -= size / 2;
                screenY -= size / 2;
                
                g2.drawImage(frames[currentFrame], screenX, screenY, size, size, null);
            }
        }
    }
    
    public boolean isFinished() {
        return finished;
    }
    
    // Yardımcı metot: Görüntüleri ayarlamak için
    protected BufferedImage setUpImage(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Bil211Game2/Resources/Images/Items/Explosion Animation Frames/" + imagePath + ".png"));
            image = UtilityTool.scaleImage(image, size, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}