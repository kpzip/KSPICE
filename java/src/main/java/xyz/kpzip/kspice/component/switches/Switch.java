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
	 * @return wheather or not this switch is on
	 */
	boolean isOn();
	
	/**
	 * Toggles the state of the switch. If the switch was off, it will now be on, and vice versa.<br><br>
	 * "I tried to switch to decaf, but it made me even more wired."
	 */
	void toggle();
	
	/**
	 * sets the state of the switch
	 * @param on - true to make the switch on, false to make the switch off.
	 */
	void setOn(boolean on);
	
	/**
	 * Turns this switch on, having no effect if it was already on.
	 */
	void setOn();
	
	/**
	 * Turns this switch off, having no effect if it was already off.
	 */
	void setOff();

}
