package assignment5;

import java.lang.reflect.Constructor;
import java.util.*;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 

	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();//List of all living Critters
	private static List<Critter> babies = new java.util.ArrayList<Critter>();		//List of baby Critters yet to be added to pop
	public static Map<Integer, Map<Integer, Tile>> world = new HashMap<Integer, Map<Integer, Tile>>();  				//2D map (Since we have 2 keys: x,y) holding the tile objects
    private static Map<Integer, Map<Integer, Tile>> lastTurnWorld = new HashMap<Integer, Map<Integer, Tile>>(); 		//copy of world as it was before this worldTimeStep started
    public static boolean firstTime = true;					//lets program know to call createWorld()
	private boolean hasMoved;									//says if Critter has moved thus far this turn
	private static HashSet<String> critterTypes = new HashSet<String>() {{add("assignment5.Craig"); add("assignment5.Algae");
		add("assignment5.Critter1"); add("assignment5.Critter2"); add("assignment5.Critter3");add("assignment5.Critter4");
		add("assignment5.AlgaephobicCritter");add("assignment5.TragicCritter");}};	//holds critterTypes
	static {				// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

    /**
     * Function that lets a Critter look 1 or 2 steps in any direction
     * @param direction int from 0-7 representing the direction the Critter wants to look
     * @param steps boolean representing if the Critter wants to look 1 or 2 steps (2 if true, 1 if false)
     * @return String representing Critter on the tile we are looking at, or null if nothing is there
     */
    protected final String look(int direction, boolean steps) {
        int[] Location = findCritter(this, lastTurnWorld).getPosition();		//get Critter location before this turn started
        int distance = (steps) ? 2 : 1;
        switch (direction) {                        //add/subtract distance to coordinates, dependent on direction
            case 0:
                Location[0] += distance;
                break;
            case 1:
                Location[0] += distance;
                Location[1] -= distance;
                break;
            case 2:
                Location[1] -= distance;
                break;
            case 3:
                Location[1] -= distance;
                Location[0] -= distance;
                break;
            case 4:
                Location[0] -= distance;
                break;
            case 5:
                Location[0] -= distance;
                Location[1] += distance;
                break;
            case 6:
                Location[1] += distance;
                break;
            case 7:
                Location[1] += distance;
                Location[0] += distance;
                break;
        }
        this.energy -= Params.look_energy_cost;
        ArrayList<Critter> lookList = lastTurnWorld.get(Location[1]).get(Location[0]).crittersOnTile(); //get the list of Critters on Tile according to lastTurnWorld
        if (lookList.size()>0){
            if (lookList.size()==1) return lookList.get(0).toString();
            return ("UH IDK WHATS SUPPOSED TO HAPPEN HERE?");		//FIX THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        }
        return null;
    }
	
	/* rest is unchanged from Project 4 */


	private static java.util.Random rand = new java.util.Random();			//new RNG
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}	//func to get random ints

	/**
	 * sets Random seed so that results can be repeated
	 * @param new_seed long representing random seed
	 */
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}


	/**
	 * Gets a one-character long string that visually depicts your critter in the ASCII interface
	 * @return String representing Critter visually
	 */
	public String toString() { return "%"; }

	private int energy = 0;				//Critter livelihood

	/**
	 * Getter for energy variable
	 * @return int representing energy
	 */
	protected int getEnergy() { return energy; }

	private int x_coord;		//location
	private int y_coord;

	/**
	 * This method simulates a critter walking
	 * @param direction int representing the direction the critter moves. it is an integer
	 * between 0 and 7 where 0 represents moving right 1 represents moving right and up and so on
	 */
	protected final void walk(int direction) {
		if (this.energy > Params.walk_energy_cost) {	//if Critter has enough energy to walk, subtract walk energy and move Critter
			this.energy -= Params.walk_energy_cost;
			this.move(direction, 1);
		}
		else {											//else Critter dies
			this.energy = 0;
			this.death();
		}
	}

	/**
	 * This method simulates a critter running
	 * @param direction int representing the direction the critter moves. it is an integer
	 * between 0 and 7 where 0 represents moving right 1 represents moving right and up and so on
	 */
	protected final void run(int direction) {
		if (this.energy > Params.run_energy_cost) {	 //if Critter has enough energy to run, subtract walk energy and move Critter
			this.energy -= Params.run_energy_cost;
			this.move(direction, 2);

		}
		else {										//else Critter dies
			this.energy = 0;
			this.death();
		}
	}

	/**
	 *
	 * @param direction is an integer between 0 and 7 that specifies the direction the critter moves
	 */

	/**
	 * This method moves the critter one tile in the specified direction
	 * @param direction int between 0 and 7 specifying the direction the critter moves
	 * @param distance int representing distance the Critter moves
	 */
	private void move(int direction, int distance) {
		if(!this.hasMoved) {			//only attempt to move if Critter hasn't yet moved
			if (population.contains(this)) world.get(this.y_coord).get(this.x_coord).remove(this);
			//remove Critter from tile he was on...
			switch (direction) {						//add/subtract distance to coordinates, dependent on direction
				case 0: this.x_coord += distance; break;
				case 1: this.x_coord += distance; this.y_coord -= distance; break;
				case 2: this.y_coord -= distance; break;
				case 3: this.y_coord -= distance; this.x_coord -= distance; break;
				case 4: this.x_coord -= distance; break;
				case 5: this.x_coord -= distance; this.y_coord += distance; break;
				case 6: this.y_coord += distance; break;
				case 7: this.y_coord += distance; this.x_coord += distance; break;
			}
			this.warp();								//Ensures torus property
			if (population.contains(this)) world.get(this.y_coord).get(this.x_coord).setFilled(this);  //adds Critter to new Tile
			this.hasMoved = true;						//sets flag so it won't move again this time step
		}
	}

	/**
	 * Connects one side to the other, and top to bottom, adds torus functionality
	 */
	private void warp() {
		this.x_coord = this.x_coord % Params.world_width;		//if x exceeds world_width, however much it exceeds by is new x value
		this.y_coord = this.y_coord % Params.world_height;		//if y exceeds world_width, however much it exceeds by is new y value
		if(this.x_coord < 0)									//if x is negative new x is x + world_width
			this.x_coord = this.x_coord + Params.world_width;
		if(this.y_coord < 0)									//if y is negative new y is y + world_width
			this.y_coord = this.y_coord + Params.world_height;
	}

	/**
	 * This method takes the offspring that another critter produced and initialized it giving
	 * it half of the parents energy rounded down and putting it in a position next to
	 * the parent indicated by the direction parameter
	 * @param offspring Critter that the parent created
	 * @param direction int between 0 and 7 that represents the direction the child will go
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if (this.energy < Params.min_reproduce_energy) {	//if Critter does not have enough energy to reproduce, don't
			return;
		}
		offspring.energy = this.energy/2;					//offspring gets half of parent's energy
		this.energy = this.energy - offspring.energy;		//parent keeps half of its energy
		offspring.x_coord = this.x_coord;					//before moving offspring is at parent's location
		offspring.y_coord = this.y_coord;
		offspring.move(direction, 1);				//move the offspring away from parent, and add it to baby list
		babies.add(offspring);
	}

	/**
	 * This method effectively kills a critter
	 */
	private void death(){
		population.remove(this);							//removes from pop
		(world.get(this.y_coord).get(this.x_coord)).remove(this);		//removes from Tile list
	}

    /**
     * Function to find the tile a critter was on according to a world map
     * @param c Critter to be found
     * @param w Map of the world we are searching through
     * @return Tile c is on
     */
    private Tile findCritter(Critter c, Map<Integer, Map<Integer, Tile>> w){
        for(int i=0; i<Params.world_height; i++){
            for (int j=0; j<Params.world_width; j++){
                for (int k=0; k<w.get(i).get(j).crittersOnTile().size(); k++){
                    if (c==w.get(i).get(j).crittersOnTile().get(k)) return w.get(i).get(j);
                }
            }
        }
        return null;
    }


	/**
	 * Determine the result of an encounter between 2 Critters
	 * @param A Critter encountering B
	 * @param B Critter encountering A
	 */
	private static void encounter(Critter A, Critter B){
		if(A.toString() == "@") {			//if one is algae...
			B.energy += A.energy/2;			//the other automatically wins and gets half of the loser's energy
			A.death();						//the loser is removed
			return;
		}
		if(B.toString() == "@") {
			A.energy += B.energy/2;
			B.death();
			return;
		}

		if(!A.fight(B.toString())) {				//if A decided to run
			int xCoordBackup = A.x_coord;			//holds coords in case run fails
			int yCoordBackup = A.y_coord;
			A.run(getRandomInt(8));				//picks a random direction...
			if(world.get(A.y_coord).get(A.x_coord).crittersOnTile().size()>1) {		//if Critter was already on that tile, return A
				A.x_coord = xCoordBackup;
				A.y_coord = yCoordBackup;
			}
		}
		if(!B.fight(A.toString())) {				//if B decided to run
			int xCoordBackup = A.x_coord;			//holds coords in case run fails
			int yCoordBackup = A.y_coord;
			B.run(getRandomInt(8));				//picks a random direction...
			if(world.get(A.y_coord).get(A.x_coord).crittersOnTile().size()>1) {	 //if Critter was already on that tile, return B
				B.x_coord = xCoordBackup;
				B.y_coord = yCoordBackup;
			}
		}

		if(A.x_coord == B.x_coord && A.y_coord == B.y_coord) {		//if A & B are still in the same place...
			int aAttack = getRandomInt(A.energy+1);	//Determine attack value of A and B based on energy and RNG
			int bAttack = getRandomInt(B.energy+1);
			if (aAttack > bAttack){						//The one with the higher attack gains half the other's energy and lives
				A.energy += (B.energy)/2;
				B.death();
			}
			else if (bAttack > aAttack){
				B.energy += (A.energy)/2;
				A.death();
			}
			else {
				int winner = getRandomInt(2);		//If they have the same attack value, randomly decide winner
				if (winner == 0) {
					A.energy += (B.energy)/2;
					B.death();
				}
				else {
					B.energy += (A.energy)/2;
					A.death();
				}
			}
		}
	}

	/**
	 * Function determining Critter's actions every time step, will be overridden
	 */
	public abstract void doTimeStep();

	/**
	 * Function determining if Critter will fight or run, will be overridden
	 * @param oponent String visually representing opponent
	 * @return boolean indicating if Critter runs or fights
	 */
	public abstract boolean fight(String oponent);

	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name String representing Critter subclass
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		if (firstTime) {												//if this is the first worldTimeStep create world
			createWorld(world);
			createWorld(lastTurnWorld);
			firstTime = false;
		}

		try{
			if(!critterTypes.contains(critter_class_name)) {	//InvalidCritterException is thrown if parameter is not a critter class name
				throw new InvalidCritterException(critter_class_name);
			}
			Class<?> critterClass = Class.forName(critter_class_name);	//get class object param refers to
			Constructor<?> ctor = critterClass.getConstructor();		//get constructor for aforementioned class
			Critter newCritter = (Critter) ctor.newInstance();			//make a new Critter of the class type
			newCritter.x_coord = Critter.getRandomInt(Params.world_width);	//put Critter at random location
			newCritter.y_coord = Critter.getRandomInt(Params.world_height);
			newCritter.energy = Params.start_energy;					//give Critter start_energy energy

			world.get(newCritter.y_coord).get(newCritter.x_coord).setFilled(newCritter);	//adds Critter to tile list
			population.add(newCritter);									//add Critter to population list
		}
		catch (InvalidCritterException e) {		//if critter name is not valid print error
			System.out.println(e);
		}
		catch (Exception e){						//handle other generic exceptions
			e.printStackTrace();
		}

	}

    /**
     * Fills the world Map with Tiles
     */
    private static void createWorld( Map<Integer, Map<Integer, Tile>>worldName){
        for (int i=0; i<Params.world_height; i++){  				//i represents the y coordinate
            Map<Integer, Tile> row = new HashMap<Integer, Tile>();  			//Map of all of the tiles with y coordinate i
            for (int j=0; j<Params.world_width; j++){ 				//j represents the x coordinate
                Tile newTile = new Tile(j,i);						//Create a new Tile with location (j,i)
                row.put(j, newTile);								//add it to Map of tiles with y coordinate i
            }
            worldName.put(i, row);										//add Map of tiles with y coordinate i to Map of all rows
        }
    }

	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();		//List of Critters of critter_class_name type
		try{															//try to access class critter_class_name...
			if(!critterTypes.contains(critter_class_name)) {			//if invalid type throw invalid critter exception
				throw new InvalidCritterException(critter_class_name);
			}
			Class<?> critterClass = Class.forName(critter_class_name); //get class object corresponding to critter_class_name
			for (int i=0; i<population.size(); i++){					//check each critter to see if they are of critterClass type
				if (population.get(i).getClass() == critterClass){
					result.add(population.get(i));						//if they are add them to result list
				}
			}
		}
		catch (InvalidCritterException e) {								//if InvalidCritterException print it
			System.out.println(e);
		}
		catch (Exception e){											//if other exception print stack trace
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static String runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");	//prints size and preface
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();	//Map holding the different amounts of Critters
		for (Critter crit : critters) {						//for each critter...
			String crit_string = crit.toString();			//check if they are in the list, if not add them
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";								//prints Critters
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();
		return (" ");
	}

	/* the TestCritter class allows some critters to "cheat". If you want to
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here.
	 *
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		/**
		 * Setter function for Critter energy
		 * @param new_energy_value int representing new energy value
		 */
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}

		/**
		 * Setter function for Critter X_coord
		 * @param new_x_coord int representing new X_coord
		 */
		protected void setX_coord(int new_x_coord) {
			world.get(super.y_coord).get(super.x_coord).remove(this);	//remove Critter from old Tile list, add it to new one
			world.get(super.y_coord).get(new_x_coord).setFilled(this);
			super.x_coord = new_x_coord;
		}

		/**
		 * Setter function for Critter Y_coord
		 * @param new_y_coord int representing new Y_coord
		 */
		protected void setY_coord(int new_y_coord) {
			world.get(super.y_coord).get(super.x_coord).remove(this);	//remove Critter from old Tile list, add it to new one
			world.get(new_y_coord).get(super.x_coord).setFilled(this);
			super.y_coord = new_y_coord;
		}

		/**
		 * Getter function for Critter X_coord
		 * @return int representing x_coord
		 */
		protected int getX_coord() {
			return super.x_coord;
		}

		/**
		 * Getter function for Critter Y_coord
		 * @return int representing y_coord
		 */
		protected int getY_coord() {
			return super.y_coord;
		}


		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}

		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		babies.clear();									//clear babies list
		for (int i=0; i<Params.world_height; i++){
			for(int j=0; j<Params.world_width; j++){
				world.get(i).get(j).clearTile();		//for every Tile clear tile list
			}
		}
	}

    /**
     * Function which updates the Map holding the copy of the world before this turn started
     */
    private static void updateOldWorld(){
        for (int i=0; i<Params.world_height; i++){  				//updates lastTurnWorld to be the same as world
            for (int j=0; j<Params.world_width; j++) {
                lastTurnWorld.get(i).get(j).clearTile();
                for (int k = 0; k < world.get(i).get(j).crittersOnTile().size(); k++) {
                    lastTurnWorld.get(i).get(j).setFilled(world.get(i).get(j).crittersOnTile().get(k));
                }
            }
        }
    }

	/**
	 * Calls doTimeStep for each critter in the world, then resolves Tiles with more than one critter
	 * @throws InvalidCritterException
	 */
	public static void worldTimeStep() throws InvalidCritterException {
		if (firstTime) {												//if this is the first worldTimeStep create world
			createWorld(world);
			createWorld(lastTurnWorld);
		}
		updateOldWorld();                                               //update before any time steps are called
		for (int i=0; i<population.size(); i++){
			population.get(i).hasMoved = false;							//reset hasMoved
			population.get(i).doTimeStep();								//call each Critter's doTimeStep
		}
        for(int i = 0; i < population.size(); i++) {                    //remove dead critters before fight as specified
            if(population.get(i).energy <= 0) {							//if energy falls below zero kill critter
                population.get(i).death();
                i--;
            }
        }

        updateOldWorld();                                              //update before 'fight' is called
		for (int i=0; i<Params.world_height; i++){						//find any Tile with more than one critter, and have them fight
			for(int j=0; j<Params.world_width; j++){
				ArrayList<Critter> crits = world.get(i).get(j).crittersOnTile();
				while(crits.size()>1){									//keep fighting until their is one Critter on the tile
					Collections.shuffle(crits);
					encounter(crits.get(0), crits.get(1));
				}
			}
		}

		for(int i = 0; i < population.size(); i++) {
			population.get(i).energy -= Params.rest_energy_cost;		//take rest energy away from every critter
			if(population.get(i).energy <= 0) {							//if energy falls below zero kill critter
				population.get(i).death();
				i--;
			}
		}

		for(int i = 0; i < Params.refresh_algae_count; i++) {			//add <refresh_algae_count> algae
			try {
				makeCritter("assignment5.Algae");
			}
			catch(InvalidCritterException e) {
				System.out.print(e);
			}
		}

		for(Critter baby: babies) {				//for every baby...
			baby.warp();						//make sure on proper coords
			population.add(baby);				//add to adult population
			world.get(baby.y_coord).get(baby.x_coord).setFilled(baby);	//add to tile lists
		}
		babies.clear();							//clear baby list
		firstTime = false;						//make sure world doesn't get recreated
	}

	/**
	 * Displays 2D representation of world with ASCII characters
	 */
	public static void displayWorld() {
		if (firstTime) {												//if this is the first worldTimeStep create world
			createWorld(world);
			createWorld(lastTurnWorld);
			firstTime = false;
		}
		System.out.print('+');												//Prints frame
		for(int i=0; i<Params.world_width; i++){System.out.print('-');}
		System.out.println('+');
		for(int i=0; i<Params.world_height; i++){							//For every row...
			System.out.print('|');											//Prints edge
			for(int j=0; j<Params.world_width; j++){						//Prints either space or critter symbol for every entry of row
				if ((world.get(i).get(j) != null) && ((world.get(i).get(j)).crittersOnTile().size()>0))
					System.out.print((world.get(i).get(j)).crittersOnTile().get(0).toString());
				else System.out.print(' ');
			}
			System.out.println('|');										//Prints edge
		}
		System.out.print('+');												//Prints frame
		for(int i=0; i<Params.world_width; i++){System.out.print('-');}
		System.out.println('+');
	}
}
