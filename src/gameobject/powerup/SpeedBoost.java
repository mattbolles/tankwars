package gameobject.powerup;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpeedBoost extends PowerUp {
    int x, y;
    Rectangle hitBox;
    String objectType;
    String owner;
    boolean active;
    BufferedImage speedBoostImage;
    int duration;
    int powerUpTickCount;
    boolean done;

    public SpeedBoost(int x, int y, BufferedImage speedBoostImage) {
        this.x = x;
        this.y = y;
        this.owner = "none";
        this.duration = 2000; // ticks / ms
        this.powerUpTickCount = 0;
        this.visible = true;
        this.active = false;
        this.objectType = "speedBoost";
        this.speedBoostImage = speedBoostImage;
        this.hitBox = new Rectangle(x, y, this.speedBoostImage.getWidth(), this.speedBoostImage.getHeight());
        this.done = false;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    public void setPowerUpTickCount(int powerUpTickCount) {
        this.powerUpTickCount = powerUpTickCount;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public void drawImage(Graphics g) {
        if (visible) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(this.speedBoostImage, x, y, null);
        }
    }

    @Override
    public void update() {
        if (active) {
            powerUpTickCount++;
        }

        if (powerUpTickCount > duration) {
            this.setActive(false);
            done = true;
        }
    }


}
