// generic object in game, anything that can be interacted with

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    int x, y, height, width;
    Rectangle hitBox;
    BufferedImage objectImage;
    boolean collisionDetected = false;
    String objectType;

    public abstract void drawImage(Graphics g);
    public abstract void update();

    public abstract Rectangle getHitBox();


    public String getObjectType() {
        return this.objectType;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract int getX();
    public abstract int getY();
    public abstract int getWidth();
    public abstract int getHeight();

    public abstract void collide();
}
