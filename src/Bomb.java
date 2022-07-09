import bagel.Image;
import bagel.Input;

/**
 * The type bomb.
 */
public class Bomb extends Weapon{

    private final int SHOOT_RANGE = 50;

    /**
     * Instantiates a new Bomb.
     *
     * @param bombImage the bomb image
     */
    public Bomb(Image bombImage) {
        super(bombImage);
    }

    public void update(Input input, Bird bird, int timeScale) {
        super.update(input, bird, timeScale);
        super.renderWeapon(SHOOT_RANGE);
    }
}
