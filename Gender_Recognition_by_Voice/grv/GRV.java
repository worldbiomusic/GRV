package grv;

import java.io.File;
/*
*  
*  Author  : Rashid A. Aljohani
*  Thursday, March 13, 2017
*
*/
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GRV{
	
	Helper helper;
	List<String> training_dataset;
	List<String> testing_dataset;
	List<String> input_dataset;
	
	double lc = 0.45;
	Map<Node, Set<Weight>> connections;

	List<Node> inputs_units;
	List<Node> hiddens_units;
	Node output_unit;

	int total;
	int correct;
	public double accuracy;
	
	public void init_values()
	{
		helper = new Helper();

		// 남자 0, 여자 1로 함
		training_dataset = helper.read("grv/dataset/training_dataset/train_aiopen.csv");
		testing_dataset =  helper.read("grv/dataset/testing_dataset/test_aiopen.csv");
		
		connections = new LinkedHashMap<>();

		inputs_units = new LinkedList<>();
		hiddens_units = new LinkedList<>();
		output_unit = new Node("output[1]");
		
		// MARK: measure the system performance
		total = 0;
		correct = 0;
	}
	
	public void init_nerual_network()
	{
		init_values();
		
		// MARK: initialized the neural network
		helper.init(inputs_units, hiddens_units, output_unit, connections);
	}
	
	public void run_training_stage()
	{
		int epoch = 0;
		while(epoch < 6000){

			for(String set : training_dataset){
	
				// MARK: convert a string line into string array
				String[] array = set.split(",");
				double TARGET = Double.parseDouble(array[array.length - 1]);
				double[] current_data_input = helper.normalize(array);
	
				// MARK:
				for(int i=0; i < current_data_input.length - 1; i += 1){
					inputs_units.get(i).set_input( current_data_input[i] );
				}
	
				// MARK: set target value
				output_unit.set_target(TARGET);
	
				helper.feedforward(connections);
				helper.backpropagation(connections, output_unit, lc);
			}

		epoch += 1;
		}
		
		
	}
	
	public void run_testing_stage()
	{
		for(String set : testing_dataset){

			// MARK: convert a string line into string array
			String[] array = set.split(",");
			double TARGET = Double.parseDouble(array[array.length - 1]);
			double[] current_data_input = helper.normalize(array);

			// MARK:
			for(int i=0; i < current_data_input.length - 1; i += 1){
				inputs_units.get(i).set_input( current_data_input[i] );
			}

			// MARK: set target value
			output_unit.set_target(TARGET);
			
			helper.feedforward(connections);
			helper.backpropagation(connections, output_unit, lc);


			// MARK: system performance
			if(output_unit.get_target() == 0 && output_unit.get_actual() < 0.01){
				correct += 1;
			}
	
			if(output_unit.get_target() == 1 && output_unit.get_actual() > 0.99){
				correct += 1;
			}

			total += 1;	
		}
		
		// 
		accuracy = ((double)correct / total) * 100;
		System.out.println(String.format("accuracy: %.2f", accuracy));
	}
	
	public String run_input_stage()
	{
		input_dataset = helper.read("grv/dataset/input_data/output/voice.csv");
		
		// process stage
		StringBuffer buf_str = new StringBuffer();
		int gender = -1;
		for(String set : input_dataset){

			// MARK: convert a string line into string array
			String[] array = set.split(",");
			double TARGET = Double.parseDouble(array[array.length - 1]);
			double[] current_data_input = helper.normalize(array);

			// MARK:
			for(int i=0; i < current_data_input.length - 1; i += 1){
				inputs_units.get(i).set_input( current_data_input[i] );
			}
			
			// MARK: set target value
			output_unit.set_target(TARGET);
			
			helper.feedforward(connections);
			helper.backpropagation(connections, output_unit, lc);
			
			double actual = output_unit.get_actual();
			
			if(actual < 0.1)
			{
				// male
				gender = 0;
			}
			else if(actual > 0.9)
			{
				// female
				gender = 1;
			}
			else
			{
				// can't recognize
				gender = -1;
			}
			
			System.out.println("input data is " + gender);
			
			String g;
			
			if(gender == 0)
				g = "male";
			else if(gender == 1)
				g = "female";
			else
				g = "none";
			
			
			buf_str.append("your voice is " + g);
			buf_str.append("\n");
			
			total += 1;
		}
		
		
		return buf_str.toString();
	}
	
	public void print_result()
	{
		print("System Performance: " + correct + " out of " + total + "\n");
	}

	
	
	public static void main(String[] args) {
		GRV grv = new GRV();
		
		// MARK: initialize nerual network
		grv.init_nerual_network();
		
		// MARK: training stage
		grv.run_training_stage();
	
		// MARK: testing stage
		grv.run_testing_stage();
		
//		grv.run_input_stage();
	
		// MARK: print result
		grv.print_result();
	}


	static void print(String str){
		System.out.print(str);
	}

}