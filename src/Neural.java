import java.util.*;

public class Neural extends Input {

	float bias;
	float deltabias = 0;
	private ArrayList<Link> fromLink = new ArrayList<Link>();
	private ArrayList<Link> toLink = new ArrayList<Link>();
	float y;
	float delta;
	float v;

	public Neural(float bias) {
		this.bias = bias;
	}

	public void addfrom(Link l) {
		fromLink.add(l);
	}
	
	public Link getfrom(int j){
		return fromLink.get(j);
	}

}
