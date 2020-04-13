import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet {
    int x, y, vx, vy, angle;
    // speed of bullet
    int R = 7;
    BufferedImage bulletImage;
    Rectangle hitBox;

    public Bullet(int x, int y, int angle, BufferedImage bulletImage) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.bulletImage = bulletImage;
        this.hitBox = new Rectangle(x, y, this.bulletImage.getWidth(), this.bulletImage.getHeight());
    }

    public void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation(x,y);
    }

    public void checkBorder() {
        if (x < 32) {
            x = 32;
        }
        //  88 = width of tank image + width of tile
        if (x >= TankGame.SCREEN_WIDTH - 80) {
            x = TankGame.SCREEN_WIDTH - 80;
        }
        if (y < 32) {
            y = 32;
        }
        if (y >= TankGame.SCREEN_HEIGHT - 104) {
            y = TankGame.SCREEN_HEIGHT - 104;
        }
    }
    public void update() {
        moveForwards();
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        // divide by 2 to get center point of tank
        rotation.rotate(Math.toRadians(angle), this.bulletImage.getWidth() / 2.0, this.bulletImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.bulletImage, rotation, null);
        // draw hitbox in yellow
        g2d.setColor(Color.YELLOW);
        g2d.drawRect(x,y,this.bulletImage.getWidth(),this.bulletImage.getHeight());
    }
}
