import java.awt.*;
import java.awt.image.BufferedImage;

//draws the background and tiles it

public class Background {
    int backgroundWidth, backgroundHeight;
    BufferedImage backgroundImage;

    public Background(int backgroundWidth, int backgroundHeight, BufferedImage backgroundImage) {
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
        this.backgroundImage = backgroundImage;
    }


    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int backgroundWidth = backgroundImage.getWidth();
        int backgroundHeight = backgroundImage.getHeight();
        for (int y = 0; y < GameInfo.WORLD_HEIGHT; y += backgroundHeight) {
            for (int x = 0; x < GameInfo.WORLD_WIDTH; x += backgroundWidth) {
                g2d.drawImage(backgroundImage, x, y, null);
            }
        }
    }

}
