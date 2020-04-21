import java.awt.*;
import java.awt.image.BufferedImage;

import static javax.imageio.ImageIO.read;

public class BreakableWall extends Wall{
    int x, y, height, width;
    int health = 25; // the wall sustains damage, much like a tank. when health reaches 0, it is destroyed
    CollisionDetection collisionDetection;
    BufferedImage wallImage;
    String objectType = "breakableWall";

    public BreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
        this.health = health;
        this.objectType = objectType;
        this.hitBox = new Rectangle(x,y,this.wallImage.getWidth(), this.wallImage.getHeight());
    }


   /* // add later
    public void stateUpdate(int state) {
        if (state == 1) {
            this.wallImage = Resource.getResourceImage("breakableWallDamaged");
        }

        if (state < 1) {
            destroy();
        }
    }
*/
    public void destroy() {

    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    public String getObjectType() {
        return objectType;
    }

    @Override
    public void collide() {

    }

    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        // if wall is damaged, show broken wall instead
        if (getHealth() < 25) {
            g2.drawImage(Resource.getResourceImage("breakableWallDamaged"), x, y, null);
        }
        else {
            g2.drawImage(this.wallImage, x, y, null);
        }

    }

    @Override
    public void update() {

    }

    public void damageWall() {
        health -= 5;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.wallImage.getWidth();
    }

    @Override
    public int getHeight() {
        return this.wallImage.getHeight();
    }
}
