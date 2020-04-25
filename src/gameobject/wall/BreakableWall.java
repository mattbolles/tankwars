package gameobject.wall;

import java.awt.*;
import java.awt.image.BufferedImage;
import resource.*;

import static javax.imageio.ImageIO.read;

public class BreakableWall extends Wall {
    int x, y, height, width;
    int health = 25; // the wall sustains damage, much like a tank. when health reaches 0, it is destroyed
    BufferedImage wallImage;
    int tickCount = 0;
    int explosionTickCount = 0;
    String objectType = "breakableWall";
    boolean damaged = false;
    boolean exploded = false;

    public BreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
        this.health = health;
        this.objectType = objectType;
        this.hitBox = new Rectangle(x,y,this.wallImage.getWidth(), this.wallImage.getHeight());
    }


    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public boolean isExploded() {
        return this.exploded;
    }

    public String getObjectType() {
        return objectType;
    }


    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        // if wall is damaged, show broken wall instead
        if (getHealth() == 25) {
            g2.drawImage(Resource.getResourceImage("breakableWall"), x, y, null);
        }
        else {
            if (!isExploded()) {
                g2.drawImage(Resource.getResourceImage("breakableWallDamaged"), x, y, null);
            }
        }

    }

    @Override
    public void update() {
        if (getHealth() == 0) {
            explosionTickCount++;
            if (explosionTickCount > 50) {
                setExploded(true);
            }
        }
    }

    public void damageWall(int damage) {
        // tickcount prevents damage from being continous
        if (tickCount == 0) {
            health -= damage;
        }
        tickCount++;
        if (tickCount >= 20) {
            setTickCount(0);
        }
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }
}
