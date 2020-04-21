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
        for (int currentGameObjectIndex = 0; currentGameObjectIndex < gameObjects.size(); currentGameObjectIndex++) {


            for (int anotherGameObjectIndex = currentGameObjectIndex; anotherGameObjectIndex < gameObjects.size(); anotherGameObjectIndex++) {
                // for readability
                GameObject currentGameObject = gameObjects.get(currentGameObjectIndex);
                GameObject anotherGameObject = gameObjects.get(anotherGameObjectIndex);
                // make sure they're not the same
                if (currentGameObject != anotherGameObject) {
                    boolean collisionDetected =
                            currentGameObject.getHitBox().getBounds().intersects(anotherGameObject.getHitBox().getBounds());
                    // if collision is detected between the two
                    if (collisionDetected) {

                        // if tank colliding with something
                        if (currentGameObject instanceof Tank) {
                            if (anotherGameObject instanceof Tank) {
                                System.out.println("First object type: " + currentGameObject.getObjectType() + currentGameObject.toString() +
                                        " Second object" +
                                        " type: " + anotherGameObject.getObjectType() + anotherGameObject.toString());
                                ((Tank) currentGameObject).setIsStopped(true);
                                ((Tank) anotherGameObject).setIsStopped(true);
                            }

                            else if (anotherGameObject instanceof Bullet) {
                                System.out.println("First object type: " + currentGameObject.getObjectType() + "Second object" +
                                        " type: " + anotherGameObject.getObjectType());
                                ((Tank) currentGameObject).damageTank();
                                String tankOwner = ((Tank) currentGameObject).getOwner();
                                String bulletOwner = ((Bullet) anotherGameObject).getOwner();
                                // if the bullet is not from the same tank, damage that tank
                                if (!bulletOwner.equals(tankOwner)) {
                                    ((Tank) currentGameObject).damageTank();
                                }
                                ((Bullet) anotherGameObject).setVisible(false);
                            }

                            else if (anotherGameObject instanceof UnBreakableWall) {
                                System.out.println("First object type: " + currentGameObject.getObjectType() + "Second object" +
                                        " type: " + anotherGameObject.getObjectType());
                                ((Tank) currentGameObject).setIsStopped(true);
                            }

                            else if (anotherGameObject instanceof BreakableWall) {
                                System.out.println("First object type: " + currentGameObject.getObjectType() + "Second object" +
                                        " type: " + anotherGameObject.getObjectType());
                                ((Tank) currentGameObject).setIsStopped(true);
                                ((BreakableWall) anotherGameObject).damageWall();
                            }
                        }

                        /*switch (currentGameObject.getObjectType()) {
                            case "tank":
                                //System.out.println("tank case reached");
                                //System.out.println("Current game object: " + currentGameObject.getObjectType());
                                //System.out.println("Other game object: " + anotherGameObject.getObjectType());
                                //check what kind of object tank collides with
                                switch (anotherGameObject.getObjectType()) {
                                    case "breakableWall":
                                        ((Tank) currentGameObject).setIsStopped(true);
                                        //((BreakableWall) anotherGameObject).damageWall();
                                        break;
                                    case "unbreakableWall":
                                        ((Tank) currentGameObject).setIsStopped(true);
                                        break;
                                    case "tank":
                                        //if they collide, they both stop and are damaged
                                        ((Tank) currentGameObject).setIsStopped(true);
                                        ((Tank) currentGameObject).damageTank();
                                        ((Tank) anotherGameObject).setIsStopped(true);
                                        ((Tank) anotherGameObject).damageTank();
                                        break;

                                    case "bullet":
                                        String tankOwner = ((Tank) currentGameObject).getOwner();
                                        assert anotherGameObject instanceof Bullet;
                                        String bulletOwner = ((Bullet) anotherGameObject).getOwner();
                                        // if the bullet is not from the same tank, damage that tank
                                        if (!bulletOwner.equals(tankOwner)) {
                                            ((Tank) currentGameObject).damageTank();
                                        }
                                        ((Bullet) anotherGameObject).setVisible(false);
                                        break;
                                    default:
                                        break;

                                }
                                break;
                            case "bullet":
                                //System.out.println("bullet case reached");
                                //System.out.println("Current game object: " + currentGameObject.getObjectType());
                                //System.out.println("Other game object: " + anotherGameObject.getObjectType());
                                switch (anotherGameObject.getObjectType()) {
                                    case "breakableWall":
                                        ((Bullet) currentGameObject).setVisible(false);
                                        ((BreakableWall) anotherGameObject).damageWall();
                                        break;
                                    case "unbreakableWall":
                                        ((Bullet) currentGameObject).setVisible(false);
                                        break;
                                    case "tank":
                                        String tankOwner = ((Tank) currentGameObject).getOwner();
                                        String bulletOwner = ((Bullet) anotherGameObject).getOwner();
                                        // if the bullet is not from the same tank, damage that tank
                                        if (!bulletOwner.equals(tankOwner)) {
                                            ((Tank) currentGameObject).damageTank();
                                        }
                                        break;
                                    case "default":
                                        break;
                                }

                                break;

                            case "unbreakableWall":
                                //System.out.println("unbreakable wall case reached");
                                //System.out.println("Current game object: " + currentGameObject.getObjectType());
                                //System.out.println("Other game object: " + anotherGameObject.getObjectType());
                                switch (anotherGameObject.getObjectType()) {
                                    case "tank":
                                        ((Tank) anotherGameObject).setIsStopped(true);
                                        break;
                                    case "bullet":
                                        assert anotherGameObject instanceof Bullet;
                                        ((Bullet) anotherGameObject).setVisible(false);
                                        break;
                                    case "default":
                                        break;
                                }

                                break;
                            case "breakableWall":
                                //System.out.println("unbreakable wall case reached");
                                //System.out.println("Current game object: " + currentGameObject.getObjectType());
                                //System.out.println("Other game object: " + anotherGameObject.getObjectType());
                                switch (anotherGameObject.getObjectType()) {
                                    case "tank":
                                        ((Tank) anotherGameObject).setIsStopped(true);
                                        ((BreakableWall) currentGameObject).damageWall();
                                        break;
                                    case "bullet":
                                        ((Bullet) anotherGameObject).setVisible(false);
                                        ((BreakableWall) currentGameObject).damageWall();
                                        break;
                                    case "default":
                                        break;
                                }

                                break;
                        }*/


                    }
                }
            }
        }
        return gameObjects;
    }
}
