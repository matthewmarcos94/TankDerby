package com.panzerfauster;

/**
 * Created by matt on 11/11/16.
 */
public class Projectile extends Entity{
    public Projectile(
        String image_path, // Location of image
        boolean isEnemy, // Will it be hostile?
        String name,
        int xcoordinate, //xLocation on screen
        int ycoordinate,  //yLocation on screen
        int speed,
        float angle
    ) {
        super(image_path, isEnemy, name, xcoordinate, ycoordinate, speed, angle);
    }
}
