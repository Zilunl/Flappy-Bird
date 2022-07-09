import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Pipe set Class.
 */
public abstract class PipeSet {
    private final Image PIPE_IMAGE;
    private final int PIPE_GAP = 168;
    private final int HIGH_GAP = 100;
    private final int MID_GAP = 300;
    private final int LOW_GAP = 500;
    private final double TOP_PIPE_Y;
    private final double BOTTOM_PIPE_Y;
    private final double RANDOM_NUMBER;
    private final double RANGE_START = 100;
    private final double RANGE_END = 500;
    private final double INITIAL_PIPE_SPEED = 5;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();
    private double pipeSpeed;
    private boolean collision = false;
    private boolean addScore = false;
    private List<Integer> pipeGaps = new ArrayList<>();

    /**
     * Instantiates a new Pipe set.
     *
     * @param pipeImage the pipe image
     * @param isLevelUp the is level up
     */
    public PipeSet(Image pipeImage, boolean isLevelUp) {
        PIPE_IMAGE = pipeImage;
        if (!isLevelUp) {
            pipeGaps.add(HIGH_GAP);
            pipeGaps.add(MID_GAP);
            pipeGaps.add(LOW_GAP);
            Random random = new Random();
            RANDOM_NUMBER = pipeGaps.get(random.nextInt(pipeGaps.size()));
        } else {
            double random = new Random().nextDouble();
            RANDOM_NUMBER = RANGE_START + random * (RANGE_END - RANGE_START);
        }
        TOP_PIPE_Y = RANDOM_NUMBER - PIPE_IMAGE.getHeight() / 2.0;
        BOTTOM_PIPE_Y = RANDOM_NUMBER + PIPE_IMAGE.getHeight() / 2.0 + PIPE_GAP;
    }

    /**
     * Update.
     *
     * @param timeScale the time scale
     */
    public void update(int timeScale) {
        renderPipeSet();
        updateSpeed(timeScale);
    }

    /**
     * Render pipe set.
     */
    public void renderPipeSet(){
        if (!collision && getTopBox().right() > 0) {
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
        }
    }

    /**
     * Update speed.
     *
     * @param timeScale the time scale
     */
    public void updateSpeed(int timeScale) {
        pipeSpeed = INITIAL_PIPE_SPEED;
        for (int i=1; i<timeScale; i++) {
            pipeSpeed = pipeSpeed * 1.5;
        }
        pipeX -= pipeSpeed;
    }

    /**
     * Gets top box.
     *
     * @return the top box
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));
    }

    /**
     * Gets bottom box.
     *
     * @return the bottom box
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));
    }

    /**
     * Gets collision.
     *
     * @return the collision
     */
    public boolean getCollision() {
        return collision;
    }

    /**
     * Sets collision.
     *
     * @param collision the collision
     */
    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    /**
     * Gets add score.
     *
     * @return the add score
     */
    public boolean getAddScore() {
        return addScore;
    }

    /**
     * Sets add score.
     *
     * @param addScore the add score
     */
    public void setAddScore(boolean addScore) {
        this.addScore = addScore;
    }

    /**
     * Gets pipe x.
     *
     * @return the pipe x
     */
    public double getPipeX() {
        return pipeX;
    }

    /**
     * Gets pipe image.
     *
     * @return the pipe image
     */
    public Image getPIPE_IMAGE() {
        return PIPE_IMAGE;
    }
}
