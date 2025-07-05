package Bil211Game2.Game.Main;

import java.io.Serializable;
import java.util.ArrayList;

public class GameSaveData implements Serializable{
    public int playerX;
    public int playerY;
    public int playerLife;
    public int playerScore;
    public ArrayList<Integer> monstersX = new ArrayList<>();
    public ArrayList<Integer> monstersY = new ArrayList<>();
    public ArrayList<Integer> monstersLife = new ArrayList<>();
    public ArrayList<String> monstersType = new ArrayList<>();
    public ArrayList<Integer> ammosX = new ArrayList<>();
    public ArrayList<Integer> ammosY = new ArrayList<>();
    public ArrayList<Integer> ammosXSpeed = new ArrayList<>();
    public ArrayList<Integer> ammosYSpeed = new ArrayList<>();
    public ArrayList<String> ammosTypes = new ArrayList<>();
    public int[] weaponsAmmo = new int[5];
    public int[] weaponsMagazineAmmo = new int[5];
    public int gamePhase;
    public int normalZombieChance, reptileZombieChance, tankZombieChance, acidZombieChance;
}
