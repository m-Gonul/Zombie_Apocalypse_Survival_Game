package Bil211Game2.Game.Main;

public class EventHandler {
    GamePanel gp;
    EventRect[][] eventRects;

    int previousEventX, previousEventY;
    boolean canTouchEvent = true; 
    
    public EventHandler(GamePanel gp){
        this.gp = gp;

        eventRects = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow){
            eventRects[col][row] = new EventRect();
            eventRects[col][row].x = 27;
            eventRects[col][row].y = 22;
            eventRects[col][row].width = 2;
            eventRects[col][row].height = 2;
            eventRects[col][row].eventRectDefaultX = eventRects[col][row].x;
            eventRects[col][row].eventRectDefaultY = eventRects[col][row].y;

            col++;
            if(col == gp.maxWorldCol){
                row++;
                col = 0;
            }
        }
    }

    public void checkEvent(){

        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = (int)Math.sqrt(yDistance*yDistance + xDistance*xDistance);

        if(distance > gp.tileSize){
            canTouchEvent = true;
        }

        if(canTouchEvent){
            if(hit(28, 22,"any")){
                damagePit(28,22);
            }
            else if(hit(26, 20,"any")){
                teleport(9,7);
            }
        }
    }

    public boolean hit(int col, int row, String reqDirection){
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRects[col][row].x = col*gp.tileSize + eventRects[col][row].x;
        eventRects[col][row].y = row*gp.tileSize + eventRects[col][row].y;

        if(gp.player.solidArea.intersects(eventRects[col][row]) &&
        !eventRects[col][row].eventDone){
            if(gp.player.direction.contentEquals(reqDirection) || 
            reqDirection.contentEquals("any")){
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRects[col][row].x = eventRects[col][row].eventRectDefaultX;
        eventRects[col][row].y = eventRects[col][row].eventRectDefaultY;

        return hit;
    }

    public void damagePit(int col, int row){
        gp.player.life--;
        //eventRects[col][row].eventDone = true;
        canTouchEvent = false;
    }

    public void teleport(int col, int row){
        gp.player.worldX = col*gp.tileSize;
        gp.player.worldY = row*gp.tileSize;
        canTouchEvent = false;
    }
}
