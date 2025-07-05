package Bil211Game2.Game.Main;

import java.util.Random;

import Bil211Game2.Game.Entity.AcidZombie;
import Bil211Game2.Game.Entity.Monster;
import Bil211Game2.Game.Entity.NormalZombie;
import Bil211Game2.Game.Entity.ReptileZombie;
import Bil211Game2.Game.Entity.TankZombie;

public class AssetSetter{

    GamePanel gp;
    int normalZombieChance = 100;
    int reptileZombieChance = 0;
    int tankZombieChance = 0;
    int acidZombieChance = 0;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setMonster(){
        
        gp.monsters.add(0, new NormalZombie(gp));
        gp.monsters.get(0).worldX = gp.tileSize*23;
        gp.monsters.get(0).worldY = gp.tileSize*36;

        gp.monsters.add(1, new NormalZombie(gp));
        gp.monsters.get(1).worldX = gp.tileSize*27;
        gp.monsters.get(1).worldY = gp.tileSize*20;

        gp.monsters.add(2, new NormalZombie(gp));
        gp.monsters.get(2).worldX = gp.tileSize*29;
        gp.monsters.get(2).worldY = gp.tileSize*20;

        gp.monsters.add(3, new NormalZombie(gp));
        gp.monsters.get(3).worldX = gp.tileSize*25;
        gp.monsters.get(3).worldY = gp.tileSize*20;

        gp.monsters.add(4, new ReptileZombie(gp));
        gp.monsters.get(4).worldX = gp.tileSize*27;
        gp.monsters.get(4).worldY = gp.tileSize*23;

        gp.monsters.add(5, new ReptileZombie(gp));
        gp.monsters.get(5).worldX = gp.tileSize*27;
        gp.monsters.get(5).worldY = gp.tileSize*26;
        
        gp.monsters.add(6, new TankZombie(gp));
        gp.monsters.get(6).worldX = gp.tileSize*27;
        gp.monsters.get(6).worldY = gp.tileSize*17;

        gp.monsters.add(7, new TankZombie(gp));
        gp.monsters.get(7).worldX = gp.tileSize*15;
        gp.monsters.get(7).worldY = gp.tileSize*14;

        gp.monsters.add(8, new TankZombie(gp));
        gp.monsters.get(8).worldX = gp.tileSize*18;
        gp.monsters.get(8).worldY = gp.tileSize*18;

        gp.monsters.add(9, new TankZombie(gp));
        gp.monsters.get(9).worldX = gp.tileSize*30;
        gp.monsters.get(9).worldY = gp.tileSize*30;

        gp.monsters.add(10, new AcidZombie(gp));
        gp.monsters.get(10).worldX = gp.tileSize*31;
        gp.monsters.get(10).worldY = gp.tileSize*31;

        gp.monsters.add(11, new AcidZombie(gp));
        gp.monsters.get(11).worldX = gp.tileSize*40;
        gp.monsters.get(11).worldY = gp.tileSize*40;
    }

    public void setMonsterByPhase(){ 
        if(gp.gameState == gp.playState){
            Random random = new Random();

            if(gp.currentPhase == gp.phase1 || gp.currentPhase == gp.phase2){
                normalZombieChance -= 10;
                reptileZombieChance += 10;
            }
            else if(gp.currentPhase == gp.phase3 || gp.currentPhase == gp.phase4){
                normalZombieChance -= 10;
                tankZombieChance += 10;
            }
            else if(gp.currentPhase == gp.phase5 || gp.currentPhase == gp.phase6){
                normalZombieChance -= 5;
                acidZombieChance += 5;
            }
            else if(gp.currentPhase == gp.phase7 || gp.currentPhase == gp.phase8){
                normalZombieChance -= 10;
                reptileZombieChance += 3;
                tankZombieChance += 3;
                acidZombieChance += 4;
            }

            for(int i = 0; i < (gp.currentPhase+1)*3; i++){
                int chance = random.nextInt(101);
                if(chance < normalZombieChance){
                    NormalZombie normalZombie = new NormalZombie(gp);
                    MonsterCreator(normalZombie);
                }
                else if(chance < normalZombieChance + reptileZombieChance){
                    ReptileZombie reptileZombie = new ReptileZombie(gp);
                    MonsterCreator(reptileZombie);
                }
                else if(chance < normalZombieChance + reptileZombieChance + tankZombieChance){
                    TankZombie tankZombie = new TankZombie(gp);
                    MonsterCreator(tankZombie);
                }
                else{
                    AcidZombie acidZombie = new AcidZombie(gp);
                    MonsterCreator(acidZombie);
                }
            }
            gp.phaseFinished = false;
        }
    }

    public void MonsterCreator(Monster monster){
        if(gp.gameState == gp.playState){
            int x = 0; 
            int y = 0;
            Random random = new Random();

            boolean validPosition = false;
        
            while(!validPosition){
                x = random.nextInt(gp.maxWorldCol);
                y = random.nextInt(gp.maxWorldRow);
                
                // Harita sınırları içinde mi kontrolü
                // Çarpışma OLMAYAN bir karo mu?
                if(!gp.tileM.tiles[gp.tileM.mapTileNum[x][y]].collision){
                        
                    double distance = UtilityTool.distanceCalculator(x*gp.tileSize, y*gp.tileSize, gp.player.worldX, gp.player.worldY);
                    if(distance > 3*gp.tileSize){  // Oyuncudan en az 3 karo uzakta olsun
                        boolean positionFree = true;
                            
                        // Mevcut monster listesi üzerinde güvenli yineleme
                        for(int i = 0; i < gp.monsters.size(); i++){
                            Monster monsterTemp = gp.monsters.get(i);
                            if(monsterTemp != null && monsterTemp.worldX/gp.tileSize == x && monsterTemp.worldY/gp.tileSize == y){
                                positionFree = false;
                                break;
                            }
                        }
                            
                        if(positionFree){
                            validPosition = true;
                        }
                    }
                }
                
            }

            monster.worldX = x*gp.tileSize;
            monster.worldY = y*gp.tileSize;
            gp.monsters.add(monster);
        }
    }

    public void resetChances(){
        normalZombieChance = 100;
        reptileZombieChance = 0;
        tankZombieChance = 0;
        acidZombieChance = 0;
    }

    public void loadChances(int normalZombieChance, int reptileZombieChance, int tankZombieChance, int acidZombieChance){
        this.normalZombieChance = normalZombieChance;
        this.reptileZombieChance = reptileZombieChance;
        this.tankZombieChance = tankZombieChance;
        this.acidZombieChance = acidZombieChance;
    }
}
