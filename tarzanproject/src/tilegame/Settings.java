package tilegame;
// insert the Gui

public class Settings {
	private static int initialStrength;
	private static int initialEndurance;
	private static int InitialEnergy; 
	private static int level;
	public static Object lvl;
	
	// Constructor with three int or setters ?
	// get these values from GUI --> how?
	
	public static int getInitialEndurance() {
		return initialEndurance;
	}
	
	public static int getInitialStrength() {
		return initialStrength;
	}
	
	public static int getLevel() {
		return level;
	}
	
	public static int getInitialEnergy() {
		return InitialEnergy;
	}
}
