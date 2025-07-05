package Bil211Game2.Game.Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Bil211Game2.Game.Entity.Entity;
import Bil211Game2.Game.Objects.Others.OBJ_Hearth;

public class UI {
    GamePanel gp;
    Font arial_40, arial_80B;
    BufferedImage heartFull, heartHalf, heartBlank;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    Graphics2D g2;
    public String currentDialog = "";
    public int commandNum = 0;
    public int pauseCommandNum = 0;

    int phaseTransitionCounter = 0;
    boolean transitionFlag = false;

    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial",Font.PLAIN,40);
        arial_80B = new Font("Arial",Font.BOLD,80);

        // Create Hud Object
        Entity heart = new OBJ_Hearth(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartBlank = heart.image3;
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if(gp.gameState == gp.playState){
            drawPlayerLife();
            drawPlayerAmmo();
            drawPlayerScore();
            drawZombieCounter();
            if(gp.phaseFinishDraw){
                transitionFlag = true;
                if(gp.currentPhase == gp.phase1){
                    drawPhaseTransition("Please Ready For The Game");
                }
                else{
                    drawPhaseTransition((gp.currentPhase) + ". Phase Finished.\n Please Ready For Next");
                }
                
            }
            if(gp.drawFPS){
                drawFPS();
            }
        }
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPlayerAmmo();
            drawPlayerScore();
            drawPauseScreen();
        }
        if(gp.gameState == gp.dialogState){
            drawDialogScreen();
        }
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        if(gp.gameState == gp.finishState){
            drawFinishScreen();
        }
        
    }

    public void drawPauseScreen(){
        // Tüm ekranı kaplayan yarı-saydam siyah arka plan
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Pause menü paneli
        int panelWidth = gp.tileSize*10;
        int panelHeight = gp.tileSize*8;
        int panelX = gp.screenWidth/2 - panelWidth/2;
        int panelY = gp.screenHeight/2 - panelHeight/2;
        
        // Panel arka planı (mavi-mor gradyan)
        GradientPaint gradient = new GradientPaint(
            panelX, panelY, new Color(50, 50, 80),
            panelX, panelY + panelHeight, new Color(20, 20, 40)
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 25, 25);
        
        // Panel kenarı
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(100, 100, 180, 200));
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 25, 25);
        
        // "GAME PAUSED" başlığı
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.setColor(new Color(220, 220, 255));
        String pausedText = "GAME PAUSED";
        int pausedX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(pausedText)/2;
        g2.drawString(pausedText, pausedX, (int) (panelY + gp.tileSize*1.5));
        
        // Alt çizgi
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(panelX + gp.tileSize, panelY + gp.tileSize*2, 
                   panelX + panelWidth - gp.tileSize, panelY + gp.tileSize*2);
        
        // Karakter resmi (daha küçük)
        int characterSize = gp.tileSize*3/2;
        int characterX = gp.screenWidth/2 - characterSize/2;
        int characterY = (int) (panelY + gp.tileSize*2.5);
        g2.drawImage(gp.player.down1, characterX, characterY, characterSize, characterSize, null);
        
        // Menü seçenekleri
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        
        // Menü arkaplanları ve metinleri
        drawMenuItem("CONTINUE", panelX, (int) (panelY + gp.tileSize*4.5), panelWidth, pauseCommandNum == 0);
        drawMenuItem("SAVE GAME", panelX, (int) (panelY + gp.tileSize*5.5), panelWidth, pauseCommandNum == 1);
        drawMenuItem("QUIT", panelX, (int) (panelY + gp.tileSize*6.5), panelWidth, pauseCommandNum == 2);
        
        // Oyuncu istatistikleri
        drawPauseStats(panelX + gp.tileSize/2, (int) (panelY + panelHeight - gp.tileSize*0.5 - gp.tileSize/4), panelWidth - gp.tileSize);
    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0,0,0, 175);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);;
    
        c = new Color(255,255,255);
        g2.setColor(c);
        // Set The Outlines What Was Created By Graphics2 
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public void drawDialogScreen(){
        // WINDOW
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x += gp.tileSize/2;
        y += gp.tileSize;

        for(String line:currentDialog.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public void drawTitleScreen(){
        // Gradyan arka plan
        GradientPaint gradient = new GradientPaint(0, 0, new Color(40, 40, 60), 
                0, gp.screenHeight, new Color(80, 20, 80));
        g2.setPaint(gradient);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    
        // Doku ekleme (küçük kareler şeklinde)
        g2.setColor(new Color(0, 0, 0, 50));
        for(int i = 0; i < gp.screenWidth; i += 20) {
            for(int j = 0; j < gp.screenHeight; j += 20) {
                g2.fillRect(i, j, 10, 10);
            }
        }
        
        // Kırmızı yarı saydam kaplama (zombi temasını vurgulamak için)
        g2.setColor(new Color(142, 22, 22, 70));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    
        // Oyun başlığı
        String gameTitle = "ZOMBIE APOCALYPSE";
        g2.setFont(new Font("Impact", Font.BOLD, 60));
        
        // Başlığa gölge efekti ekleme
        g2.setColor(new Color(120, 0, 0));
        int x = getXForCenteredText(gameTitle);
        int y = gp.tileSize*3;
        g2.drawString(gameTitle, x+3, y+3);
        
        // Başlık ana rengi
        g2.setColor(new Color(220, 20, 20));
        g2.drawString(gameTitle, x, y);
        
        // Karakter gösterimi
        int characterSize = gp.tileSize*3;
        int characterX = gp.screenWidth/2 - characterSize/2;
        int characterY = y + gp.tileSize;
        g2.drawImage(gp.player.down1, characterX, characterY - gp.tileSize + gp.tileSize/2, characterSize, characterSize, null);
        
        // Menü paneli
        int menuWidth = gp.tileSize*8;
        int menuHeight = gp.tileSize*4 + gp.tileSize/2 + gp.tileSize/8;
        int menuX = gp.screenWidth/2 - menuWidth/2;
        int menuY = characterY + characterSize - gp.tileSize/6;
        
        // Menü arka planı
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 25, 25);
        
        // Menü kenarı
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(100, 20, 20, 200));
        g2.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 25, 25);
        
        // Menü başlığı
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(new Color(220, 220, 220));
        String menuTitle = "MAIN MENU";
        int menuTitleX = getXForCenteredText(menuTitle);
        g2.drawString(menuTitle, menuTitleX, menuY + gp.tileSize/2 + 10);
        
        // Alt çizgi
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(menuX + gp.tileSize, menuY + gp.tileSize/2 + 20, 
                    menuX + menuWidth - gp.tileSize, menuY + gp.tileSize/2 + 20);
        
        // Menü seçenekleri
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        
        // Menü öğeleri
        String[] options = {"NEW GAME", "TEST MODE", "LOAD GAME", "QUIT"};
        for(int i = 0; i < options.length; i++) {
            drawMenuOption(options[i], menuX, (int) (menuY + gp.tileSize*1.5 + (i * gp.tileSize*0.9)), 
                          menuWidth, i == commandNum);
        }
        
        // Ekranın alt kısmında kontrol bilgileri
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(180, 180, 180));
        String controlsText = "CONTROLS: W,A,S,D to move | MOUSE to aim and shoot | R to reload | P to pause";
        int controlsX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(controlsText)/2;
        g2.drawString(controlsText, controlsX, gp.screenHeight - gp.tileSize/8);
    }

    public void menuSelect(){
        if(commandNum == 0){
            gp.gameState = gp.playState;
            gp.aSetter.setMonsterByPhase();
            
            gp.stopMusic();
            gp.playMusic(Sound.PHASE1_MUSIC);
        }
        else if(commandNum == 1){
            gp.testMode();

            gp.stopMusic();
            gp.playMusic(Sound.PHASE1_MUSIC);
        }
        else if(commandNum == 2){
            gp.loadGame();
        }
        else{
            System.exit(0);
        }
    }

    public void drawPlayerLife(){
        int x = gp.screenWidth - gp.tileSize*4;
        int y = gp.tileSize/2;

        int i = 0;

        // DRAW MAX LIFE
        while(i < gp.player.maxLife / 2){
            g2.drawImage(heartBlank, x, y, null);
            i++;
            x += gp.tileSize + gp.tileSize/6;
        }

        // RESET
        x = gp.screenWidth - gp.tileSize*4;
        y = gp.tileSize/2;

        i = 0;

        // DRAW CURRENT LIFE
        while(i < gp.player.life){
            g2.drawImage(heartHalf, x, y, null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heartFull, x, y, null);
            }
            i++;
            x += gp.tileSize + gp.tileSize/6;
        }
    }

    public void drawPlayerAmmo(){
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int width = gp.tileSize*4;
        int height = gp.tileSize;
        
        // Arka plan paneli
        g2.setColor(new Color(40, 40, 40, 200));
        g2.fillRoundRect(x, y, width, height, 15, 15);
        
        // Silah adı
        String weaponName = "";
        switch(gp.player.currentWeaponIndex) {
            case 0: weaponName = "Pistol"; break;
            case 1: weaponName = "Rifle"; break;
            case 2: weaponName = "Shotgun"; break;
            case 3: weaponName = "Sniper"; break;
            case 4: weaponName = "Rocket"; break;
        }
        
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.white);
        g2.drawString(weaponName, x + 10, y + 20);
        
        // Mevcut mermi / Toplam mermi
        String ammoText = gp.player.weapons[gp.player.currentWeaponIndex].magazineAmmo + 
                         " / " + 
                         gp.player.weapons[gp.player.currentWeaponIndex].ammo;
        
        g2.drawString(ammoText, x + 10, y + height - 4);
        
        // Sarjor Doluluk Seviyesi
        int magazineSize = gp.player.weapons[gp.player.currentWeaponIndex].magazineSize;
        int currentAmmo = gp.player.weapons[gp.player.currentWeaponIndex].magazineAmmo;
        int barWidth = width - 20;
        int barHeight = 5;
        int barX = x + 10;
        int barY = y + height - 25;
        
        // Boş şarjör
        g2.setColor(new Color(100, 100, 100));
        g2.fillRect(barX, barY, barWidth, barHeight);
        
        // Dolu şarjör
        float ammoRatio = (float)currentAmmo / magazineSize;
        int filledWidth = (int)(barWidth * ammoRatio);
        g2.setColor(new Color(230, 230, 30));
        g2.fillRect(barX, barY, filledWidth, barHeight);
    }

    public void drawPhaseTransition(String text){
        // Yarı saydam siyah arka plan
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Dalgalı animasyon efekti
        float alpha = 0.7f + 0.3f * (float)Math.sin(System.currentTimeMillis() / 200.0);
        g2.setColor(new Color(255, 165, 0, (int)(alpha * 255)));
        
        // Ana metin
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        int y = gp.screenHeight/2 - 50;
        
        for(String line : text.split("\n")){
            int x = getXForCenteredText(line);
            g2.drawString(line, x, y);
            y += 50;
        }
        
        // Alt bilgi metni
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(new Color(255, 255, 255, 200));
        String readyText = "Zombie Amount: " + ((gp.currentPhase + 1)*3);
        int readyX = getXForCenteredText(readyText);
        g2.drawString(readyText, readyX, gp.screenHeight/2 + 100);
    }

    public void drawPlayerScore(){
        // Skor paneli konumu ve boyutu
        int x = gp.screenWidth - gp.tileSize*4 - gp.tileSize/2;
        int y = gp.tileSize/2 + gp.tileSize + 10;
        int width = gp.tileSize*4;
        int height = gp.tileSize;
        
        // Arka plan paneli
        g2.setColor(new Color(40, 40, 40, 200));
        g2.fillRoundRect(x, y, width, height, 15, 15);
        
        // Panel kenarı
        g2.setColor(new Color(150, 150, 150, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 15, 15);
        
        // Skor etiketi
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(220, 220, 220));
        g2.drawString("SCORE:", x + 10, y + height/2 + 6);
        
        // Skor değeri
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.setColor(new Color(255, 215, 0)); // Altın sarısı
        String scoreValue = String.valueOf(gp.player.score);
        int valueX = x + width - g2.getFontMetrics().stringWidth(scoreValue) - 10;
        g2.drawString(scoreValue, valueX, y + height/2 + 6);
    }

    public void drawFinishScreen(){
        // Tüm ekranı kaplayan yarı-saydam siyah arka plan
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Oyun sonu paneli
        int panelWidth = gp.tileSize*10;
        int panelHeight = gp.tileSize*8;
        int panelX = gp.screenWidth/2 - panelWidth/2;
        int panelY = gp.screenHeight/2 - panelHeight/2;
        
        // Panel arka planı
        GradientPaint gradient = new GradientPaint(
            panelX, panelY, new Color(80, 0, 0),
            panelX, panelY + panelHeight, new Color(30, 0, 0)
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);
        
        // Panel kenarı
        g2.setStroke(new BasicStroke(4));
        g2.setColor(new Color(150, 0, 0));
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);
        
        // "GAME OVER" başlığı
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.setColor(new Color(200, 0, 0));
        String gameOverText = "GAME OVER";
        int gameOverX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(gameOverText)/2;
        g2.drawString(gameOverText, gameOverX, panelY + gp.tileSize*2);
        
        // Alt gölge efekti
        g2.setColor(new Color(50, 0, 0));
        g2.drawString(gameOverText, gameOverX + 3, panelY + gp.tileSize*2 + 3);
        
        // Skor
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.setColor(new Color(255, 215, 0)); // Altın rengi
        String scoreText = "SCORE: " + gp.player.score;
        int scoreX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(scoreText)/2;
        g2.drawString(scoreText, scoreX, panelY + gp.tileSize*4);
        
        // Faz bilgisi
        g2.setFont(new Font("Arial", Font.PLAIN, 22));
        g2.setColor(new Color(220, 220, 220));
        String phaseText = "PHASE REACHED: " + (gp.currentPhase + 1);
        int phaseX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(phaseText)/2;
        g2.drawString(phaseText, phaseX, panelY + gp.tileSize*5);
        
        // Yeniden başlatma talimatı
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(new Color(200, 200, 200));
        String restartText = "Press ENTER to return to main menu";
        int restartX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(restartText)/2;
        
        // Yanıp sönen metin için basit efekt
        if(System.currentTimeMillis() / 500 % 2 == 0) {
            g2.drawString(restartText, restartX, panelY + gp.tileSize*7);
        }
    }

    public void drawFPS() {
        // FPS göstergesi için konum ve boyut
        int x = gp.tileSize/2;
        int y = gp.screenHeight - gp.tileSize;
        int width = gp.tileSize*2;
        int height = gp.tileSize/2;
        
        // Arka plan paneli
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(x, y, width, height, 10, 10);
        
        // Panel kenarı
        g2.setColor(new Color(150, 150, 150, 120));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(x, y, width, height, 10, 10);
        
        // FPS değeri
        g2.setFont(new Font("Consolas", Font.BOLD, 14));
        
        // FPS durumuna göre renk değişimi
        if (gp.fps >= 55) {
            g2.setColor(new Color(30, 180, 30)); // Yeşil - iyi
        } else if (gp.fps >= 30) {
            g2.setColor(new Color(180, 180, 30)); // Sarı - orta
        } else {
            g2.setColor(new Color(180, 30, 30)); // Kırmızı - düşük
        }
        
        String fpsText = "FPS: " + gp.fps;
        int textX = x + 10;
        int textY = y + height - gp.tileSize/8 - 1;
        g2.drawString(fpsText, textX, textY);
    }

    private void drawMenuItem(String text, int x, int y, int width, boolean selected) {
        int itemHeight = gp.tileSize/2 + 10;
        int textX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(text)/2;
        
        // Seçili öğe için arka plan
        if(selected) {
            g2.setColor(new Color(100, 100, 180, 180));
            g2.fillRoundRect(x + gp.tileSize, y - itemHeight/2, width - gp.tileSize*2, itemHeight, 15, 15);
            g2.setColor(new Color(220, 220, 255));
            // Seçim işaretçisi
            g2.fillPolygon(
                new int[]{x + gp.tileSize - 10, x + gp.tileSize - 20, x + gp.tileSize - 10},
                new int[]{y - 8, y, y + 8},
                3
            );
        } else {
            g2.setColor(new Color(180, 180, 180));
        }
        
        // Menü metni
        g2.drawString(text, textX, y + 10);
    }

    private void drawPauseStats(int x, int y, int width) {
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(180, 180, 180));
        
        // Skor
        String scoreText = "Score: " + gp.player.score;
        g2.drawString(scoreText, x, y);
        
        // Can
        String healthText = "Health: " + gp.player.life + "/" + gp.player.maxLife;
        int healthX = x + width - g2.getFontMetrics().stringWidth(healthText);
        g2.drawString(healthText, healthX, y);
        
        // Silah
        String weaponText = "Weapon: ";
        switch(gp.player.currentWeaponIndex) {
            case 0: weaponText += "Pistol"; break;
            case 1: weaponText += "Rifle"; break;
            case 2: weaponText += "Shotgun"; break;
            case 3: weaponText += "Sniper"; break;
            case 4: weaponText += "Rocket"; break;
        }
        g2.drawString(weaponText, x, y + 25);
        
        // Mevcut faz
        String phaseText = "Phase: " + (gp.currentPhase + 1);
        int phaseX = x + width - g2.getFontMetrics().stringWidth(phaseText);
        g2.drawString(phaseText, phaseX, y + 25);
    }

    private void drawMenuOption(String text, int x, int y, int width, boolean selected) {
        int itemHeight = gp.tileSize/2;
        int textX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(text)/2;
        
        // Seçili öğe için arka plan
        if(selected) {
            // Kırmızı gradyan arka plan
            GradientPaint optionGradient = new GradientPaint(
                x + gp.tileSize, y - itemHeight/2, 
                new Color(120, 20, 20, 200),
                x + gp.tileSize, y + itemHeight/2, 
                new Color(180, 30, 30, 200)
            );
            g2.setPaint(optionGradient);
            g2.fillRoundRect(x + gp.tileSize/2, y - itemHeight/2, 
                            width - gp.tileSize, itemHeight, 15, 15);
            
            // Seçim işaretçisi (üçgen)
            g2.setColor(new Color(255, 255, 255));
            g2.fillPolygon(
                new int[]{x + gp.tileSize/2 - 5, x + gp.tileSize/2 - 15, x + gp.tileSize/2 - 5},
                new int[]{y - 8, y, y + 8},
                3
            );
            
            g2.setColor(new Color(255, 255, 255));
        } else {
            g2.setColor(new Color(200, 200, 200));
        }
        
        // Menü metni
        g2.drawString(text, textX, y + gp.tileSize/6 - 1);
    }

    public void drawZombieCounter() {
        // Zombi sayacı için konum ve boyut
        int x = gp.tileSize/2;
        int y = gp.tileSize*2 - gp.tileSize/4;
        int width = gp.tileSize*4;
        int height = (int)(gp.tileSize*0.8);
        
        // Arka plan paneli
        g2.setColor(new Color(40, 0, 0, 180));
        g2.fillRoundRect(x, y, width, height, 15, 15);
        
        // Panel kenarı
        g2.setColor(new Color(120, 20, 20, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 15, 15);
        
        // Zombi ikonu (basit bir zombi kafa silüeti)
        g2.setColor(new Color(50, 150, 50)); // Zombi yeşili
        int iconSize = (int)(height * 0.6);
        int iconX = x + 10;
        
        // Hayatta kalan zombi sayısı
        int remainingZombies = 0;
        for(int i = 0; i < gp.monsters.size(); i++) {
            if(gp.monsters.get(i) != null) {
                remainingZombies++;
            }
        }
        
        // Zombi sayısı metni
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(Color.WHITE);
        String zombieText = "Zombies: " + remainingZombies;
        g2.drawString(zombieText, iconX + iconSize + 15, y + height/2 + 6);
        
        // Eğer faz bitmiş ve yeni zombiler gelecekse uyarı işareti ekle
        if(gp.phaseFinished && !gp.phaseFinishDraw) {
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.setColor(new Color(255, 200, 0));
            String warningText = "Next Wave Coming!";
            g2.drawString(warningText, x + 10, y + height + 20);
        }
    }
}
