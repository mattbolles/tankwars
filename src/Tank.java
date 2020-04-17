

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject {


    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private int width;
    private int height;

    // R from trig - higher the value, faster it goes
    private final int R = 2;
    private final int ROTATION_SPEED = 3;
    private Rectangle hitBox; // for collision detection
    private ArrayList<Bullet> bulletList;
    private BufferedImage tankImage;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;


    Tank(int x, int y, int vx, int vy, int angle, BufferedImage tankImage) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.tankImage = tankImage;
        this.angle = angle;
        // puts hitbox in location of tank, dimensions are same as tank image
        this.hitBox = new Rectangle(x,y,this.tankImage.getWidth(), this.tankImage.getHeight());
        this.bulletList = new ArrayList<>();
        //need to add generic weapon implementation
    }


    public Rectangle getHitBox() {
        // return bounds of hitbox - where they can collide
        return hitBox.getBounds();
    }


    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleShootPressed() {this.ShootPressed = true;}

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleShootPressed() {this.ShootPressed = false;}

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getVx() {
        return vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public int getVy() {
        return vy;
    }

    public boolean isUpPressed() {
        return UpPressed;
    }

    public boolean isDownPressed() {
        return DownPressed;
    }



    //every game loop this runs
    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        if (this.ShootPressed && TankGame.tickCounter % 20 == 0) {
            Bullet bullet = new Bullet(x, y, angle, TankGame.bulletImage);
            bulletList.add(bullet);
        }

        /*if (this.ShootPressed = false) {
            TankGame.tickCounter = 20;
        }*/

        this.bulletList.forEach(bullet -> bullet.update());
    }

    private void rotateLeft() {
        this.angle -= this.ROTATION_SPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATION_SPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        // makes sure doesn't go out of bounds
        checkBorder();
        // update hitbox location
        this.hitBox.setLocation(x,y);
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation(x,y);
    }

    private void shoot() {
        // yeah we're gonna need to add this later
    }




    private void checkBorder() {
        //  32 = width of tile
        // left border
        if (x < 32) {
            x = 32;
        }

        // right border
        // number is tank width + tile width
        if (x >= TankGame.WORLD_WIDTH - 82) {
            x = TankGame.WORLD_WIDTH - 82;
        }

        // top border
        if (y < 32) {
            y = 32;
        }

        // bottom border
        // number is tank height + tile height
        if (y >= TankGame.WORLD_HEIGHT - 82) {
            y = TankGame.WORLD_HEIGHT - 82;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        // divide by 2 to get center point of tank
        rotation.rotate(Math.toRadians(angle), this.tankImage.getWidth() / 2.0, this.tankImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.tankImage, rotation, null);
        this.bulletList.forEach(bullet -> bullet.drawImage(g));
        // draw hitbox in yellow
        g2d.setColor(Color.YELLOW);
        g2d.drawRect(x,y,this.tankImage.getWidth(),this.tankImage.getHeight());
    }



}
