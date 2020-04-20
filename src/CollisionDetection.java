// used to detect whether a collision happens between two game objects
// processes all collision detecting for gameobjects

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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


    public ArrayList<GameObject> processCollisions(ArrayList<GameObject> gameObjects) {

        //process all game objects in list, and return completed list after done
        for (GameObject currentGameObject : gameObjects) {
            for (GameObject anotherGameObject : gameObjects) {
                // make sure they're not the same
                if (!currentGameObject.equals(anotherGameObject)) {
                    boolean collisionDetected = isCollisionDetected(currentGameObject, anotherGameObject);
                    // if collision is detected between the two
                    if (collisionDetected) {
                        if (currentGameObject instanceof Tank) {
                            //check what kind of object tank collides with
                            switch (anotherGameObject.getObjectType()) {
                                case "breakableWall":
                                case "unbreakableWall":
                                case "tank":
                                    ((Tank) currentGameObject).setIsStopped(true);
                                case "bullet":
                                    String tankOwner = ((Tank) currentGameObject).getOwner();
                                    String bulletOwner = ((Bullet) anotherGameObject).getOwner();
                                    if (!tankOwner.equalsIgnoreCase(bulletOwner)) {
                                        ((Tank) currentGameObject).bulletDamage();
                                    }
                                default:
                                    break;

                            }
                        }
                    }
                }
            }
        }
        return gameObjects;
    }
}
