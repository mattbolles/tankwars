// generic object in game, anything that can be interacted with

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameObject {
    int x, y, height, width;
    Rectangle hitBox;
    BufferedImage objectImage;

    public GameObject(int x, int y, int width, int height, BufferedImage objectImage) {
        this.x = x;
        this.y = y;
        this.width = objectImage.getWidth();
        this.height = objectImage.getHeight();
        this.objectImage = objectImage;
    }

    public void drawImage(Graphics g) {

    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }
}
