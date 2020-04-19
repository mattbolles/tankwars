import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Wall extends GameObject {
        int x, y, height, width;
        Rectangle hitBox;
        String objectType = "wall";


        public abstract void drawImage(Graphics g);

        public void update() {

        }

        public String getObjectType() {
                return "wall";
        }

        public abstract int getX();
        public abstract int getY();
        public abstract int getWidth();
        public abstract int getHeight();

        public abstract Rectangle getHitBox();

}
