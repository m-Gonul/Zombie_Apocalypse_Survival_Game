package Bil211Game2.Game.Objects.Ammos;

import Bil211Game2.Game.Main.GamePanel;

public class Acid extends Ammo{

    public static int defaultSpeed = 10;

    public Acid(GamePanel gp) {
        super(gp);

        solidArea.x = speedX > 0 ? -gp.tileSize/4 : gp.tileSize/4;  
        solidArea.y = speedY > 0 ? -gp.tileSize/4 : gp.tileSize/4;  
        solidArea.width = gp.tileSize/2;
        solidArea.height = gp.tileSize/2;

        // Set default values explicitly
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImages();
        setupSolidArea(solidArea.width, solidArea.height);
    }

    public void getImages(){
        image = setUpNew("Turret Zombie Vomit Shooting Animation Frames/Zombie-Tileset---_0470_Capa-471", gp.tileSize/2, gp.tileSize/2);
    }
}
