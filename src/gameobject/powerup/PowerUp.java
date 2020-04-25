package gameobject.powerup;

import gameobject.GameObject;

import java.awt.*;

public abstract class PowerUp extends GameObject {
    Rectangle hitBox;
    String objectType;
    String owner;
    boolean active;
    public boolean visible;
    int duration;
    int powerUpTickCount;

    public abstract String getOwner();
    public abstract void setOwner(String owner);
    public abstract void setActive(boolean active);
    public abstract boolean isActive();
    public abstract boolean isVisible();
    public abstract void setVisible(boolean visible);


}
