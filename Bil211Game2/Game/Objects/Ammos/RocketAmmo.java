package Bil211Game2.Game.Objects.Ammos;

import Bil211Game2.Game.Entity.Entity;
import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.Sound;
import Bil211Game2.Game.Main.UtilityTool;

public class RocketAmmo extends Ammo{

    public static int defaultSpeed = 10;

    public RocketAmmo(GamePanel gp) {
        super(gp);

        solidArea.x = speedX > 0 ? -gp.tileSize/4 : gp.tileSize/4;  
        solidArea.y = speedY > 0 ? -gp.tileSize/4 : gp.tileSize/4;  
        solidArea.width = gp.tileSize/2;
        solidArea.height = gp.tileSize/2;

        // Set default values explicitly
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Set a longer lifespan for the acid projectile
        lifespan = 180;

        getImages();
        setupSolidArea(solidArea.width, solidArea.height);
    }

    public void getImages(){
        image = setUpNew("Rocket", gp.tileSize, gp.tileSize);
    }

    @Override
    public void additionalFeature(Entity entity) {
        gp.animationManager.addExplosion(worldX, worldY);

        for(int i = 0; i < gp.monsters.size(); i++){
            if(gp.monsters.get(i) != null 
            && UtilityTool.distanceCalculator(gp.monsters.get(i).worldX, gp.monsters.get(i).worldY, worldX, worldY) < 3 * gp.tileSize){
                gp.monsters.get(i).shooted(attack);
            }
        }    

        gp.startScreenShake(25, 30);
        gp.playSE(Sound.ROCKET_BANG_SOUND);
    }
}
