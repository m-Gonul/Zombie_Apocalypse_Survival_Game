package Bil211Game2.Game.Main.Tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import javax.imageio.ImageIO;

import Bil211Game2.Game.Main.GamePanel;
import Bil211Game2.Game.Main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public Tile[] tiles;
    public int[][] mapTileNum;
    public int[][] mapTileSecondStage;

    public TileManager(GamePanel gp){
        this.gp = gp;
        tiles = new Tile[160];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        mapTileSecondStage = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        //loadMap("/Bil211Game2/Resources/Maps/Grassland1.txt");
        loadMap("/Bil211Game2/Resources/Maps/ApocalypseWorld_1.txt");
    }

    public void loadMap(String filePath){
        try(InputStream in = getClass().getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(in))){
            Random random = new Random();

            int col = 0;
            int row = 0;
            
            while(col < gp.maxScreenCol && row < gp.maxWorldRow){
                String line = br.readLine();
                while(col < gp.maxWorldCol){
                    String[] numbers = line.split(" ");
                    int num = 0;
                    if(Integer.parseInt(numbers[col]) == 0){
                        boolean flag = random.nextBoolean();
                        if(flag){
                            int type = random.nextInt(3);
                            num = type + 1;
                        }
                        else{
                            num = Integer.parseInt(numbers[col]);
                        }
                        mapTileSecondStage[col][row] = 0;
                    }
                    else if(Integer.parseInt(numbers[col]) == 148){
                        int type = random.nextInt(3);
                        num = type + 148;
                        mapTileSecondStage[col][row] = 0;
                    }
                    else{
                        boolean flag = random.nextBoolean();
                        if(flag){
                            int type = random.nextInt(3);
                            num = type + 1;
                        }
                        else{
                            num = Integer.parseInt(numbers[col]);
                        }
                        mapTileSecondStage[col][row] = Integer.parseInt(numbers[col]);
                    }
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col =0;
                    row++;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getTileImage(){
        // Terrains
        setUpNew(0, "Terrain Variations/Zombie-Tileset---_0077_Capa-78", false);
        setUpNew(1, "Terrain Variations/Zombie-Tileset---_0078_Capa-79", false);
        setUpNew(2, "Terrain Variations/Zombie-Tileset---_0079_Capa-80", false);
        setUpNew(3, "Terrain Variations/Zombie-Tileset---_0080_Capa-81", false);
        // Terrain Roads
        for(int i = 0; i < 12; i++){
            setUpNew(i+4, "Modular Terrain Path/Zombie-Tileset---_00"+(i+65)+"_Capa-"+(i+66), false);
        }
        // Terrain Wall - Vertical
        setUpNew(16, "Terrain wall/Zombie-Tileset---_0064_Capa-65", true);
        // Modular Road
        for(int i = 0; i < 26; i++){
            setUpNew(i+17, "Modular Road/Zombie-Tileset---_00"+(i+29)+"_Capa-"+(i+30), false);
        }
        // Small Building
        for(int i = 0; i < 8; i++){
            setUpNew(i+43, "Modular Small Building/Zombie-Tileset---_00"+(i+91)+"_Capa-"+(i+92), true);
        }
        setUpNew(52, "Modular Small Building/Zombie-Tileset---_0099_Capa-100", true);
        for(int i = 0; i < 6; i++){
            setUpNew(i+53, "Modular Small Building/Zombie-Tileset---_0"+(i+100)+"_Capa-"+(i+101), true);
        }
        // Modular Fences
        for(int i = 0; i < 8; i++){
            setUpNew(i+59, "Modular Fences/Zombie-Tileset---_0"+(i+126)+"_Capa-"+(i+127), true);
        }
        for(int i = 0; i < 23; i++){
            setUpNew(i+67, "Modular Fences/Zombie-Tileset---_0"+(i+194)+"_Capa-"+(i+195), true);
        }
        // Modular Barns
        for(int i = 0; i < 58; i++){
            setUpNew(i+90, "Modular Barns/Zombie-Tileset---_0"+(i+226)+"_Capa-"+(i+227), true);
        }
        // Water Animation Frames
        setUpNew(148, "Water animation frames/Zombie-Tileset---_0061_Capa-62", true);
        setUpNew(149, "Water animation frames/Zombie-Tileset---_0062_Capa-63", true);
        setUpNew(150, "Water animation frames/Zombie-Tileset---_0063_Capa-64", true);
        // Tractor
        setUpNew(151, "Tractor/Zombie-Tileset---_0172_Capa-173", true);
        setUpNew(152, "Tractor/Zombie-Tileset---_0173_Capa-174", true);
        setUpNew(153, "Tractor/Zombie-Tileset---_0174_Capa-175", true);
        setUpNew(154, "Tractor/Zombie-Tileset---_0175_Capa-176", true);
    }

    public void setUp(int index, String imageName, boolean collision){
        try{
            tiles[index] = new Tile();
            tiles[index].image = ImageIO.read(getClass().getResourceAsStream("/Bil211Game/Resources/Tiles/"+ imageName +".png"));
            tiles[index].image = UtilityTool.scaleImage(tiles[index].image, gp.tileSize, gp.tileSize);
            tiles[index].collision = collision;
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setUpNew(int index, String imageName, boolean collision){
        try{
            tiles[index] = new Tile();
            tiles[index].image = ImageIO.read(getClass().getResourceAsStream("/Bil211Game2/Resources/Images/Tiles/"+ imageName +".png"));
            tiles[index].image = UtilityTool.scaleImage(tiles[index].image, gp.tileSize, gp.tileSize);
            tiles[index].collision = collision;
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX 
            && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
            && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY 
            && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
                g2.drawImage(tiles[tileNum].image, screenX, screenY, null);
            }
            
            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }

        worldCol = 0; 
        worldRow = 0;
        
        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){
            int tileNum = mapTileSecondStage[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if(tileNum != 0 && worldX + gp.tileSize > gp.player.worldX - gp.player.screenX 
            && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
            && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY 
            && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
                g2.drawImage(tiles[0].image, screenX, screenY, null);
                g2.drawImage(tiles[tileNum].image, screenX, screenY, null);
            }
            
            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
