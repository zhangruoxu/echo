package testing.ttg.node;

/**
 * This class represents an error state during testing.
 * 
 * @author yifei
 */
public class ErrorState extends TTGNode {
	public ErrorState() {
		entry = false;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(this.getClass().equals(o.getClass()))
			return true;
		else return false;
	}

	@Override
	public String toString() {
		return "[##ErrorState##]";
	}
}
