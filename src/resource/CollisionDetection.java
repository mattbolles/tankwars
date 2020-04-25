package resource;// used to detect whether a collision happens between two game objects
// processes all collision detecting for gameobject

import gameobject.GameObject;
import gameobject.Tank;
import gameobject.powerup.SpeedBoost;
import gameobject.wall.BreakableWall;
import gameobject.wall.UnBreakableWall;
import gameobject.weapon.Bullet;

import java.util.ArrayList;

public class CollisionDetection{
    boolean collisionDetected = false;

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
            for (int anotherGameObjectIndex = 0; anotherGameObjectIndex < gameObjects.size(); anotherGameObjectIndex++) {
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
                                ((Tank) currentGameObject).setIsStopped(true);
                                ((Tank) anotherGameObject).setIsStopped(true);
                            }

                            else if (anotherGameObject instanceof Bullet) {
                                String tankOwner = ((Tank) currentGameObject).getOwner();
                                String bulletOwner = ((Bullet) anotherGameObject).getOwner();
                                // if the bullet is not from the same tank, damage that tank
                                if (!bulletOwner.equals(tankOwner)) {
                                    ((Tank) currentGameObject).damageTank(5);
                                    ((Bullet) anotherGameObject).setVisible(false);
                                }

                            }

                            else if (anotherGameObject instanceof UnBreakableWall) {
                                ((Tank) currentGameObject).setIsStopped(true);
                            }

                            else if (anotherGameObject instanceof BreakableWall) {
                                ((Tank) currentGameObject).setIsStopped(true);
                                ((BreakableWall) anotherGameObject).damageWall(5);
                            }

                            else if (anotherGameObject instanceof SpeedBoost) {
                                ((SpeedBoost) anotherGameObject).setOwner(((Tank) currentGameObject).getOwner());
                                ((SpeedBoost) anotherGameObject).setActive(true);
                                ((SpeedBoost) anotherGameObject).setVisible(false);
                                ((Tank) currentGameObject).setCurrentPowerUp("speedBoost");
                                ((Tank) currentGameObject).hasPowerUp = true;
                                ((SpeedBoost) anotherGameObject).setPowerUpTickCount(0);
                                ((Tank) currentGameObject).setCurrentPowerUpTickCount(0);

                            }
                        }

                        // if bullet collides with something
                        else if (currentGameObject instanceof Bullet) {
                            if (anotherGameObject instanceof Tank) {
                                String bulletOwner = ((Bullet) currentGameObject).getOwner();
                                String tankOwner = ((Tank) anotherGameObject).getOwner();
                                // if the bullet is not from the same tank, damage that tank
                                if (!bulletOwner.equals(tankOwner)) {
                                    ((Tank) anotherGameObject).damageTank(5);
                                    ((Bullet) currentGameObject).setVisible(false);
                                }
                            }

                            else if (anotherGameObject instanceof Bullet) {
                                ((Bullet) currentGameObject).setVisible(false);
                                ((Bullet) anotherGameObject).setVisible(false);
                            }

                            else if (anotherGameObject instanceof UnBreakableWall) {
                                ((Bullet) currentGameObject).setVisible(false);
                            }

                            else if (anotherGameObject instanceof BreakableWall) {
                                ((Bullet) currentGameObject).setVisible(false);
                                ((BreakableWall) anotherGameObject).damageWall(5);
                            }
                        }
                    }
                }
            }
        }
        return gameObjects;
    }
}
