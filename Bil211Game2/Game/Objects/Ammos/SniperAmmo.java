package Bil211Game2.Game.Objects.Ammos;

import Bil211Game2.Game.Main.GamePanel;

public class SniperAmmo extends Ammo{

    public static int defaultSpeed = 20;

    public SniperAmmo(GamePanel gp) {
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
        image = setUpNew("sniper", gp.tileSize/4, gp.tileSize/2);
    }
}
