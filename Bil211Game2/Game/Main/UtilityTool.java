package Bil211Game2.Game.Main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool {
    public static BufferedImage scaleImage(BufferedImage original, int width, int height){
        BufferedImage  scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        
        return scaledImage;
    }

    public static double distanceCalculator(int p1X, int p1Y, int p2X, int p2Y){
        return Math.sqrt((p2X - p1X)*(p2X - p1X)+(p2Y - p1Y)*(p2Y - p1Y));
    }
}
