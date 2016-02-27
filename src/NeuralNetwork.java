import java.util.*;

public class NeuralNetwork {
	static ArrayList<Integer> results = new ArrayList<Integer>();
	
	Input[] inputlayer = new Input[4];
	LinkedList<Neural> hiddenLayer = new LinkedList<Neural>();
	float bias = (float) (Math.random() * 2 - 1);
	Neural output = new Neural(bias);
	int count = 0;

	ArrayList<List<Integer>> traindata = new ArrayList<List<Integer>>();

	public void generate_train(int n) {
		if (n == 0)
			return;
		dfs(new ArrayList<Integer>(), n);
	}

	private int parity(List<Integer> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		return (sum % 2);
	}

	public void dfs(ArrayList<Integer> list, int n) {
		if (n == list.size()) {
			list.add(parity(list));
			traindata.add(new ArrayList<Integer>(list));
			return;
		}
		list.add(0);
		dfs(new ArrayList<Integer>(list), n);
		list.remove(list.size() - 1);
		list.add(1);
		dfs(new ArrayList<Integer>(list), n);
	}

	public void initialize() {

		for (int i = 0; i < 4; i++) {
			inputlayer[i] = new Input();
		}
		for (int i = 0; i < 4; i++) {
			float bias = (float) (Math.random() * 2 - 1);
			Neural hidden = new Neural(bias);
			for (int j = 0; j < 4; j++) {
				float weight = (float) (Math.random() * 2 - 1);
				Link link = new Link(weight);
				link.FromTo(inputlayer[j], hidden);
				hidden.addfrom(link);
				inputlayer[j].addto(link);
			}
			hiddenLayer.add(hidden);
		}

		for (int i = 0; i < 4; i++) {
			float weight = (float) (Math.random() * 2 - 1);
			Link link = new Link(weight);
			link.FromTo(hiddenLayer.get(i), output);
			hiddenLayer.get(i).addto(link);
			output.addfrom(link);
		}
	}

	public void train(float n, float alpha) {
		boolean flag = false;
		while (!flag) {
			float max = 0;
			for (int k = 0; k < 16; k++) {
				List<Integer> train = traindata.get(k);
				forward(train);
				float e = train.get(4) - output.y;
				output.delta = e * sigmodp(output.v);
				output.deltabias = n * output.delta + alpha * output.deltabias;
				output.bias += output.deltabias;
				for (int j = 0; j < 4; j++) {
					Neural hiddenNerual = hiddenLayer.get(j);
					Link wkj = output.getfrom(j);
					hiddenNerual.delta = sigmodp(hiddenNerual.v) * wkj.weight * output.delta;
					wkj.deltaw = n * output.delta * hiddenNerual.y + alpha * wkj.deltaw;
					wkj.weight += wkj.deltaw;
					for (int i = 0; i < 4; i++) {
						Link wji = hiddenNerual.getfrom(i);
						wji.deltaw = n * hiddenNerual.delta * wji.from.val + alpha * wji.deltaw;
						wji.weight += wji.deltaw;
					}
					hiddenNerual.deltabias = n * hiddenNerual.delta + alpha	* hiddenNerual.deltabias;
					hiddenNerual.bias += hiddenNerual.deltabias;
				}
				max = Math.max(Math.abs(e), max);
			}
			flag = (max < 0.05);
			count++;
			System.out.println(count + " " + max);
			if (count >= 2000000)
				break;
		}
	}

	public void forward(List<Integer> list) {
		for (int i = 0; i < 4; i++) {
			float temp = 0;
			for (int j = 0; j < 4; j++) {
				Input thisinput = inputlayer[j];
				thisinput.setVal(list.get(j));
				temp += thisinput.val * thisinput.getTo(i).weight;
			}
			temp += hiddenLayer.get(i).bias;
			hiddenLayer.get(i).v = temp;
			temp = sigmod(temp);
			hiddenLayer.get(i).y = temp;
		}
		float temp = 0;
		for (int i = 0; i < 4; i++) {
			Neural current = hiddenLayer.get(i);
			temp += current.y * current.getTo(0).weight;
		}
		temp += output.bias;
		output.v = temp;
		temp = sigmod(temp);
		output.y = temp;
	}

	public float sigmod(float x) {
		return (float) (1 / (1 + Math.exp(-x)));
	}

	public float sigmodp(float x) {
		return sigmod(x) * (1 - sigmod(x));
	}

	public static void run(float n, float alpha) {
		NeuralNetwork NN = new NeuralNetwork();
		NN.generate_train(4);
		NN.initialize();
		NN.train(n, alpha);
		results.add(NN.count);
	}

	public static void main(String[] args) {
		float n = (float) 0.05;
//		run((float) 0.25, (float) 0.9);
/*		for (int i = 0; i < 10; i++) {
			run(n, (float) 0.0);
			n += 0.05;
		}*/
		for (int i = 0; i < 10; i++) {
			run(n, (float) 0.9);
			n += 0.05;
		}
		for (int i = 0; i < results.size(); i++){
			System.out.println(results.get(i));
		}
	}

}
