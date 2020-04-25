package gameobject.powerup;

import gameobject.GameObject;

public abstract class PowerUp extends GameObject {
    public boolean visible;

    public abstract void setVisible(boolean visible);
}
