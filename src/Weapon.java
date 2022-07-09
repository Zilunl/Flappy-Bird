import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Random;

/**
 * The Weapon Class.
 */
public abstract class Weapon {
    private final Image WEAPON_IMAGE;
    private final double INITIAL_WEAPON_SPEED = 5;
    private final double SHOOT_WEAPON_SPEED = 5;
    private final double RANDOM_NUMBER;
    private final double RANGE_START = 100;
    private final double RANGE_END = 500;
    private int frameCount;
    private double weaponX = Window.getWidth();
    private double weaponY;
    private double weaponSpeed;
    private boolean isPickedUp = false;
    private boolean isShoot = false;
    private boolean isOverlap = false;
    private boolean isDestroy = false;

    /**
     * Instantiates a new Weapon.
     *
     * @param weaponImage the weapon image
     */
    public Weapon(Image weaponImage) {
        WEAPON_IMAGE = weaponImage;
        double random = new Random().nextDouble();
        RANDOM_NUMBER = RANGE_START + random * (RANGE_END - RANGE_START);
        weaponY = RANDOM_NUMBER - WEAPON_IMAGE.getHeight()/2.0;
    }

    /**
     * Update.
     *
     * @param input     the input
     * @param bird      the bird
     * @param timeScale the time scale
     */
    public void update(Input input, Bird bird, int timeScale) {
        updateSpeed(timeScale);
        frameCount += 1;
        if (!isPickedUp) {
            weaponX -= weaponSpeed;
        } else {
            if (bird.isCatchWeapon() && !isShoot) {
                if (input.wasPressed(Keys.S)) {
                    frameCount = 0;
                    bird.setCatchWeapon(false);
                    isShoot = true;
                }
                weaponX = bird.getBox().right();
                weaponY = bird.getY();
            }
        }
    }

    /**
     * Render weapon.
     *
     * @param shootRange the shoot range
     */
    public void renderWeapon(int shootRange){
        if (isShoot) {
            if (frameCount <= shootRange) {
                weaponX += SHOOT_WEAPON_SPEED;
            } else {
                isDestroy = true;
            }
        }
        if (!isDestroy && !isOverlap && getWeaponBox().right() > 0) {
            WEAPON_IMAGE.draw(weaponX, weaponY);
        }
    }

    /**
     * Update speed.
     *
     * @param timeScale the time scale
     */
    public void updateSpeed(int timeScale) {
        weaponSpeed = INITIAL_WEAPON_SPEED;
        for (int i=1; i<timeScale; i++) {
            weaponSpeed = weaponSpeed * 1.5;
        }
    }

    /**
     * Gets weapon box.
     *
     * @return the weapon box
     */
    public Rectangle getWeaponBox() {
        return WEAPON_IMAGE.getBoundingBoxAt(new Point(weaponX, weaponY));
    }

    /**
     * Is overlap boolean.
     *
     * @return the boolean
     */
    public boolean isOverlap() {
        return isOverlap;
    }

    /**
     * Sets overlap.
     *
     * @param overlap the overlap
     */
    public void setOverlap(boolean overlap) {
        isOverlap = overlap;
    }

    /**
     * Is picked up boolean.
     *
     * @return the boolean
     */
    public boolean isPickedUp() {
        return isPickedUp;
    }

    /**
     * Sets picked up.
     *
     * @param pickedUp the picked up
     */
    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    /**
     * Is destroy boolean.
     *
     * @return the boolean
     */
    public boolean isDestroy() {
        return isDestroy;
    }

    /**
     * Sets destroy.
     *
     * @param destroy the destroy
     */
    public void setDestroy(boolean destroy) {
        isDestroy = destroy;
    }

    /**
     * Is shoot boolean.
     *
     * @return the boolean
     */
    public boolean isShoot() {
        return isShoot;
    }

    /**
     * Gets weapon image.
     *
     * @return the weapon image
     */
    public Image getWEAPON_IMAGE() {
        return WEAPON_IMAGE;
    }
}
