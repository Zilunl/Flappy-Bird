import bagel.*;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 * Some codes modified project1 solution written by Betty Lin
 * 
 * Please filling your name below
 *
 * @author: Zilun Li
 */
public class ShadowFlap extends AbstractGame {
    private final Image LEVEL0_BACKGROUND_IMAGE = new Image("res/level-0/background.png");
    private final Image LEVEL1_BACKGROUND_IMAGE = new Image("res/level-1/background.png");
    private final Image PLASTIC_PIPE = new Image("res/level/plasticPipe.png");
    private final Image STEEL_PIPE = new Image("res/level-1/steelPipe.png");
    private final Image BOMB_IMAGE = new Image("res/level-1/bomb.png");
    private final Image ROCK_IMAGE = new Image("res/level-1/rock.png");
    private final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final String LEVEL_UP_MSG = "LEVEL UP!";
    private final String SHOOT_MSG = "PRESS 'S' TO SHOOT";

    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private final int SCORE_X = 100;
    private final int SCORE_Y = 100;
    private final int MIN_SCALE = 1;
    private final int MAX_SCALE = 5;
    private final int LEVEL_UP_FRAME = 150;
    private final int LEVEL0_SCORE = 10;
    private final int LEVEL1_SCORE = 30;
    private final int LEVEL1_MAX_LIFE = 6;
    private final int INITIAL_PIPE_FRAME = 100;
    private final int INITIAL_WEAPON_FRAME = 150;
    private final double BIRD_Y = 350;
    private Bird bird;
    private LifeBar lifeBar;
    private int score;
    private int frameCount = 0;
    private int timeScale = 1;
    private int levelUpCount = 0;
    private int pipeFrame;
    private int weaponFrame;
    private boolean gameOn;
    private boolean gameWin;
    private boolean isLevelUp;
    private boolean levelUpScreen;

    private ArrayList<PipeSet> level0_PipeSet = new ArrayList<>();
    private ArrayList<PipeSet> level1_PipeSet = new ArrayList<>();
    private ArrayList<Weapon> level1_Weapon = new ArrayList<>();

    /**
     * Class constructor instantiates a new Shadow flap.
     */
    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new Bird();
        lifeBar = new LifeBar();
        score = 0;
        pipeFrame = INITIAL_PIPE_FRAME;
        weaponFrame = INITIAL_WEAPON_FRAME;
        gameOn = false;
        gameWin = false;
        isLevelUp = false;
    }

    /**
     * The entry point for the program.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {

        // game background
        if (!isLevelUp){
            LEVEL0_BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        } else {
            LEVEL1_BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started
        if (!gameOn) {
            renderInstructionScreen(input);
        }

        // game over
        if (birdOutOfBound()) {
            if (lifeBar.getLeftLife() > 0) {
                lifeBar.setLeftLife(lifeBar.getLeftLife() - 1);
                bird.setY(BIRD_Y);
            }
        }

        // game won
        if (gameWin) {
            renderWinScreen();
        }

        // game over
        if (lifeBar.getLeftLife() == 0) {
            renderGameOverScreen();
        }

        // game is active
        if (gameOn && !levelUpScreen && lifeBar.getLeftLife() != 0 && !gameWin && !birdOutOfBound()) {
            bird.update(input, isLevelUp);
            Rectangle birdBox = bird.getBox();
            // level0 pipe
            if (!isLevelUp) {
                updateTimeScale(input);
                if (frameCount % pipeFrame == 0) {
                    level0_PipeSet.add(new PlasticPipeSet(PLASTIC_PIPE, isLevelUp));
                }
                updatePipeSet(level0_PipeSet, birdBox);
                removePipe(level0_PipeSet);
            }
            // level1 pipe and weapon
            else {
                updateTimeScale(input);
                if (frameCount % pipeFrame == 0) {
                    randomPipe();
                }
                if (frameCount % weaponFrame == 0) {
                    randomWeapon();
                    level1_Weapon.get(level1_Weapon.size() - 1).setOverlap(detectOverlap(level1_Weapon.get(level1_Weapon.size() - 1).getWeaponBox(), level1_PipeSet));
                }
                updatePipeSet(level1_PipeSet, birdBox);
                updateWeapon(birdBox, input);
                removePipe(level1_PipeSet);
                removeWeapon(level1_Weapon);
            }
            frameCount++;
            lifeBar.update();
        }

        // game level up
        if (levelUpScreen && !isLevelUp) {
            renderLevelUpScreen();
            levelUpCount++;
            if (levelUpCount == LEVEL_UP_FRAME) {
                newGame();
            }
        }

    }

    /**
     * Check if bird out of bound boolean.
     *
     * @return the boolean
     */
    public boolean birdOutOfBound() {
        return (bird.getY() > Window.getHeight()) || (bird.getY() < 0);
    }

    /**
     * Render instruction screen.
     *
     * @param input the input
     */
    public void renderInstructionScreen(Input input) {
        // paint the instruction on screen
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        if (isLevelUp) {
            FONT.drawString(SHOOT_MSG, (Window.getWidth()/2.0-(FONT.getWidth(SHOOT_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
        }
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
    }

    /**
     * Render game over screen.
     */
    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * Render win screen.
     */
    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * Render level up screen.
     */
    public void renderLevelUpScreen() {
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }


    /**
     * Detect if there is a collision.
     *
     * @param mainBox   the main box
     * @param topBox    the top box
     * @param bottomBox the bottom box
     * @return the boolean
     */
    public boolean detectCollision(Rectangle mainBox, Rectangle topBox, Rectangle bottomBox) {
        // check for collision
        return mainBox.intersects(topBox) ||
                mainBox.intersects(bottomBox);
    }

    /**
     * Detect if weapon overlap with pipe.
     *
     * @param weaponBox the weapon box
     * @param pipeSets  the pipe sets
     * @return the boolean
     */
    public boolean detectOverlap(Rectangle weaponBox, ArrayList<PipeSet> pipeSets) {
        // check for overlap between weapon and pipes
        for (PipeSet pipeSet: pipeSets) {
            Rectangle topPipeBox = pipeSet.getTopBox();
            Rectangle bottomPipeBox = pipeSet.getBottomBox();
            boolean overlap = detectCollision(weaponBox, topPipeBox, bottomPipeBox);
            if (overlap) {
                return true;
            }
        }
        return false;
    }

    /**
     * Detect whether the shot weapon can eliminate the corresponding pipe  .
     *
     * @param weapon the weapon
     */
    public void detectCollisionWeapon(Weapon weapon) {
        Rectangle weaponBox = weapon.getWeaponBox();
        for (PipeSet pipeSet: level1_PipeSet) {
            Rectangle topPipeBox = pipeSet.getTopBox();
            Rectangle bottomPipeBox = pipeSet.getBottomBox();
            if (!weapon.isDestroy() && weapon.isShoot()) {
                boolean weaponCollision = detectCollision(weaponBox, topPipeBox, bottomPipeBox);
                // weapon rock's target pipe is plastic
                if (weapon.getWEAPON_IMAGE().equals(ROCK_IMAGE) && pipeSet.getPIPE_IMAGE().equals(PLASTIC_PIPE) && weaponCollision) {
                    weapon.setDestroy(true);
                    pipeSet.setCollision(true);
                    pipeSet.setAddScore(true);
                    score += 1;
                }
                // weapon bomb can eliminate both type of pipes
                if (weapon.getWEAPON_IMAGE().equals(BOMB_IMAGE) && weaponCollision) {
                    weapon.setDestroy(true);
                    pipeSet.setCollision(true);
                    pipeSet.setAddScore(true);
                    score += 1;
                }
            }
        }
    }

    /**
     * Update score.
     *
     * @param pipeSet the pipe set
     */
    public void updateScore(PipeSet pipeSet) {
        if (bird.getX() > pipeSet.getTopBox().right() && !pipeSet.getAddScore() && !pipeSet.getCollision()) {
            score += 1;
            pipeSet.setAddScore(true);
        }
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, SCORE_X, SCORE_Y);

        // detect win for each level
        if (score == LEVEL0_SCORE && !isLevelUp) {
            levelUpScreen = true;
        }
        if (score == LEVEL1_SCORE && isLevelUp) {
            gameWin = true;
        }
    }

    /**
     * Update pipe and life.
     *
     * @param pipeSets the pipe sets
     * @param birdBox  the bird box
     */
    public void updatePipeSet(ArrayList<PipeSet> pipeSets, Rectangle birdBox) {
        for (PipeSet pipeSet: pipeSets) {
            pipeSet.update(timeScale);
            Rectangle topPipeBox = pipeSet.getTopBox();
            Rectangle bottomPipeBox = pipeSet.getBottomBox();
            boolean pipeCollision = detectCollision(birdBox, topPipeBox, bottomPipeBox);
            // lose 1 life if there is a collision
            if (pipeCollision && !pipeSet.getCollision()) {
                lifeBar.setLeftLife(lifeBar.getLeftLife() - 1);
                pipeSet.setCollision(true);
            }
            updateScore(pipeSet);

            // lose 1 left if bird collide with pipe or flame
            if (pipeSet.getPIPE_IMAGE().equals(STEEL_PIPE)) {
                SteelPipeSet steelPipeSet = (SteelPipeSet) pipeSet;
                Rectangle topFlameBox = steelPipeSet.getTopFlameBox();
                Rectangle bottomFlameBox = steelPipeSet.getBottomFlameBox();
                // only collide when flame is shooting
                if (steelPipeSet.isFlameShoot()) {
                    boolean flameCollision = detectCollision(birdBox, topFlameBox, bottomFlameBox);
                    if (flameCollision && !steelPipeSet.isFlameCollision()) {
                        lifeBar.setLeftLife(lifeBar.getLeftLife() - 1);
                        steelPipeSet.setFlameCollision(true);
                        pipeSet.setCollision(true);
                    }
                }
            }
        }
    }

    /**
     * Update weapon and check if it is caught.
     *
     * @param birdBox the bird box
     * @param input   the input
     */
    public void updateWeapon(Rectangle birdBox, Input input) {
        for (Weapon weapon: level1_Weapon) {
            weapon.update(input, bird, timeScale);
            Rectangle weaponBox = weapon.getWeaponBox();
            if (birdBox.intersects(weaponBox) && !weapon.isPickedUp() && !weapon.isOverlap()) {
                if (!bird.isCatchWeapon()) {
                    bird.setCatchWeapon(true);
                    weapon.setPickedUp(true);
                }
            }
            detectCollisionWeapon(weapon);
        }
    }

    /**
     * Update time scale.
     *
     * @param input the input
     */
    public void updateTimeScale(Input input) {
        if (input.wasPressed(Keys.L) && timeScale < MAX_SCALE) {
            timeScale += 1;
            pipeFrame = (int) Math.round(pipeFrame / 1.5);
            weaponFrame = (int) Math.round(pipeFrame / 1.5);
        }
        if (input.wasPressed(Keys.K) && timeScale > MIN_SCALE) {
            timeScale -= 1;
            pipeFrame = (int) Math.round(pipeFrame * 1.5);
            weaponFrame = (int) Math.round(pipeFrame * 1.5);
        }
    }

    /**
     * Random add pipe at level1.
     */
    public void randomPipe() {
        Random random = new Random();
        if (random.nextBoolean()) {
            level1_PipeSet.add(new PlasticPipeSet(PLASTIC_PIPE, isLevelUp));
        } else {
            level1_PipeSet.add(new SteelPipeSet(STEEL_PIPE, isLevelUp));
        }
    }

    /**
     * Random add weapon at level1.
     */
    public void randomWeapon() {
        Random random = new Random();
        if (random.nextBoolean()) {
            level1_Weapon.add(new Bomb(BOMB_IMAGE));
        } else {
            level1_Weapon.add(new Rock(ROCK_IMAGE));
        }
    }

    /**
     * Set a new game.
     */
    public void newGame(){
        isLevelUp = true;
        gameOn = false;
        levelUpScreen = false;
        timeScale = 1;
        lifeBar.setTotalLife(LEVEL1_MAX_LIFE);
        lifeBar.setLeftLife(LEVEL1_MAX_LIFE);
        score = 0;
        frameCount = 0;
        pipeFrame = INITIAL_PIPE_FRAME;
        weaponFrame = INITIAL_WEAPON_FRAME;
        bird.setY(BIRD_Y);
    }

    /**
     * Remove pipe ensure do not have much memory leak.
     *
     * @param pipeSets the pipe sets
     */
    public void removePipe(ArrayList<PipeSet> pipeSets) {
        for (int i=0; i<pipeSets.size(); i++) {
            PipeSet pipeSet = pipeSets.get(i);
            // remove the pipe out of window
            if (pipeSet.getTopBox().right() < 0) {
                pipeSets.remove(pipeSet);
                i--;
            }
        }
    }

    /**
     * Remove weapon ensure do not have much memory leak.
     *
     * @param weapons the weapons
     */
    public void removeWeapon(ArrayList<Weapon> weapons) {
        for (int i=0; i<weapons.size(); i++) {
            Weapon weapon = weapons.get(i);
            // remove the weapon out of window
            if (weapon.getWeaponBox().right() < 0) {
                weapons.remove(weapon);
                i--;
            }
        }
    }
}
