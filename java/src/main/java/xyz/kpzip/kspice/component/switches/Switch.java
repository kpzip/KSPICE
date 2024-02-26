package xyz.kpzip.kspice.component.switches;

/**
 * Represents a logical switch
 * can be on or off.
 * 
 * @author kpzip
 *
 */
public interface Switch {
	
	/**
	 * @return whether or not this switch is on
	 */
	boolean isOn();
	
	/**
	 * Toggles the state of the switch. If the switch was off, it will now be on, and vice versa.<br><br>
	 * "I tried to switch to decaf, but it made me even more wired."
	 */
	default void toggle() {
		setOn(!isOn());
	}
	
	/**
	 * sets the state of the switch
	 * @param on - true to make the switch on, false to make the switch off.
	 */
	void setOn(boolean on);
	
	/**
	 * Turns this switch on, having no effect if it was already on.
	 */
	default void setOn() {
		setOn(true);
	}
	
	/**
	 * Turns this switch off, having no effect if it was already off.
	 */
	default void setOff()  {
		setOn(false);
	}

}
