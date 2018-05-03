package model;
/**
 * 
 * This exception is thrown when there is an attempt to remove more gold than the Owner has.
 * The constructor for NotEnoughGoldException should take two integers,
 * the first representing the amount of credits we tried to remove (this should be a positive number),
 * the second represents the amount of credits the Owner had at the time of the call (this too should be positive).
 * 
 * @author Yebin Brandt
 * 
 */

public class NotEnoughGoldException extends RuntimeException{

	private static final long serialVersionUID = 1L;


	/**
	 * throws the exception and gives information why 
	 * 
	 * @param creditsToRemove credits that the tower costs
	 * @param creditsOwnerHas credits the player had
	 */
	public NotEnoughGoldException(int creditsToRemove, int creditsOwnerHas){
		super("Attempted to remove " + creditsToRemove + " gold, but only had " + creditsOwnerHas + " gold.");
	}
}
