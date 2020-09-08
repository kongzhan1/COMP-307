package part1;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PerceptronBinaryClassification {
	
	private Double[] weight = new Double[3];
	private double learningRate = 0.1;
	private int bias =0;
	private int Max_epoch =200;//max number of iteration for training weight
	
	public PerceptronBinaryClassification() {
		for(int i =0; i< weight.length; i++) {
			//gets either 0 or 1
			weight[i]= (double) Math.round(Math.random());
		}
	}
	
	 public int predict(Integer[] inputs){
	        int sum = 0;
	        for(int i = 0; i < weight.length; i++){
	                sum +=  inputs[i] * weight[i];
	        }
	        int cal = sum + bias;
	        return activation(cal);
	 }
	 
	 /**
	     * Activation Function.
	     */
	    int activation(int n){
	        if(n > 0){
	            return 1;
	        } else {
	            return 0;
	        }
	    }
	  
	       
	  
	   /**
	    * trains the weight to the number of maximum iteration 
	    * @param inputs
	    */
	    public void trainWeight(String filename,Map<String,ArrayList<Integer>> inputs){
	    	Main main = new Main(filename);
	    	int correctPrediction =0; 
	    	int numErr =0;//total number of error in prediction
	    	int iter =0;
	    	List<Integer> classList = main.getParsedData().get("Class");
	    	while(iter<this.Max_epoch) {
	    		int error =0;
	    		for(int pos =0;pos<main.getParsedData().get("A").size(); pos++) {
	    			int targetClass = classList.get(pos);
		    		Integer[] inputFeatures = main.getFeatures(pos);
		    		int predictOutput = predict(inputFeatures);
			        System.out.println("Predicted Inputs: " + predictOutput);
			        System.out.println("Actual answer: " + targetClass);
			        error = targetClass - predictOutput;
			        System.out.println("error output: " + error);
			        System.out.println("-----------------------------------");
			        
			        //if no error, increase the number of correct prediction
			        if(error == 0) { correctPrediction++;}
			        
			        //The gradient descent
			        if(error < 0) {
			        	for(int i=0; i<weight.length; i++){
				            weight[i] -= error * inputFeatures[i] * learningRate;
				        }
			            numErr++;
			        }
			        if(error > 0) {
			        	for(int i=0; i<weight.length; i++){
				            weight[i] += error * inputFeatures[i] * learningRate; 
				        }
			            numErr++;
			        }			       
	    		}
	    		iter++;
	    	}
	    	System.out.println("correct: " + correctPrediction);
	    	System.out.println("error num: " + numErr);
	    	
	    	//calculates Accuracy
	    	double accuracy = correctPrediction;
	    	DecimalFormat f = new DecimalFormat("##.0");
	    	int numOfNestedtLoop = this.Max_epoch*main.getParsedData().get("A").size();
	    	accuracy = (accuracy/numOfNestedtLoop)*100.0;
	    	System.out.println("Accuracy of correctness: "+f.format(accuracy) + "%");
	    	}
	    	
//	    }
	    
	    /**
	     * Getters and Setters
	     * @return
	     */
	    public Double[] getWeights() {
	        return weight;
	    }


}
