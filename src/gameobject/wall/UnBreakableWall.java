package gameobject.wall;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnBreakableWall extends Wall {
    int x, y;
    BufferedImage wallImage;

    public UnBreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
        this.hitBox = new Rectangle(x, y, this.wallImage.getWidth(), this.wallImage.getHeight());
        this.objectType = "unbreakableWall";
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


    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }


    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.wallImage, x, y, null);
    }

    @Override
    public void update() {
        //nothing needs to be done for this one
    }
}
