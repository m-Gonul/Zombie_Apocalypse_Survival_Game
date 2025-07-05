package Bil211Game2.Game.Objects.Animations;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Bil211Game2.Game.Main.GamePanel;

public class AnimationManager {
    private GamePanel gp;
    private List<Animation> animations;
    
    public AnimationManager(GamePanel gp) {
        this.gp = gp;
        animations = new ArrayList<>();
    }
    
    public void addExplosion(int worldX, int worldY) {
        animations.add(new ExplosionAnimation(gp, worldX, worldY));
    }
    
    public void addAcidSplash(int worldX, int worldY) {
        animations.add(new AcidSplashAnimation(gp, worldX, worldY));
    }

    public void addBloodSplash(int worldX, int worldY) {
        animations.add(new BloodSplashAnimation(gp, worldX, worldY));
    }
    
    public void update() {
        Iterator<Animation> iterator = animations.iterator();
        while (iterator.hasNext()) {
            Animation animation = iterator.next();
            animation.update();
            
            if (animation.isFinished()) {
                iterator.remove();
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        for (Animation animation : animations) {
            animation.draw(g2);
        }
    }
}