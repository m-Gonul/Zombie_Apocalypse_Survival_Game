package Bil211Game2.Game.Entity;

import java.util.Random;

import Bil211Game2.Game.Main.GamePanel;

public abstract class Monster extends Entity{

    public int baseScore = 1;
    public int attack = 0;

    public int lootChance = 0;
    public int rifleAmmoChance = 0;
    public int shotgunAmmoChance = 0;
    public int sniperAmmoChance = 0;
    public int rocketAmmoChance = 0;

    public Monster(GamePanel gp2) {
        super(gp2);
    }

    public abstract void kill();

    @Override
    public void update() {
        if(life <= 0){
            kill();
            dying = true;
            return;
        }
        super.update();
    }
    
    public String lootAmmoType(){
        Random random = new Random();
        int lootFalled = random.nextInt(101);
        boolean falled = false;
        if(100 - lootFalled > lootChance){
            falled = true;
        }

        if(falled){
            int ammoType = random.nextInt(101);
            if(ammoType < rifleAmmoChance){
                return "rifle";
            }
            else if(ammoType < rifleAmmoChance + shotgunAmmoChance){
                return "shotgun";
            }
            else if(ammoType < rifleAmmoChance + shotgunAmmoChance + sniperAmmoChance){
                return "sniper";
            }
            else{
                return "rocket";
            }
        }

        return "null";
    }

    public void looting(){
        String loot = lootAmmoType();
        
        switch(loot){
            case "rifle":
                gp.player.weapons[1].ammo += 5;
                break;
            case "shotgun":
                gp.player.weapons[2].ammo += 5;
                break;
            case "sniper":
                gp.player.weapons[3].ammo += 5;
                break;
            case "rocket":
                gp.player.weapons[4].ammo += 5;
                break;
            default:
                break;
        }
    }
}
