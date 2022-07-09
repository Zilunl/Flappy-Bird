import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The Bird class.
 */
public class Bird {
    private final Image LEVEL0_WING_DOWN_IMAGE = new Image("res/level-0/birdWingDown.png");
    private final Image LEVEL0_WING_UP_IMAGE = new Image("res/level-0/birdWingUp.png");
    private final Image LEVEL1_WING_DOWN_IMAGE = new Image("res/level-1/birdWingDown.png");
    private final Image LEVEL1_WING_UP_IMAGE = new Image("res/level-1/birdWingUp.png");
    private final double X = 200;
    private final double FLY_SIZE = 6;
    private final double FALL_SIZE = 0.4;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private int frameCount = 0;
    private double y;
    private double yVelocity;
    private Rectangle boundingBox;
    private boolean isCatchWeapon = false;

    /**
     * Class constructor instantiates a new Bird.
     */
    public Bird() {
        y = INITIAL_Y;
        yVelocity = 0;
        boundingBox = LEVEL0_WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
    }

    /**
     * Update bird.
     *
     * @param input     the input
     * @param isLevelUp the is level up
     * @return the rectangle
     */
    public Rectangle update(Input input, boolean isLevelUp) {
        Image birdDown, birdUp;
        // choose bird image depend on level
        if (!isLevelUp) {
            birdDown = LEVEL0_WING_DOWN_IMAGE;
            birdUp = LEVEL0_WING_UP_IMAGE;
        } else {
            birdDown = LEVEL1_WING_DOWN_IMAGE;
            birdUp = LEVEL1_WING_UP_IMAGE;
        }
        frameCount += 1;
        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;
            birdDown.draw(X, y);
        }
        else {
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);
            if (frameCount % SWITCH_FRAME == 0) {
                birdUp.draw(X, y);
                boundingBox = birdUp.getBoundingBoxAt(new Point(X, y));
            }
            else {
                birdDown.draw(X, y);
                boundingBox = birdDown.getBoundingBoxAt(new Point(X, y));
            }
        }
        y += yVelocity;

        return boundingBox;
    }

    /**
     * Gets box.
     *
     * @return the box
     */
    public Rectangle getBox() {
        return boundingBox;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return X;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Is catch weapon boolean.
     *
     * @return the boolean
     */
    public boolean isCatchWeapon() {
        return isCatchWeapon;
    }

    /**
     * Sets catch weapon.
     *
     * @param catchWeapon the catch weapon
     */
    public void setCatchWeapon(boolean catchWeapon) {
        isCatchWeapon = catchWeapon;
    }
}
