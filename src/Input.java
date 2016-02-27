import java.util.*;

public class Input {
	int val;
	
	private ArrayList<Link> toLink = new ArrayList<Link>();
	
	public Input(){}
	
	public Input(int val){ this.val = val;}
	
	public void setVal(int val){
		this.val = val;
	}
	

	public void addto(Link l) {
		toLink.add(l);
	}
	
	public Link getTo(int j){
		return toLink.get(j);
	}
	
}
