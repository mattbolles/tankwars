

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject{


    private int x;
    private int y;
    private int rightCorner = x + 50;
    private int leftCorner = y + 50;
    private int vx;
    private int vy;
    private int angle;
    private int width;
    private int height;
    private int oldX;
    private int oldY;
    // R from trig - higher the value, faster it goes
    private int R = 2;

    private int health = 100;
    private int healthTickCount = 0;
    private final int ROTATION_SPEED = 3;
    private Rectangle hitBox; // for collision detection
    private ArrayList<Bullet> bulletList;
    private ArrayList<Bullet> explodedBulletList;
    private BufferedImage tankImage;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    String objectType = "tank";
    private boolean isStopped = false;
    private TankGame tankGame;
    private String owner;
    int lives = 3;
    boolean completelyKilled = false;


    Tank(int x, int y, int vx, int vy, int angle, BufferedImage tankImage, String owner, TankGame tankGame) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.oldX = oldX;
        this.oldY = oldY;
        this.tankImage = tankImage;
        this.angle = angle;
        this.owner = owner;
        this.tankGame = tankGame;
        // puts hitbox in location of tank, dimensions are same as tank image
        this.hitBox = new Rectangle(x + vx,y + vy,this.tankImage.getWidth(), this.tankImage.getHeight());
        this.bulletList = new ArrayList<>();
        this.explodedBulletList = new ArrayList<>();
        this.objectType = objectType;
        //need to add generic weapon implementation
    }

   void setTankGame(TankGame tankGame) {
        this.tankGame = tankGame;
    }

    void setAngle(int angle) {
        this.angle = angle;
    }


    public Rectangle getHitBox() {
        // return bounds of hitbox - where they can collide
        return hitBox.getBounds();
    }

    public void setSpeed(int R) {
        this.R = R;
    }

    int getLives() {
        return lives;
    }

    void setLives(int lives) {
        this.lives = lives;
    }

    void killTank() {
        if (lives > 0) {
            //respawn
            if ("tankOne".equals(this.getOwner())) {
                this.setX(GameInfo.tankOneXSpawnCoord);
                this.setY(GameInfo.tankOneYSpawnCoord);
                this.setAngle(0);
            }

            else {
                this.setX(GameInfo.tankTwoXSpawnCoord);
                this.setY(GameInfo.tankTwoYSpawnCoord);
                this.setAngle(180);
            }
            this.setVx(0);
            this.setVy(0);
            this.setHealth(100);
            lives--;
        }

        else {
            completelyKilled = true;
        }
    }

    boolean getCompletelyKilled() {
        return completelyKilled;
    }

    void setIsStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    public String getObjectType() {
        return objectType;
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

    public void setHealthTickCount(int healthTickCount) {
        this.healthTickCount = healthTickCount;
    }

    @Override
    public void collide() {
        setIsStopped(true);
        checkBorder();
        this.hitBox.setLocation(x + vx, y + vy);
    }

    public int getY() {
        return y;
    }

    void addBullet(int x, int y, int vx, int vy, int angle, TankGame tankGame) {
        Bullet bullet = new Bullet(x, y, angle, Resource.getResourceImage("bullet"), owner);
        tankGame.addGameObject(bullet);
    }

    @Override
    public int getWidth() {
        return this.tankImage.getWidth();
    }

    @Override
    public int getHeight() {
        return this.tankImage.getHeight();
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void damageTank(int damage) {
            // tickcount prevents damage from being continous
            if (healthTickCount == 0) {
                health -= damage;
            }
            healthTickCount++;
            // reset to make sure it can be damaged again
            if (healthTickCount >= 20) {
                setHealthTickCount(0);
            }
    }

    public int getHealth() {
        return this.health;
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

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return this.owner;
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
            addBullet(x + vx, y + vy, vx, vy, angle, tankGame);
            //Bullet bullet = new Bullet(x + vx, y + vy, angle, Resource.getResourceImage("bullet"), owner);
            //tankGame.addGameObject(bullet);

        }

        /*if (this.ShootPressed = false) {
            TankGame.tickCounter = 20;
        }*/
        this.isStopped = false;
        /*this.bulletList.forEach(bullet -> bullet.update());
        for (Bullet bullet : this.bulletList) {
                if (bullet.isExploded()) {
                    this.explodedBulletList.add(bullet);
                }
        }
        bulletList.removeAll(explodedBulletList);*/
        this.hitBox.setLocation(x + vx,y + vy);

    }

    public ArrayList<Bullet> getBulletList() {
        return this.bulletList;
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
        if (!isStopped) {
            x -= vx;
            y -= vy;
        }
        // makes sure doesn't go out of bounds
        checkBorder();
        // update hitbox location
        this.hitBox.setLocation(x + vx,y + vy);
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        if (!isStopped) {
            x += vx;
            y += vy;
        }
        checkBorder();
        // take into account momentum to prevent tank from getting stuck
        this.hitBox.setLocation(x+vx ,y+vy);
    }


    public int getAngle() {
        return this.angle;
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
        if (x >= GameInfo.WORLD_WIDTH - 82) {
            x = GameInfo.WORLD_WIDTH - 82;
        }

        // top border
        if (y < 32) {
            y = 32;
        }

        // bottom border
        // number is tank height + tile height
        if (y >= GameInfo.WORLD_HEIGHT - 82) {
            y = GameInfo.WORLD_HEIGHT - 82;
        }

    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public int getCameraX() {
        // get 1st tank
        // divide by 4 as each player gets half the screen width originally
        int cameraX = this.getX() - (GameInfo.SCREEN_WIDTH / 4);
        if (cameraX > GameInfo.offsetMaxX) {
            cameraX = GameInfo. offsetMaxX;
        }

        else if (cameraX < GameInfo.offsetMinX) {
            cameraX = GameInfo.offsetMinX;
        }
        return cameraX;
    }

    public int getCameraY() {
        int cameraY = this.getY() - (GameInfo.SCREEN_HEIGHT / 2);
        if (cameraY > GameInfo.offsetMaxY) {
            cameraY = GameInfo.offsetMaxY;
        }

        else if (cameraY < GameInfo.offsetMinY) {
            cameraY = GameInfo.offsetMinY;
        }
        return cameraY;
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
        /*g2d.setColor(Color.YELLOW);
        g2d.drawRect(x,y,this.tankImage.getWidth(),this.tankImage.getHeight());*/
    }



}
