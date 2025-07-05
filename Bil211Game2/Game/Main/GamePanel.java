package Bil211Game2.Game.Main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import Bil211Game2.Game.Entity.AcidZombie;
import Bil211Game2.Game.Entity.Entity;
import Bil211Game2.Game.Entity.Monster;
import Bil211Game2.Game.Entity.NormalZombie;
import Bil211Game2.Game.Main.Tile.TileManager;
import Bil211Game2.Game.Objects.Ammos.Ammo;
import Bil211Game2.Game.Objects.Ammos.PistolAmmo;
import Bil211Game2.Game.Objects.Ammos.RifleAmmo;
import Bil211Game2.Game.Objects.Ammos.RocketAmmo;
import Bil211Game2.Game.Objects.Ammos.ShotgunAmmo;
import Bil211Game2.Game.Objects.Ammos.SniperAmmo;
import Bil211Game2.Game.Objects.Animations.AnimationManager;
import Bil211Game2.Game.Entity.Player;
import Bil211Game2.Game.Entity.ReptileZombie;
import Bil211Game2.Game.Entity.TankZombie;

public class GamePanel extends Canvas implements Runnable{

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize*maxScreenCol;
    public final int screenHeight = tileSize*maxScreenRow;

    public final int maxWorldCol = 116;
    public final int maxWorldRow = 112;

    int FPS = 60;

    // DOUBLE BUFFER VARIABLES
    private BufferStrategy bufferStrategy;
    private GraphicsConfiguration graphicsConfig;
    private BufferedImage offscreenBuffer;

    // SYSTEM
    public Thread gameThread;
    public KeyHandler keyH = new KeyHandler(this);
    public MouseHandler mouseH = new MouseHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public UI ui = new UI(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public EventHandler eHandler = new EventHandler(this);
    public Sound music = new Sound();
    public Sound se = new Sound();
    public Sound seAmmo = new Sound();
    public Sound seMonster = new Sound();
    TileManager tileM = new TileManager(this);
    private int frameCount = 0;
    private long fpsTimer = 0;
    public int fps = 0;
    public boolean drawFPS = false;
    public AnimationManager animationManager;

    // ENTITY
    public Player player = new Player(this, keyH, mouseH);
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Monster> monsters = new ArrayList<>();
    public ArrayList<Ammo> ammos = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int finishState = 4;
    public boolean phaseFinished = false;
    public boolean phaseFinishDraw = false;

    private int phaseTransitionCounter = 0;
    private final int PHASE_TRANSITION_DURATION = 90; 

    // PHASES
    public final int phase1 = 0;
    public final int phase2 = 1;
    public final int phase3 = 2;
    public final int phase4 = 3;
    public final int phase5 = 4;
    public final int phase6 = 5;
    public final int phase7 = 6;
    public final int phase8 = 7;
    public final int phase9 = 8;
    public final int phase10 = 9;
    public int currentPhase;

    // GamePanel sınıfına eklenecek yeni değişkenler
    private boolean screenShakeActive = false;
    private int screenShakeIntensity = 0;
    private int screenShakeDuration = 0;
    private int screenShakeTimer = 0;
    private int shakeOffsetX = 0;
    private int shakeOffsetY = 0;
    private Random shakeRandom = new Random();

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setIgnoreRepaint(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.setFocusable(true);
        animationManager = new AnimationManager(this);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsConfig = env.getDefaultScreenDevice().getDefaultConfiguration();
    }

    public void setupAfterVisible() {
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();
        
        offscreenBuffer = graphicsConfig.createCompatibleImage(
                screenWidth, screenHeight, Transparency.OPAQUE);
    }

    public void setUpGame(){
        gameState = titleState;
        currentPhase = phase1 - 1;

        playMusic(Sound.TITLE_SCREEN_MUSIC);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInternal = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        fpsTimer = System.currentTimeMillis();

        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/drawInternal;
            lastTime = currentTime;
            
            if(delta >= 1){
                update();

                // Çizim yap
                render();

                delta--;
                frameCount++;

                // Her saniye FPS'i güncelle
                if(System.currentTimeMillis() - fpsTimer >= 1000) {
                    fps = frameCount;
                    frameCount = 0;
                    fpsTimer = System.currentTimeMillis();
                }
            }
        }
    }

    private void render() {
        // BufferStrategy kontrol et
        if (bufferStrategy == null) {
            return;
        }
        
        try {
            Graphics2D g2 = offscreenBuffer.createGraphics();
            try {
                configureGraphicsQuality(g2);
                
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, screenWidth, screenHeight);
                
                if (gameState == titleState) {
                    ui.draw(g2);
                } else {

                    // Oyun öğeleri için ekran sallama uygula
                    AffineTransform originalTransform = g2.getTransform();
                    
                    if (screenShakeActive) {
                        g2.translate(shakeOffsetX, shakeOffsetY);
                    }

                    // TILES
                    tileM.draw(g2);

                    // ADD TO ELEMENTS TO ARRAYLIST
                    entityList.add(player);

                    // MONSTERS ADDING
                    for (int i = 0; i < monsters.size(); i++) {
                        if (monsters.get(i) != null) {
                            entityList.add(monsters.get(i));
                        }
                    }

                    // SORT
                    Collections.sort(entityList, new Comparator<Entity>() {
                        @Override
                        public int compare(Entity e1, Entity e2) {
                            int result = Integer.compare(e1.worldY, e2.worldY);
                            return result;
                        }
                    });

                    // DRAW LIST
                    for (int i = 0; i < entityList.size(); i++) {
                        if (UtilityTool.distanceCalculator(entityList.get(i).worldX, entityList.get(i).worldY, player.worldX, player.worldY) < 10 * tileSize) {
                            entityList.get(i).draw(g2);
                        }
                    }

                    // EMPTY LIST
                    entityList.clear();

                    // AMMO'S DRAW
                    for (Ammo ammo : ammos) {
                        if (UtilityTool.distanceCalculator(ammo.worldX, ammo.worldY, player.worldX, player.worldY) < 8 * tileSize) {
                            ammo.draw(g2);
                        }
                    }

                    // UI çizmeden önce dönüşümü sıfırla (UI sallanmasın)
                    g2.setTransform(originalTransform);

                    animationManager.draw(g2);

                    // UI
                    ui.draw(g2);
                }
            } 
            finally {
                g2.dispose();
            }
            
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            try {
                g.drawImage(offscreenBuffer, 0, 0, null);
            } 
            finally {
                g.dispose();
            }
            
            if (!bufferStrategy.contentsLost()) {
                bufferStrategy.show();
            }
            
            Toolkit.getDefaultToolkit().sync();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureGraphicsQuality(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    public void update(){

        if(gameState == playState){
            updateScreenShake();
            player.update();

            // Handle phase transitions
            if(phaseFinishDraw) {
                phaseTransitionCounter++;
                if(phaseTransitionCounter >= PHASE_TRANSITION_DURATION) {
                    phaseFinishDraw = false;
                    phaseTransitionCounter = 0;
                }
            }
            else if(phaseChecker()){
                phaseFinished = true;
                phaseFinishDraw = true;
                currentPhase++;
                aSetter.setMonsterByPhase();

                playSE(Sound.FANFARE_SOUND);
            }

            for(int i = 0; i < monsters.size(); i++){
                if(monsters.get(i) != null){
                    Monster monsterTemp = monsters.get(i);
                    if(monsterTemp.alive && !monsterTemp.dying){
                        monsterTemp.update();
                    }
                    else if(monsterTemp.dying){
                        monsterTemp.updateDying();
                    }
                    if(!monsterTemp.alive){
                        monsters.set(i, null);
                    }
                }
            }

            for(Ammo ammo : ammos) {
                ammo.update();
            }

            animationManager.update();
        }

        if(gameState == pauseState){

        }
        
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSE(int i){
        se.setFile(i);
        se.setVolume(0.5f);
        se.play();
    }

    public void cleanupResources() {
        // Ses kaynaklarını temizle
        if (music != null) {
            music.cleanup();
        }
        if (se != null) {
            se.cleanup();
        }
    }

    public boolean phaseChecker(){
        boolean flag = true;

        for(int i = 0; i < monsters.size(); i++){
            if(monsters.get(i) != null){
                flag = false;
                break;
            }
        }

        return flag;
    }

    public void resetGame(){
        currentPhase = phase1 - 1;
        player = new Player(this, keyH, mouseH);
        monsters = new ArrayList<>();
        ammos = new ArrayList<>();
        aSetter.resetChances();

        stopMusic();
        playMusic(Sound.PHASE1_MUSIC);
    }

    public void saveGame(){
        GameSaveData data = new GameSaveData();
        data.playerX = player.worldX;
        data.playerY = player.worldY;
        data.playerLife = player.life;
        data.playerScore = player.score;

        for(Monster monster:monsters){
            if(monster != null){
                data.monstersX.add(monster.worldX);
                data.monstersY.add(monster.worldY);
                data.monstersLife.add(monster.life);

                if(monster instanceof NormalZombie){
                    data.monstersType.add("normal");
                }
                else if(monster instanceof ReptileZombie){
                    data.monstersType.add("reptile");
                }
                else if(monster instanceof TankZombie){
                    data.monstersType.add("tank");
                }
                else{
                    data.monstersType.add("acid");
                }
            }
        }

        for(Ammo ammo:ammos){
            if(ammo != null){
                data.ammosX.add(ammo.worldX);
                data.ammosY.add(ammo.worldY);
                data.ammosXSpeed.add(ammo.speedX);
                data.ammosYSpeed.add(ammo.speedY);

                if(ammo instanceof PistolAmmo){
                    data.ammosTypes.add("pistol");
                }
                else if(ammo instanceof RifleAmmo){
                    data.monstersType.add("rifle");
                }
                else if(ammo instanceof ShotgunAmmo){
                    data.ammosTypes.add("shotgun");
                }
                else if(ammo instanceof SniperAmmo){
                    data.ammosTypes.add("sniper");
                }
                else{
                    data.ammosTypes.add("rocket");
                }
            }
        }

        for(int i = 0; i < 5; i++){
            data.weaponsAmmo[i] = player.weapons[i].ammo;
            data.weaponsMagazineAmmo[i] = player.weapons[i].magazineAmmo;
        }

        data.gamePhase = currentPhase;

        data.normalZombieChance = aSetter.normalZombieChance;
        data.reptileZombieChance = aSetter.reptileZombieChance;
        data.tankZombieChance = aSetter.tankZombieChance;
        data.acidZombieChance = aSetter.acidZombieChance;

        try(ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream("Bil211Game2/Resources/Save/lastGameSave.bin"))){
            objOut.writeObject(data);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadGame(){
        resetGame();
        try(ObjectInputStream objOut = new ObjectInputStream(new FileInputStream("Bil211Game2/Resources/Save/lastGameSave.bin"))){
            GameSaveData data = (GameSaveData)objOut.readObject();
            player = new Player(this, keyH, mouseH);
            player.worldX = data.playerX;
            player.worldY = data.playerY;
            player.life = data.playerLife;
            player.score = data.playerScore;

            monsters = new ArrayList<>();

            for(int i = 0; i < data.monstersType.size(); i++){
                if(data.monstersLife != null && data.monstersX != null
                && data.monstersY != null && data.monstersType != null){
                    switch(data.monstersType.get(i)){
                        case "normal":
                            monsters.add(new NormalZombie(this));
                            break;
                        case "reptile":
                            monsters.add(new ReptileZombie(this));
                            break;
                        case "tank":
                            monsters.add(new TankZombie(this));
                            break;
                        case "acid":
                            monsters.add(new AcidZombie(this));
                            break;
                    }
                    if(monsters.size() > i){
                        monsters.get(i).worldX = data.monstersX.get(i);
                        monsters.get(i).worldY = data.monstersY.get(i);
                        monsters.get(i).life = data.monstersLife.get(i);
                    }
                }
            }

            ammos = new ArrayList<>();

            for(int i = 0; i < data.ammosTypes.size(); i++){
                if(data.ammosTypes != null && data.ammosX != null
                && data.ammosXSpeed != null && data.ammosY != null
                && data.ammosYSpeed != null){
                    switch(data.ammosTypes.get(i)){
                        case "pistol":
                            ammos.add(new PistolAmmo(this));
                            break;
                        case "rifle":
                            ammos.add(new RifleAmmo(this));
                            break;
                        case "ahotgun":
                            ammos.add(new ShotgunAmmo(this));
                            break;
                        case "sniper":
                            ammos.add(new SniperAmmo(this));
                            break;
                        case "rocket":
                            ammos.add(new RocketAmmo(this));
                            break;
                    }
                    ammos.get(i).worldX = data.ammosX.get(i);
                    ammos.get(i).worldY = data.ammosY.get(i);
                    ammos.get(i).speedX = data.ammosXSpeed.get(i);
                    ammos.get(i).speedY = data.ammosYSpeed.get(i);
                }
            }

            for(int i = 0; i < 5; i++){
                player.weapons[i].ammo = data.weaponsAmmo[i];
                player.weapons[i].magazineAmmo = data.weaponsMagazineAmmo[i];
            }

            currentPhase = data.gamePhase;

            aSetter.loadChances(data.normalZombieChance, data.reptileZombieChance,
            data.tankZombieChance, data.acidZombieChance);

            gameState = playState;
        }
        catch(IOException e){
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        stopMusic();
        playMusic(Sound.PHASE1_MUSIC);
    }

    public void testMode(){
        resetGame();
        player.worldX = 40*tileSize;
        player.worldY = 40*tileSize;
        for(int i = 0; i < 30; i++){
            for(int j = 0; j < 30; j++){
                NormalZombie zombie = new NormalZombie(this);
                monsters.add(zombie);
                zombie.worldX = (j+9)*tileSize;
                zombie.worldY = (i+7)*tileSize;
            }
        }
        currentPhase = phase10 + 1;
        gameState = playState;
    }

    public void startScreenShake(int intensity, int duration) {
        screenShakeActive = true;
        screenShakeIntensity = intensity;
        screenShakeDuration = duration;
        screenShakeTimer = 0;
    }

    private void updateScreenShake() {
        if (screenShakeActive) {
            // Zamanla azalan bir yoğunluk hesapla
            float currentIntensity = screenShakeIntensity * (1.0f - (float)screenShakeTimer / screenShakeDuration);
            
            // Rastgele ofsetler oluştur
            shakeOffsetX = (int)(shakeRandom.nextInt((int)currentIntensity * 2 + 1) - currentIntensity);
            shakeOffsetY = (int)(shakeRandom.nextInt((int)currentIntensity * 2 + 1) - currentIntensity);
            
            screenShakeTimer++;
            if (screenShakeTimer >= screenShakeDuration) {
                screenShakeActive = false;
                screenShakeTimer = 0;
                shakeOffsetX = 0;
                shakeOffsetY = 0;
            }
        }
    }
}