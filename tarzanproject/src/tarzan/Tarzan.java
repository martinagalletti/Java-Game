package tarzan;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import tilegame.*;

import map.Map;
import notmoving.*;

/**
 * @author Faustine & Martina
 * 
 *         The Tarzan class represents the player. It implements KeyListener in
 *         order to move him around. Tarzan has skills (endurance and strength),
 *         energy, and the number of jaguars (opponents) he has killed. He can
 *         move around the board to interact with NotLivings. Moving cost
 *         energy, and going on a WaterTile decreases his energy even more
 *         (swimming is exhausting!) The game updates (change in position,
 *         skills, energy...) are handled here. The the end of the game (victory
 *         or defeat) is managed here. He can find knives to increase his
 *         strength, eat bananas for endurance, and take Kavuru's pills for
 *         energy. To win the game, Tarzan must be strong and endurant enough +
 *         have killed enough jaguars (to impress Jane). If Jane is found and
 *         the goals are met, the game is won and a new game can be started from
 *         the StartPanel. If Tarzan runs out of energy, the game is lost and a
 *         new game can be started from the Start panel. The final score depends
 *         on the number of bananas eaten, the number of knives found, the
 *         number of jaguars killed and the energy remaining at the end of the
 *         game.
 * 
 *         Since a lot of the game is handled here, Tarzan has an Handler
 *         attribute to make it easier to access Game, Map, and World
 *         attributes/methods for updates.
 * 
 *         Most updates and interactions are handled in the keyPressed method,
 *         as when no keys are pressed, nothing should be happening (no need to
 *         call the methods at each time step if no movement was detected).
 * 
 */

//TODO: implement scores for high scores

public class Tarzan implements KeyListener {
	private final static int SPEED = 1; // variable speed of Tarzan
	private final static int ENERGY_LOSS = 1; // variable for setting energy lost by Tarzan moving into the jungle
	private final static int WATER_ENERGY_LOSS = 10; // variable for setting the energy water when he need to swim
	public final static int FIELD_OF_VIEW_RADIUS = 2; // variable for setting the field of view of Tarzan
	public final static int INITIAL_ENERGY = 500; // variable for setting his initial energy
	private int energy; // variable energy declared
	private int endurance; // variable endurance declared
	private int strength; // variable strength declared
	private int jaguarsKilled; // variable Jaguars who are killed declared
	private Position2D tarzanPosition; // variable Tarzan position
	private Handler handler; // handler declared
	private int speed; // variable speed of Tarzan
	private int score; // variable score of Tarzan

	private boolean[] keys;

	/**
	 * Constructor.
	 * 
	 * @param pos, handler, level, strength, endurance
	 */
	public Tarzan(Position2D pos, Handler handler, Level level, int strength, int endurance) {
		this.handler = handler;
		tarzanPosition = pos;
		energy = INITIAL_ENERGY;
		this.strength = strength;
		this.endurance = endurance;
		jaguarsKilled = 0;
		speed = SPEED;
		keys = new boolean[256];
		score = 0;
	}

	/**
	 * Getter.
	 * 
	 * @return tarzanPosition
	 */
	public Position2D getTarzanPosition() {
		return tarzanPosition;
	}

	/**
	 * Setter.
	 * 
	 * @param tarzanPosition
	 */
	public void setTarzanPosition(Position2D tarzanPosition) {
		this.tarzanPosition = tarzanPosition;
	}

	/**
	 * Setter.
	 * 
	 * @param x, y
	 */
	public void setTarzanPosition(int x, int y) {
		if (x > Map.SIZE_MAP) {
			x = Map.SIZE_MAP - 1;
		}
		if (x < 0) {
			x = 0;
		}
		if (y > Map.SIZE_MAP) {
			y = Map.SIZE_MAP - 1;
		}
		if (y < 0) {
			y = 0;
		}
		tarzanPosition.set(x, y);
	}

	/**
	 * Returns true if Tarzan is on a WaterTile.
	 * 
	 * @return boolean
	 */
	private boolean inTheWater() {
		return (handler.getHandlerWorld().getWorldTiles()[tarzanPosition.getY()][tarzanPosition.getX()] == 1);
	}

	/**
	 * Interaction with Jaguar: loss of energy. Tarzan might die.
	 */
	public void takeDamage() {
		// manipulate the amount of damage taken
		if (Jaguar.JAGUAR_STRENGTH >= energy) {
			energy = 0;
			System.out.println("This is the end of the game!");
		} else {
			energy -= Jaguar.JAGUAR_STRENGTH;
		}
	}

	/**
	 * Interaction with Banana: score and endurance increased.
	 */
	public void eatBanana() {
		endurance += Banana.ENDURANCE_GIVEN;
		score += Banana.ENDURANCE_GIVEN;
	}

	/**
	 * Interaction with Knife: score and strength increased.
	 */
	public void pickKnife() {
		strength += Knife.STRENGTH_GIVEN;
		score += Knife.STRENGTH_GIVEN;
	}

	/**
	 * Interaction with Kavurus: energy increased.
	 */
	public void takePill() {
		energy += Kavurus.ENERGY_GIVEN; // no idea how much
	}

	/**
	 * Interaction with Jane: if goals are met, game won.
	 */
	public void janeFound() {
		if (areGoalsMet()) {
			score += energy;
			endOfGameWin();
		} else {
			System.out.println("Meet the goals and come find me later!");
		}
	}

	/**
	 * Returns true if goals are met.
	 * 
	 * @return boolean
	 */
	private boolean areGoalsMet() {
		if (strength >= handler.getHandlerMap().getMapLevel().getGoalStrength()
				&& endurance >= handler.getHandlerMap().getMapLevel().getGoalEndurance()
				&& jaguarsKilled >= handler.getHandlerMap().getMapLevel().getGoalJaguars()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Getter.
	 * 
	 * @return handler
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * Getter.
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Getter.
	 * 
	 * @return energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * Getter.
	 * 
	 * @return endurance
	 */
	public int getEndurance() {
		return endurance;
	}

	/**
	 * Getter.
	 * 
	 * @return strength
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * Getter.
	 * 
	 * @return jaguarsKilled
	 */
	public int getJaguarsKilled() {
		return jaguarsKilled;
	}

	/**
	 * Increment jaguarsKilled by 1.
	 */
	public void killsJaguar() {
		jaguarsKilled += 1;
	}

	/**
	 * End of game - player lost. New game application.
	 */
	private void endOfGameLost() {
		handler.getHandlerGame().getGameApp().newJOptionPane("Sorry, you lost :("); // add score
		handler.getHandlerGame().init(); // new Game --> back to start
	}

	/**
	 * End of game - player won. New game application.
	 */
	private void endOfGameWin() {
		handler.getHandlerGame().getGameApp().newJOptionPane("Congrats, you win :D"); // add score
		handler.getHandlerGame().init(); // new Game --> back to start
	}

	/**
	 * Update.
	 */
	public void tick() {
		handler.getHandlerGame().getGameApp().getGamePanel().updateGameSettings(strength, endurance, energy,
				jaguarsKilled, handler.getHandlerMap().getMapLevel().getGoalStrength(),
				handler.getHandlerMap().getMapLevel().getGoalEndurance(),
				handler.getHandlerMap().getMapLevel().getGoalJaguars());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < 0 || e.getKeyCode() >= keys.length)
			return;
		keys[e.getKeyCode()] = true;

		if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {
			tarzanPosition.setY(Math.max(0, tarzanPosition.getY() - speed));
			energy -= ENERGY_LOSS * (20 - endurance / 10);
		}
		if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) {
			tarzanPosition.setY(Math.min(Map.SIZE_MAP - 1, tarzanPosition.getY() + speed));
			energy -= ENERGY_LOSS * (15 - endurance / 10);
		}
		if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
			tarzanPosition.setX(Math.max(0, tarzanPosition.getX() - speed));
			energy -= ENERGY_LOSS * (15 - endurance / 10);
		}
		if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
			tarzanPosition.setX(Math.min(Map.SIZE_MAP - 1, tarzanPosition.getX() + speed));
			energy -= ENERGY_LOSS * (15 - endurance / 10);
		}

		if (inTheWater()) {
			energy -= WATER_ENERGY_LOSS;
		}

		if (energy <= 0) {
			endOfGameLost();
		}

		if (handler.getHandlerWorld().getWorldNotMovings(tarzanPosition) != null) {
			System.out.println("Non moving encountered");
			handler.getHandlerWorld().getWorldNotMovings(tarzanPosition).interact(this);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < 0 || e.getKeyCode() >= keys.length)
			return;
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
