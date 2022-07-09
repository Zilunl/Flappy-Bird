import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The type Steel pipe set.
 */
public class SteelPipeSet extends PipeSet {
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private final double FLAME_FRAME = 30;
    private final double SWITCH_FRAME = 20;
    private final double FLAME_GAP = 10;
    private int frameCount = 0;
    private int frameLastTime = 0;
    private boolean flameCollision = false;
    private boolean flameShoot = false;

    /**
     * Instantiates a new Steel pipe set.
     *
     * @param pipeImage the pipe image
     * @param isLevelUp the is level up
     */
    public SteelPipeSet(Image pipeImage, boolean isLevelUp){
        super(pipeImage, isLevelUp);
    }

    public void update(int timeScale) {
        frameCount += 1;
        if (frameCount % SWITCH_FRAME == 0) {
            flameShoot = true;
        }
        if (!super.getCollision() && !flameCollision && frameLastTime < FLAME_FRAME && flameShoot) {
            shootFrame();
            frameLastTime += 1;
            if (frameLastTime == FLAME_FRAME) {
                frameLastTime = 0;
                flameShoot = false;
            }
        }
        super.update(timeScale);
    }

    /**
     * Shoot frame.
     */
    public void shootFrame() {
        if (!flameCollision) {
            FLAME_IMAGE.draw(super.getPipeX(), super.getTopBox().bottom() + FLAME_GAP);
            FLAME_IMAGE.draw(super.getPipeX(), super.getBottomBox().top() - FLAME_GAP, ROTATOR);
        }
    }

    /**
     * Gets top flame box.
     *
     * @return the top flame box
     */
    public Rectangle getTopFlameBox() {
        return FLAME_IMAGE.getBoundingBoxAt(new Point(super.getPipeX(), super.getTopBox().bottom() + FLAME_GAP));
    }

    /**
     * Gets bottom flame box.
     *
     * @return the bottom flame box
     */
    public Rectangle getBottomFlameBox() {
        return FLAME_IMAGE.getBoundingBoxAt(new Point(super.getPipeX(), super.getBottomBox().top() - FLAME_GAP));
    }

    /**
     * Is flame collision boolean.
     *
     * @return the boolean
     */
    public boolean isFlameCollision() {
        return flameCollision;
    }

    /**
     * Sets flame collision.
     *
     * @param flameCollision the flame collision
     */
    public void setFlameCollision(boolean flameCollision) {
        this.flameCollision = flameCollision;
    }

    /**
     * Is flame shoot boolean.
     *
     * @return the boolean
     */
    public boolean isFlameShoot() {
        return flameShoot;
    }
}
