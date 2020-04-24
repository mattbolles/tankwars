import java.awt.*;

public abstract class Wall extends GameObject {
        Rectangle hitBox;
        String objectType;

        public abstract void drawImage(Graphics g);
        public abstract void update();
        public abstract int getX();
        public abstract int getY();
        public abstract Rectangle getHitBox();

}
