import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet implements ActionListener {
    int x, y, vx, vy, angle;
    // speed of bullet
    int R = 7;
    BufferedImage bulletImage;
    Rectangle hitBox;
    boolean visible;
    boolean explosionVisible;
    private ActionListener explosionTimerEvent;
    private Timer explosionTimer;

    // called when bullet hits wall and explosion shown
    // make a generic explosion class to go with generic weapon class
    Timer timer = new Timer(1000, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        setExplosionVisible(false);
        System.out.println("explosion not visible - from timer");
    }
    });

    public Bullet(int x, int y, int angle, BufferedImage bulletImage) {
        // next 2 lines align bullet with tank
        this.x = (int) (x + (25 * Math.cos(Math.toRadians(angle))) + 20);
        this.y = (int) (y + (25 * Math.sin(Math.toRadians(angle))) + 20);
        this.angle = angle;
        this.bulletImage = bulletImage;
        this.hitBox = new Rectangle(x, y, this.bulletImage.getWidth(), this.bulletImage.getHeight());
        visible = true;
        explosionVisible = false;
    }


    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setExplosionVisible(Boolean visible) {
        this.explosionVisible = visible;
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
        this.hitBox.setLocation(x,y);
    }

    public boolean checkBorder() {
        boolean wallHit = false;
        // left border - works
        if (x < 32) {
            x = 32;
            //setExplosionVisible(true);
            wallHit = true;
        }
        //

        // right border
        if (x >= TankGame.SCREEN_WIDTH - 44) {
            x = TankGame.SCREEN_WIDTH - 44;
            //setExplosionVisible(true);
            wallHit = true;
        }
        //top border - works
        if (y < 32) {
            y = 32;
            wallHit = true;
        }

        // bottom border
        if (y >= TankGame.SCREEN_HEIGHT - 60) {
            y = TankGame.SCREEN_HEIGHT - 60;
            wallHit = true;
        }
        return wallHit;
    }
    public void update() {
        moveForwards();
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        // divide by 2 to get center point of tank
        rotation.rotate(Math.toRadians(angle), this.bulletImage.getWidth() / 2.0, this.bulletImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        if (isVisible()) {
            g2d.drawImage(this.bulletImage, rotation, null);
        }
        if (checkBorder() == true) {
            setVisible(false);
            setExplosionVisible(true);
            startExplosionTimer();
            if (isExplosionVisible() == true) {
                g2d.drawImage(TankGame.explosionSmall, x, y, null);
            }
        }

        //timer.setRepeats(false);

        // draw hitbox in yellow
        g2d.setColor(Color.YELLOW);
        g2d.drawRect(x,y,this.bulletImage.getWidth(),this.bulletImage.getHeight());
    }

    public void startExplosionTimer() {
        explosionTimerEvent = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setExplosionVisible(false);
                System.out.println("Explosion timer end");
            }
        };
        explosionTimer = new Timer(1000, explosionTimerEvent);
        explosionTimer.setRepeats(false);
        explosionTimer.start();
        System.out.println("Explosion timer start");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setExplosionVisible(false);
        System.out.println("explosion not visible - from timer2 ");
    }
}
