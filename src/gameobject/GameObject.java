package gameobject;

// generic object in game, anything that can be interacted with

import java.awt.*;

public abstract class GameObject {
    public abstract void drawImage(Graphics g);
    public abstract void update();
    public abstract Rectangle getHitBox();
    public abstract void setX(int x);
    public abstract void setY(int y);
    public abstract int getX();
    public abstract int getY();
}
