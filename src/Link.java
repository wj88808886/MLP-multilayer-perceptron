
public class Link {

	float weight;
	float deltaw = 0;
	Input from;
	Input to;
	
	public Link(float weight){
		this.weight = weight;
	}
	
	public void FromTo(Input from, Input to){
		this.from = from;
		this.to = to;
	}
	
}
