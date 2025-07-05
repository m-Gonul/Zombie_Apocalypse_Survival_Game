package Bil211Game2.Game.Main;

import java.util.ArrayList;

import Bil211Game2.Game.Entity.Entity;
import Bil211Game2.Game.Objects.Ammos.Ammo;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;
    
        int tileNum1, tileNum2, tileNum3, tileNum4;

        switch(entity.direction){
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum3 = gp.tileM.mapTileSecondStage[entityLeftCol][entityTopRow];
                tileNum4 = gp.tileM.mapTileSecondStage[entityRightCol][entityTopRow];
                if(gp.tileM.tiles[tileNum1].collision||gp.tileM.tiles[tileNum2].collision
                ||gp.tileM.tiles[tileNum3 != 0 ? tileNum3 : 0].collision
                ||gp.tileM.tiles[tileNum4 != 0 ? tileNum3 : 0].collision){
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                tileNum3 = gp.tileM.mapTileSecondStage[entityLeftCol][entityBottomRow];
                tileNum4 = gp.tileM.mapTileSecondStage[entityRightCol][entityBottomRow];
                if(gp.tileM.tiles[tileNum1].collision||gp.tileM.tiles[tileNum2].collision
                ||gp.tileM.tiles[tileNum3 != 0 ? tileNum3 : 0].collision
                ||gp.tileM.tiles[tileNum4 != 0 ? tileNum3 : 0].collision){
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum3 = gp.tileM.mapTileSecondStage[entityLeftCol][entityTopRow];
                tileNum4 = gp.tileM.mapTileSecondStage[entityLeftCol][entityBottomRow];
                if(gp.tileM.tiles[tileNum1].collision||gp.tileM.tiles[tileNum2].collision
                ||gp.tileM.tiles[tileNum3 != 0 ? tileNum3 : 0].collision
                ||gp.tileM.tiles[tileNum4 != 0 ? tileNum3 : 0].collision){
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                tileNum3 = gp.tileM.mapTileSecondStage[entityRightCol][entityTopRow];
                tileNum4 = gp.tileM.mapTileSecondStage[entityRightCol][entityBottomRow];
                if(gp.tileM.tiles[tileNum1].collision||gp.tileM.tiles[tileNum2].collision
                ||gp.tileM.tiles[tileNum3 != 0 ? tileNum3 : 0].collision
                ||gp.tileM.tiles[tileNum4 != 0 ? tileNum3 : 0].collision){
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkEntity(Entity entity, ArrayList<? extends Entity> target){
        int index = 999;

        for(int i = 0; i < target.size(); i++){
            if(target.get(i) != null){
                Entity trgt = target.get(i);
                // Get Entity's Solid Area Position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                // Get The Object's Solid Area Position
                trgt.solidArea.x = trgt.worldX + trgt.solidArea.x;
                trgt.solidArea.y = trgt.worldY + trgt.solidArea.y;
            
                switch(entity.direction){
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if(entity.solidArea.intersects(trgt.solidArea)){
                    if(trgt != entity){
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                trgt.solidArea.x = trgt.solidAreaDefaultX;
                trgt.solidArea.y = trgt.solidAreaDefaultY;
            }
        }

        return index;
    }

    public boolean checkPlayer(Entity entity){
        boolean contactPlayer = false;

        if(gp.player != null){
            // Get Entity's Solid Area Position
            entity.solidArea.x = entity.worldX + entity.solidArea.x;
            entity.solidArea.y = entity.worldY + entity.solidArea.y;
            // Get The Object's Solid Area Position
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        
            switch(entity.direction){
                case "up":
                    entity.solidArea.y -= entity.speed;
                    break;
                case "down":
                    entity.solidArea.y += entity.speed;
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    break;
            }

            if(entity.solidArea.intersects(gp.player.solidArea)){
                entity.collisionOn = true;
                contactPlayer = true;
            }

            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        }

        return contactPlayer;
    }

    public boolean checkAmmo(Ammo ammo, Entity entity){
        boolean collision = false;

        if(ammo == null || entity == null || entity.dying || !entity.alive || ammo.contact){
            return false;
        }

        if(solidAreaCheck(ammo.worldX, ammo.worldY, ammo.solidAreaDefaultX, ammo.solidAreaDefaultY,
        entity.worldX+ entity.solidAreaDefaultX, entity.worldY + entity.solidAreaDefaultY, 
        entity.solidArea.width, entity.solidArea.height, entity.direction, entity.speed)){
            collision = true;
        }        

        return collision;
    }

    public boolean solidAreaCheck(int o1X,int  o1Y,int  o1Width,int  o1Height,int  o2X,
    int  o2Y,int  o2Width,int  o2Height, String direction, int speed){
        boolean inside = false;

        int xIncr = 0;
        int yIncr = 0;

        switch(direction){
            case "up":
                yIncr = -speed;
                break;
            case "down":
                yIncr = +speed;
                break;
            case "left":
                xIncr = -speed;
                break;
            case "right":
                xIncr = +speed;
                break;
        }

        int centerX = o1X + o1Width/2;
        int centerY = o1Y + o1Height/2;
        
        if(centerX > o2X + xIncr && centerX < o2X + o2Width + xIncr 
        && centerY > o2Y + yIncr && centerY < o2Y + o2Height + yIncr){
            inside = true;
        }

        return inside;
    }
}
