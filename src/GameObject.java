// generic object in game, anything that can be interacted with

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    int x, y, height, width;
    Rectangle hitBox = new Rectangle(x, y, width, height);
    BufferedImage objectImage;



    public abstract void drawImage(Graphics g);

    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }
}
