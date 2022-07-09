import bagel.Image;

/**
 * The Life bar Class.
 */
public class LifeBar {
    private final Image EMPTY_HEART_IMAGE = new Image("res/level/noLife.png");
    private final Image FULL_HEART_IMAGE = new Image("res/level/fullLife.png");
    private final int LEVEL0_MAX_LIFE = 3;
    private final double FIRST_LIFE_X = 100;
    private final double FIRST_LIFE_Y = 15;
    private final double LIFE_SPACE = 50;
    private int totalLife;
    private int leftLife;
    private double lifeX;
    private double lifeY;

    /**
     * Class constructor instantiates a new Life bar.
     */
    public LifeBar() {
        totalLife = LEVEL0_MAX_LIFE;
        leftLife = LEVEL0_MAX_LIFE;
    }

    /**
     * Update.
     */
    public void update() {
        renderLifeBar();
    }

    /**
     * Render life bar.
     */
    public void renderLifeBar() {
        lifeX = FIRST_LIFE_X;
        lifeY = FIRST_LIFE_Y;
        for (int i=0; i<totalLife; i++) {
            if (i < leftLife) {
                FULL_HEART_IMAGE.drawFromTopLeft(lifeX, lifeY);
            } else {
                EMPTY_HEART_IMAGE.drawFromTopLeft (lifeX, lifeY);
            }
            lifeX = lifeX + LIFE_SPACE;
        }
    }

    /**
     * Sets total life.
     *
     * @param totalLife the total life
     */
    public void setTotalLife(int totalLife) {
        this.totalLife = totalLife;
    }

    /**
     * Gets left life.
     *
     * @return the left life
     */
    public int getLeftLife() {
        return leftLife;
    }

    /**
     * Sets left life.
     *
     * @param leftLife the left life
     */
    public void setLeftLife(int leftLife) {
        this.leftLife = leftLife;
    }
}
