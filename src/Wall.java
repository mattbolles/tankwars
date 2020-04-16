import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Wall extends GameObject {
        int x, y, height, width;
        Rectangle hitBox = new Rectangle(x, y, width, height);
        BufferedImage wallImage;


        public abstract void drawImage(Graphics g);

        public Rectangle getHitBox() {
            return hitBox.getBounds();
        }
}
