package resource;

import game.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static javax.imageio.ImageIO.read;

public class Resource {
    private static Map<String, BufferedImage> resources;

    static {
        Resource.resources = new HashMap<>();
        try {
        Resource.resources.put("tankOne", read(TankGame.class.getClassLoader().getResource("images/tank1.png")));
        Resource.resources.put("tankTwo", read(TankGame.class.getClassLoader().getResource("images/tank2.png")));
        Resource.resources.put("bullet", read(TankGame.class.getClassLoader().getResource("images/BulletCropped.png")));
        Resource.resources.put("explosionSmall", read(TankGame.class.getClassLoader().getResource("images/Explosion_small_still.gif")));
        Resource.resources.put("background", read(TankGame.class.getClassLoader().getResource("images/background.png")));
        Resource.resources.put("breakableWall", read(TankGame.class.getClassLoader().getResource("images/WallBreakable.gif")));
        Resource.resources.put("breakableWallDamaged", read(TankGame.class.getClassLoader().getResource("images/WallBreakableDamaged.gif")));
        Resource.resources.put("unBreakableWall", read(TankGame.class.getClassLoader().getResource("images/WallUnbreakable.gif")));
        Resource.resources.put("tank1life", read(TankGame.class.getClassLoader().getResource("images/tank1lifeicon.png")));
        Resource.resources.put("tank2life", read(TankGame.class.getClassLoader().getResource("images/tank2lifeicon" +
                ".png")));
        Resource.resources.put("logo", read(TankGame.class.getClassLoader().getResource("images/tankwarslogo.png")));
        Resource.resources.put("speedBoost", read(TankGame.class.getClassLoader().getResource("images/speedboost" +
                ".png")));
        Resource.resources.put("speedBoostIcon",
                read(TankGame.class.getClassLoader().getResource("images/speedboosticon.png")));
        } catch (IOException exception) {
            exception.printStackTrace();
            // abandon ship if resources don't work
            System.exit(-5);
        }




    }

    public static Font infoFontBold = new Font("Helvetica", Font.BOLD, 19);
    public static Font buttonFont = new Font("Helvetica", Font.BOLD, 50);
    public static Font gameOverFont = new Font("Helvetica", Font.BOLD, 35);
    public static Font creditFont = new Font("Helvetica", Font.ITALIC, 16);
    public static BufferedImage getResourceImage(String key) {
        return Resource.resources.get(key);
    }
}
