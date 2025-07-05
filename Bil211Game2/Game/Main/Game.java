package Bil211Game2.Game.Main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Zombie Apocalypse");

        try {
            BufferedImage icon = ImageIO.read(
                Game.class.getResourceAsStream
                ("/Bil211Game2/Resources/Images/Other/Zombie Poster/Zombie-Tileset---_0296_Capa-297.png"));
            window.setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                gamePanel.cleanupResources();
            }
        });

        gamePanel.setupAfterVisible();
        
        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }
}

/*
 * 4500 SATIR KOD
 */