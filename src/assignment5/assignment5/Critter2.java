package assignment5;/* CRITTERS Critter2.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Jonathan Rosenzweig>
 * <JJR3349>
 * <15466>
 * <Student2 Zach Sisti>
 * <Student2 zes279>
 * <15495>
 * Slip days used: <0>
 * Fall 2016
 *
 * Critter2 boosts it's energy by 50% when it enters a fight, and returns then loses 75% of that original energy after the fight.
 * It also does not move very frequently, only doing so when it has less than 25 energy. It also reproduces when it has 60 or more energy
 */


import java.util.List;

public class Critter2 extends Critter.TestCritter {

	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return javafx.scene.paint.Color.CRIMSON;  }

	public CritterShape viewShape(){
		return CritterShape.SQUARE;
	}

	/**
	 * construct sets fought to 0
	 */
	public Critter2(){
		fought=0;
	}
	private int fought;
	//energy to be deducted at the beginning of the next time step (70% of energy at the time of the previous turn's fight)

	/**
	 * Determines if and how Critter2 will walk and reproduce (and lose energy if fought last turn) each timestep
	 */
	@Override
	public void doTimeStep() {
		if (fought>0) {						//if fought previous turn deduct fought
			setEnergy(getEnergy()-fought);
			fought=0;
		}
		if (getEnergy()<25){				//only move if energy is below threshold (25)
			walk(getRandomInt(8));
		}
		if (getEnergy()>=60){				//only reproduce if energy is above threshold (120)
			Critter2 child = new Critter2();
			reproduce(child, getRandomInt(8));
		}
	}

	/**
	 * Determines if Critter2 will fight
	 * @param opponent String representing opponent Class symbol
	 * @return boolean indicating if Critter2 will fight
	 */
	@Override
	public boolean fight(String opponent) {
		if (getEnergy()>20){						//only fight if energy is above threshold (20)
			fought = (int)(getEnergy()*(0.7));		//When entering a fight, increase energy by 50%, but lose 70% after fight ends
			setEnergy(getEnergy()+(getEnergy()/2));
			return true;
		}
		return false;
	}

	/**
	 * gets symbol of Critter2
	 * @return String representing symbol of Critter2
	 */
	@Override
	public String toString () {
		return "*";
	}

	/**
	 * Function returns stats of Critter2s
	 * @param sixes List of all Critter2s
	 */
	public static String runStats(List<Critter> sixes) {
		int g100 = 0;				//number of Critter2s with > 100 energy
		int l30 = 0;				//number of Critter2s with < 30 energy
		for (int i=0; i<sixes.size(); i++){				//go through list and increment g100 and l30 accordingly
			if (sixes.get(i).getEnergy()>=100) g100++;
			if (sixes.get(i).getEnergy()<30) l30++;
		}
		return("" + sixes.size() + " total Critter2s "+ g100 + " thriving, and " + l30 + " on death's door");
	}
}
