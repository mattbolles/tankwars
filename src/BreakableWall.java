import java.awt.*;
import java.awt.image.BufferedImage;

import static javax.imageio.ImageIO.read;

public class BreakableWall extends Wall{
    int x, y, height, width;
    int state = 2; // state of wall brokenness - change to 1 when hit, 0 when broken
    CollisionDetection collisionDetection;
    BufferedImage wallImage;
    String objectType = "breakableWall";

    public BreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
        this.state = state;
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

    public void setState(int state) {
        this.state = state;
    }

    public String getObjectType() {
        return "breakableWall";
    }

    @Override
    public void collide(GameObject objectCollidedWith) {

    }

    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(this.wallImage, x, y, null);
    }

    @Override
    public void update() {

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
