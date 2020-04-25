package gameobject.wall;

import resource.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall {
    int x;
    int y;
    int health; // the wall sustains damage, much like a tank. when health reaches 0, it is destroyed
    BufferedImage wallImage;
    int tickCount = 0;
    int explosionTickCount = 0;
    String objectType;
    boolean exploded = false;

    public BreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
        this.health = 15;
        this.objectType = "breakableWall";
        this.hitBox = new Rectangle(x, y, this.wallImage.getWidth(), this.wallImage.getHeight());
    }


    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    public int getHealth() {
        return health;
    }

    public boolean isExploded() {
        return this.exploded;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // if wall is damaged, show broken wall instead
        if (getHealth() == 15) {
            g2.drawImage(Resource.getResourceImage("breakableWall"), x, y, null);
        } else {
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
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }
}
