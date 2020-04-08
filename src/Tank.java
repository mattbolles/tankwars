

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author anthony-pc
 */
public class Tank{


    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;

    // R from trig - higher the value, faster it goes
    private final int R = 2;
    private final int ROTATION_SPEED = 3;
    private Rectangle hitBox; // for collision detection

    private BufferedImage tankImage;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean SpacePressed;
    private boolean EnterPressed;


    Tank(int x, int y, int vx, int vy, int angle, BufferedImage tankImage) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.tankImage = tankImage;
        this.angle = angle;
        // puts hitbox in location of tank, dimensions are same as tank image
        this.hitBox = new Rectangle(x,y,this.tankImage.getWidth(), this.tankImage.getHeight());
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

    void toggleLeftShiftPressed() {
        this.SpacePressed = true;
    }

    void toggleRightShiftPressed() {
        this.EnterPressed = true;
    }

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

    void unToggleLeftShiftPressed() {
        this.SpacePressed = false;
    }

    void unToggleRightShiftPressed() {
        this.EnterPressed = false;
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
        if (this.SpacePressed) {
            this.shoot();
        }
        if (this.EnterPressed) {
            this.shoot();
        }

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
        if (x < 30) {
            x = 30;
        }
        // 88 = width of tank image - adjust later to include wall
        if (x >= TankGame.SCREEN_WIDTH - 88) {
            x = TankGame.SCREEN_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= TankGame.SCREEN_HEIGHT - 80) {
            y = TankGame.SCREEN_HEIGHT - 80;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        // divide by 2 to get center point of tank
        rotation.rotate(Math.toRadians(angle), this.tankImage.getWidth() / 2.0, this.tankImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.tankImage, rotation, null);
    }



}
