package Bil211Game2.Game.Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    public boolean upPressed, downPressed, leftPressed, rightPressed, RPressed, escPressed, HPressed;

    GamePanel gp;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e){
        int code = e.getKeyCode();

        if(gp.gameState == gp.playState){
            if(code == KeyEvent.VK_W){
                upPressed = true;
            }
            if(code == KeyEvent.VK_S){
                downPressed = true;
            }
            if(code == KeyEvent.VK_A){
                leftPressed = true;
            }
            if(code == KeyEvent.VK_D){
                rightPressed = true;
            }
            if(code == KeyEvent.VK_P){
                gp.gameState = gp.pauseState;
            }
            if(code == KeyEvent.VK_R){
                RPressed = true;
            }
            if(code == KeyEvent.VK_ESCAPE){
                escPressed = true;
            }
            if(code == KeyEvent.VK_1){
                gp.player.changeWeapon(0);
            }
            
            if(code == KeyEvent.VK_2){
                gp.player.changeWeapon(1);
            }
            
            if(code == KeyEvent.VK_3){
                gp.player.changeWeapon(2);
            }
            
            if(code == KeyEvent.VK_4){
                gp.player.changeWeapon(3);
            }
            
            if(code == KeyEvent.VK_5){
                gp.player.changeWeapon(4);
            }

            if(code == KeyEvent.VK_R){
                gp.player.weapons[gp.player.currentWeaponIndex].reload();
            }

            if(code == KeyEvent.VK_T){
                gp.drawFPS = !gp.drawFPS;
            }

            if(code == KeyEvent.VK_H){
                HPressed = true;
            }

            if(code == KeyEvent.VK_L){
                gp.tileM.loadMap("/Bil211Game2/Resources/Maps/ApocalypseWorld_1.txt");
            }
        }
        else if(gp.gameState == gp.pauseState){
            if(code == KeyEvent.VK_P){
                gp.gameState = gp.playState;
            }
            if(code == KeyEvent.VK_W){
                gp.ui.pauseCommandNum--;
                if(gp.ui.pauseCommandNum < 0){
                    gp.ui.pauseCommandNum = 2;
                }
            }
            if(code == KeyEvent.VK_S){
                gp.ui.pauseCommandNum++;
                if(gp.ui.pauseCommandNum > 2){
                    gp.ui.pauseCommandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER){
                if(gp.ui.pauseCommandNum == 0){
                    gp.gameState = gp.playState;
                }
                else if(gp.ui.pauseCommandNum == 1){
                    gp.saveGame();
                }
                else{
                    gp.resetGame();
                    gp.gameState = gp.titleState;

                    gp.stopMusic();
                    gp.playMusic(Sound.TITLE_SCREEN_MUSIC);
                }
            }
        }
        else if(gp.gameState == gp.titleState){
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 3;
                }
            }
            if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 3){
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER){
                gp.ui.menuSelect();
                
            }
        }
        else if(gp.gameState == gp.finishState){
            if(code == KeyEvent.VK_ENTER){
                gp.resetGame();
                gp.gameState = gp.titleState;

                gp.stopMusic();
                gp.playMusic(Sound.TITLE_SCREEN_MUSIC);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_P){}
        if(code == KeyEvent.VK_R){
            RPressed = false;
        }
        if(code == KeyEvent.VK_ESCAPE){
            escPressed = false;
        }
        if(code == KeyEvent.VK_H){
            HPressed = false;
        }
    }
    
}
