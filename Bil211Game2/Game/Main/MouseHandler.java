package Bil211Game2.Game.Main;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class MouseHandler implements MouseListener, MouseMotionListener{

    GamePanel gp;
    public MouseEvent event;

    public boolean mouseLeftPressed;
    public int mouseX, mouseY;

    private Cursor customNormalCursor;
    private Cursor customTargetCursor;

    public MouseHandler(GamePanel gp){
        this.gp = gp;

        try{
            BufferedImage cursorTargetImg = ImageIO.read(getClass().getResourceAsStream("/Bil211Game2/Resources/Images/UI/Mouse/Target.png"));
            BufferedImage cursorNormalImg = ImageIO.read(getClass().getResourceAsStream("/Bil211Game2/Resources/Images/UI/Mouse/Normal.png"));
        
            Point hotspotTarget = new Point(cursorTargetImg.getWidth() / 2, cursorTargetImg.getHeight() / 2);
            Point hotspotNormal = new Point(cursorNormalImg.getWidth() / 2, cursorNormalImg.getHeight() / 2);
        
            customTargetCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorTargetImg, hotspotTarget, "Target Cursor");

            customNormalCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorNormalImg, hotspotNormal, "Game Cursor");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int code = e.getButton();
        if(code == MouseEvent.BUTTON1){
            mouseLeftPressed = true;
            event = e;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseLeftPressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(customNormalCursor == null || customTargetCursor == null){
            gp.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        }
        else if(gp.gameState == gp.playState){
            gp.setCursor(customTargetCursor);
        }
        else{
            gp.setCursor(customNormalCursor);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        gp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Varsayılana dön
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Update current mouse position while dragging
        if(mouseLeftPressed) {
            mouseX = e.getX();
            mouseY = e.getY();
            event = e; // Also update the event object
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Update current mouse position while moving
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
