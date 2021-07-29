package grv;
/*
*  
*  Author  : Rashid A. Aljohani
*  Thursday, March 13, 2017
*
*/


public class Weight{
	
	private Node source;
	private double weight;
	private Node destination;

	public Weight(Node source, double weight, Node destination){

		this.source = source;
		this.weight = weight;
		this.destination = destination;
	}

	public void update_weight_hiddens(double lc){

		double NEW = weight + (lc * destination.get_delta() * source.get_input());
		weight = NEW;
	}

	public void update_weight_outputs(double lc, double delta){

		double NEW = weight + (lc * delta * source.get_actual());
		weight = NEW;
	}
	
	public double get_weight(){
		return  weight;
	}

	public Node get_source(){
		return source;
	}

	public Node get_destination(){
		return destination;
	}

	public String toString(){
		return source + "--( "  + weight + " ) --> " + destination;
	}
}
