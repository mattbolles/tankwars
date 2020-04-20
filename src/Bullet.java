import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    int x, y, vx, vy, angle;
    // speed of bullet
    int R = 7;
    BufferedImage bulletImage;
    BufferedImage explosionImage = Resource.getResourceImage("explosionSmall");
    Rectangle hitBox;
    boolean visible;
    boolean explosionVisible;
    boolean collided = false;
    boolean exploded = false;
    int tickCount = 0;
    //track which tank shoots bullet to avoid self collision
    String owner;

    // make a generic explosion class to go with generic weapon class

    public Bullet(int x, int y, int angle, BufferedImage bulletImage, String owner) {
        // next 2 lines align bullet with tank
        this.x = (int) (x + (25 * Math.cos(Math.toRadians(angle))) + 20);
        this.y = (int) (y + (25 * Math.sin(Math.toRadians(angle))) + 20);
        this.angle = angle;
        this.bulletImage = bulletImage;
        this.owner = owner;
        this.hitBox = new Rectangle(x + vx, y + vy, this.bulletImage.getWidth(), this.bulletImage.getHeight());
        visible = true;
    }

    public String getObjectType() {
        return "bullet";
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return this.owner;
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
        return this.bulletImage.getWidth();
    }

    @Override
    public int getHeight() {
        return this.bulletImage.getHeight();
    }

    @Override
    public void collide() {
        setVisible(false);
    }


    public void setVisible(Boolean visible) {
        this.visible = visible;
    }


    public boolean isVisible() {
        return visible;
    }

    public boolean isExplosionVisible() {
        return explosionVisible;
    }

    public void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation(x + vx,y + vy);
    }

    public boolean checkBorder() {
        boolean wallHit = false;
        // left border - works
        if (x < 32) {
            x = 32;
            setVisible(false);
            wallHit = true;
        }
        //

        // right border
        if (x >= GameInfo.WORLD_WIDTH - 45) {
            x = GameInfo.WORLD_WIDTH - 45;
            setVisible(false);
            wallHit = true;
        }
        //top border - works
        if (y < 32) {
            y = 32;
            setVisible(false);
            wallHit = true;
        }

        // bottom border
        if (y >= GameInfo.WORLD_HEIGHT - 41) {
            y = GameInfo.WORLD_HEIGHT - 41;
            setVisible(false);
            wallHit = true;
        }
        return wallHit;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void update() {
        if (visible) {
            moveForwards();
        }
        else {
            // only iterate tick count once bullet has collided so it can explode
            tickCount++;
            if (tickCount > 30) {
                exploded = true;
            }
        }
        this.hitBox.setLocation(x,y);
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        // divide by 2 to get center point of tank
        rotation.rotate(Math.toRadians(angle), this.bulletImage.getWidth() / 2.0, this.bulletImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        if (isVisible()) {
            g2d.drawImage(this.bulletImage, rotation, null);
        }
        else {
            if (!exploded) {
                g2d.drawImage(explosionImage, rotation, null);
            }
        }
        // draw hitbox in yellow
        g2d.setColor(Color.YELLOW);
        g2d.drawRect(x,y,this.bulletImage.getWidth(),this.bulletImage.getHeight());
    }

}
