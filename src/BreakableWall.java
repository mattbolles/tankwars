import java.awt.*;
import java.awt.image.BufferedImage;

import static javax.imageio.ImageIO.read;

public class BreakableWall extends Wall{
    int x, y, height, width;
    int state = 2; // state of wall brokenness - change to 1 when hit, 0 when broken
    CollisionDetection collisionDetection;
    BufferedImage wallImage;

    public BreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.height = wallImage.getHeight();
        this.width = wallImage.getWidth();
        this.wallImage = wallImage;
        this.state = state;
    }


    public void stateUpdate(int state) {
        if (state == 1) {
            this.wallImage = TankGame.breakableWallDamaged;
        }

        if (state < 1) {
            destroy();
        }
    }

    public void destroy() {

    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(this.wallImage, x, y, null);
    }
}
