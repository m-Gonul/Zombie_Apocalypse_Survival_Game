package Bil211Game2.Game.Objects.Weapons;

import java.io.Serializable;

import Bil211Game2.Game.Main.GamePanel;

public abstract class Weapon implements Serializable{

    public int ammo = 0;
    int attack = 0;
    public int magazineSize = 0;
    public int magazineAmmo = 0;
    public int fireRate = 0;
    GamePanel gp;

    public Weapon(GamePanel gp){
        this.gp = gp;
    }

    public abstract void fire(int x, int y);

    public abstract void reload();
}
