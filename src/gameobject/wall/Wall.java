package gameobject.wall;

import gameobject.GameObject;

import java.awt.*;

public abstract class Wall extends GameObject {
    public Rectangle hitBox;
    public String objectType;

    public abstract int getX();

    public abstract int getY();

    public abstract Rectangle getHitBox();

    public abstract void drawImage(Graphics g);

    public abstract void update();

}
