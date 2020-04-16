import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall{
    int x, y;
    int state = 2; // state of wall brokenness - change to 1 when hit, 0 when broken
    BufferedImage wallImage;

    public BreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
    }

    public void setState(int wallHits) {
        if (wallHits == 1
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(this.wallImage, x, y, null);
    }
}
