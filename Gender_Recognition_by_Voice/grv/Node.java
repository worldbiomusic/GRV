package grv;
/*
*  
*  Author  : Rashid A. Aljohani
*  Thursday, March 13, 2017
*
*/



public class Node{
	
	private String type;
	private double input;
	private double target;
	private double output;

	private double actual;
	private double delta;

	public Node(){}

	public Node(String type){
		this.type = type;
	}

	public void set_input(double input){
		this.input = input;
	}

	public void set_target(double target){
		this.target = target;
	}

	public void set_actual(double actual){
		this.actual = sigmod(actual);
	}

	public void set_delta(double delta){
		this.delta = delta;
	}


	public double get_input(){
		return input;
	}
	public double get_target(){
		return target;
	}

	public double get_actual(){
		return actual;
	}

	public double get_delta(){
		return delta;
	}


	public double sigmod(double val){
		return 1/(1+ Math.exp(-val));
	}

	public String toString(){
		return type;
	}

}
