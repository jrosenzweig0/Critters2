package assignment5;

/**
 * Critter3 chooses a direction and moves in that direction every turn. When the constructor is called there is a
 * 50% chance that the critter gets a reproduce energy of 50 and a 50% chance that it gets a reproduce energy of 200. 
 * This is so that some of the critters will be strong and win fights and others will be weak and used solely for the purpose of reproduction. 
 * The critters with 50 energy are assigned the toString value of b and the ones with 200 reproduce energy have the toString value of B.  
 * The critter waits until the percentage of Algae on the screen is 6% before it starts moving.
 * @author jonathan
 *
 */
public class Critter3 extends Critter.TestCritter {
	private int reproduceLimit; //the energy it needs to have for it to reproduce 
	private double algae; // the percentage of algae in the world. a number between 0 and 1
	private boolean hasmoved; //says whether or not a critter has moved
	public int direction; //the direction that the critter will move in
	/**
	 * Constructor method for Critter3 it sets the parameters
	 */
	public Critter3() {
		if(getRandomInt(2) == 0) {
			reproduceLimit=50;
		}
		else {
			reproduceLimit = 200;
		}
		direction = getRandomInt(8);
	}

	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }

	public CritterShape viewShape(){
		return CritterShape.CIRCLE;
	}
	 
	@Override
	/**
	 * This method does the time step for the critter.
	 */
	public void doTimeStep() {
		hasmoved = false;
		try {
			algae = ((double) getInstances("assignment4.Algae").size())/((double)(Params.world_height*Params.world_width));
		} catch (InvalidCritterException e) {
			algae = 1.0;
		}
		if(algae > 0.06) {
			walk(direction);
			hasmoved = true;
		}
		
		if (getEnergy() > reproduceLimit) {
			Critter3 child = new Critter3();
			reproduce(child, getRandomInt(8));
		}
		


	}

	@Override
	/**
	 * this method determines if the critter is going to fight or run if it encounters another critter
	 * if it returns true it will fight otherwise 
	 * @return returns a boolean for if it will fight or try and run
	 */
	public boolean fight(String opponent) {
		return true;
	}

	@Override
	/**
	 * this is the to string method for the critter
	 * @return returns a character representing what it will look like on the grid
	 */
	public String toString () {
		if(reproduceLimit == 50)
			return "b";
		else
			return "B";
	}
	/**
	 * This method displays some stats about the critter
	 * It prints the number of critters that can reproduce and the number of critters that are impotent
	 * @param My1s is the list of critter4s in the population list
	 */
	public static String runStats(java.util.List<Critter> My1s) {
		int total=0;
		for (int i=0; i<My1s.size(); i++){
			if(My1s.get(i).toString() == "B")
				total += 1;
		}
		int total1 = My1s.size() - total;
		return("" + My1s.size() + " total Critter3s with " + total + " B's and " + total1 + " b's");
	}

}
