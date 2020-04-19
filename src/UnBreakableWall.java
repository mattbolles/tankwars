import java.awt.*;
import java.awt.image.BufferedImage;

public class UnBreakableWall extends Wall {
    int x, y;
    BufferedImage wallImage;

    public UnBreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
        this.hitBox = new Rectangle(x,y,this.wallImage.getWidth(), this.wallImage.getHeight());
    }

    public String getObjectType() {
        return "unbreakableWall";
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
        //nothing needs to be done for this one
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
