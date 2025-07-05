package Bil211Game2.Game.Objects.Others;

import Bil211Game2.Game.Entity.Entity;
import Bil211Game2.Game.Main.GamePanel;

public class OBJ_Hearth extends Entity{
    public OBJ_Hearth(GamePanel gp){
        super(gp);
        name = "Heart";
        image = setUpNew("Other/Hearts/heart_full", gp.tileSize, gp.tileSize);
        image2 = setUpNew("Other/Hearts/heart_half", gp.tileSize, gp.tileSize);
        image3 = setUpNew("Other/Hearts/heart_blank", gp.tileSize, gp.tileSize);
    }
}
