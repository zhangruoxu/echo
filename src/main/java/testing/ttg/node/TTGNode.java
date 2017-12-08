package testing.ttg.node;

import java.io.Serializable;

/**
 * TTGNode represents testing state during testing. 
 * It can be a NormalState or a ErrorState.
 * 
 * @author yifei
 */
public abstract class TTGNode implements Serializable {
	public boolean isEntry() {
		return entry;
	}
	
	public void setAsEntry() {
		entry = true;
	}
	
	// Whether the TTGNode is the error state.
	public boolean isErrorState() {
		return this.getClass().equals(ErrorState.class);
	}
	
	// Whether the TTGNode is the entry node.
	protected boolean entry;
}
