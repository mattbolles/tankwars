// used to detect whether a collision happens between two game objects

import java.awt.*;
import java.awt.image.BufferedImage;

public class CollisionDetection{

    public GameObject gameObject1, gameObject2;
    public int x1, x2, y1, y2, height1, height2, width1, width2, collisionX, collisionY;
    boolean collisionDetected = false;
    BufferedImage objectImage1, objectImage2;
    Rectangle intersection;

    public CollisionDetection() {
    }

    public boolean isCollisionDetected(GameObject gameObject1, GameObject gameObject2) {
        if (gameObject1.getHitBox().getBounds().intersects(gameObject2.getHitBox().getBounds())) {
            collisionDetected = true;
        }
        return collisionDetected;
    }

    public Rectangle getIntersection(GameObject gameObject1, GameObject gameObject2) {
        return gameObject1.getHitBox().intersection(gameObject2.getHitBox());
    }
}
