package assignment5;/* CRITTERS Critter1.java
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
 * Critter1's main behavior is to go toward more Critter1s. Using getInstances it retrieves a list of all Critter1s, it then determines
 * which Critter1 is closest using the distance formula. From there it uses trig to determine the angle between it and the other Critter1,
 * and then scales that from 0-7 and walks that way. Other than that it walks every turn, reproduces when it has >=100 energy, and does not
 * attack other Critter1's.
 */

import java.util.List;

import static java.lang.Math.sqrt;

public class Critter1 extends Critter.TestCritter{


	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return javafx.scene.paint.Color.NAVY;  }
	public CritterShape viewShape(){
		return CritterShape.CIRCLE;
	}

	/**
	 * gets the distance to another Critter using the distance formula
	 * @param friend Critter whose distance we are solving for
	 * @return double representing distance to friend
	 */
	public double getDist(Critter friend){
		Critter1 fr = (Critter1)friend;							//cast to Critter1
		double distance = sqrt((fr.getX_coord()-this.getX_coord())*(fr.getX_coord()-this.getX_coord())+	//distance formula
				(fr.getY_coord()-this.getY_coord())*(fr.getY_coord()-this.getY_coord()));
		return distance;
	}

	/**
	 * gets the direction from 0-8 that this Critter would have to walk to get to friend
	 * @param friend Critter whose direction we are interested in
	 * @return int from 0-8 representing direction that friend is in
	 */
	public int getDirec(Critter friend){
		Critter1 fr = (Critter1)friend;							//cast to Critter1
		double x = (double)(fr.getX_coord()-this.getX_coord());		//+ means to the right, - means to the left
		double y = (double)(this.getY_coord()-this.getY_coord());		//+ means up, - means down
		if (x==0 && y==0) return getRandomInt(8); //if in the same tile go in a random direcion
		if (x==0) return (y>0) ? 2 : 6;				//if x is the same go up or down
		if (y==0) return (x>0) ? 0 : 4;				//if y is the same go left or right
		double angle=0.0;				 				//get angle
		double frac=0.0;
		if (x>0 && y>0) {		 				//first quad tan(y/x)
			frac = y/x;
			angle = Math.atan(frac);
		}
		else if (x<0 && y>0) {					//second quad 180-tan(y/(-x))
			frac = y/((-1.0)*x);
			angle = (Math.PI)-Math.atan(frac);
		}
		else if (x<0 && y<0) {					//third quad 180+tan(y/x)
			frac = y/x;
			angle = (Math.PI)+Math.atan(frac);
		}
		else {									//fourth quad 360-tan((-y)/x)
			frac = ((-1.0)*y)/x;
			angle = ((2.0)*Math.PI)-Math.atan(frac);
		}
		double direc = (angle/((2.0)*Math.PI))*(8.0);		//convert angle to int from 0 to 7
		int dir = (int)Math.round(direc);
		if (dir == 8) dir = 0;
		return dir;
	}

	/**
	 * determines if and how Critter1 will walk and reproduce each time step
	 */
	@Override
	public void doTimeStep() {
		try {                                                                //gets list of Critter1s
				List<Critter> friends = getInstances("assignment5.Critter1");
				double bestDistance = getDist(friends.get(0));					//double representing distance to closest Critter1
				int bestFriend = 0;												//index of closest Critter1 in friends
				if ((friends.get(bestFriend) == this)&&(friends.size()>1)) {	//makes start Critter!=this
					bestFriend = 1;
					bestDistance = getDist( friends.get(bestFriend));
				}
				for (int i = 0; i < friends.size(); i++) {						//checks all Critter1s to find closest
					if (getDist(friends.get(i)) < bestDistance && friends.get(i) != this) {
						bestDistance = getDist(friends.get(i));
						bestFriend = i;
					}
				}
				Critter bFriend = friends.get(bestFriend);						//Critter representing closest Critter1
				if (bFriend==this) walk(getRandomInt(8));					//if this is the only Critter1, walk in random direction
				else walk(getDirec(bFriend));									//if not walk toward nearest Critter
			} catch (InvalidCritterException e) {                                //if InvalidCritterException print it
				System.out.println(e);
			} catch (Exception e) {                                            //if other exception print stack trace
				e.printStackTrace();
			}
		if (this.getEnergy()>=100) {											//if energy is above threshold (120) reproduce
			Critter1 baby = new Critter1();
			reproduce(baby,getRandomInt(8));
		}
	}

	/**
	 * Determines if Critter1 will fight
	 * @param opponent String representing opponents Class symbol
	 * @return boolean representing Critter1's willingness to fight
	 */
	@Override
	public boolean fight(String opponent) {
		if ((getEnergy() > 10) && (!opponent.equals("Z"))) return true;			//if energy is above threshold (10) and opponent is not a Critter1 fight
		return false;
	}

	/**
	 * Function which prints stats about Critter1s
	 * @param ones List of Critters of type Critter1
	 */
	public static String runStats(List<Critter> ones) {
		int total=0;									//total energy amassed by all Critter1s
		for (int i=0; i<ones.size(); i++){
			total+=ones.get(i).getEnergy();
		}
		return("" + ones.size() + " total Critter1s, with an average of " + total/(ones.size()) + " energy");
	}

	/**
	 * gets symbol of Critter1
	 * @return String representing symbol of Critter1
	 */
	public String toString() {
		return "Z";
	}

}
