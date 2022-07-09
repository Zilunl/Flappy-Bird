import bagel.Image;
import bagel.Input;

/**
 * The type Rock.
 */
public class Rock extends Weapon{

    private final int SHOOT_RANGE = 25;

    /**
     * Instantiates a new Rock.
     *
     * @param bombImage the bomb image
     */
    public Rock(Image bombImage) {
        super(bombImage);
    }

    public void update(Input input, Bird bird, int timeScale) {
        super.update(input, bird, timeScale);
        super.renderWeapon(SHOOT_RANGE);
    }
}
