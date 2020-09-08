package part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class percForBinaryClassification {

    /**
     * store dataSet.
     */
    private Map<String,ArrayList<Integer>> dataSet = new HashMap<String,ArrayList<Integer>>();

    public percForBinaryClassification(String filename) {
        parseFile(new File(filename));
    }

    /**
     * parses the dataSet and stores it in a Map
     * @param file .
     */
    public void parseFile(File file) {
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String line = buffer.readLine();
            String[] split = line.split("\\s+");
            //creates a list of each feature
            for(int i =0;i < 4 ;i++) {
                dataSet.put(split[i], new ArrayList<Integer>());
            }

            //adding the features in the list
            while((line= buffer.readLine()) != null) {
                split = line.split("\\s+");
                int num=0;
                for(String feature: dataSet.keySet()) {
                    dataSet.get(feature).add(Integer.valueOf(split[num]));
                    num++;
                }
            }


        } catch (IOException e) {e.printStackTrace();}
    }

    /**
     * returns features of instance
     */
    public Integer[] getFeatures(int pos) {
        instanceObject test;
        int f1 = dataSet.get("A").get(pos);
        int f2 = dataSet.get("B").get(pos);
        int f3 = dataSet.get("C").get(pos);
        test = new instanceObject(f1, f2, f3);
        return test.features;
    }

    /**
     * @return the map of the parsed data
     */
    public Map<String,ArrayList<Integer>> getParsedData() {
        return dataSet;
    }

    public static void main(String[] args) {
        percForBinaryClassification start = new percForBinaryClassification("C:\\Users\\Admin\\IdeaProjects\\untitled1\\src\\part1\\data\\dataset");//change the filename here to change the training set
        PerceptronBinaryClassification perceptron = new PerceptronBinaryClassification();
        perceptron.trainWeight(start.getParsedData());
    }

    /**
     * class for doing PerceptronBinaryClassification.
     */
    public static class PerceptronBinaryClassification {

        private Double[] weight = new Double[3];

        /**
         *
         */
        PerceptronBinaryClassification() {
            for(int i =0; i< weight.length; i++) {
                weight[i]= (double) Math.round(Math.random());
            }
        }

        /**
         *
         * @param inputs
         * @return
         */
        int predict(Integer[] inputs){
            int sum = 0;
            int bias = 0;
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
        void trainWeight(Map<String, ArrayList<Integer>> inputs){
            percForBinaryClassification percForBinaryClassification = new percForBinaryClassification("C:\\Users\\Admin\\IdeaProjects\\untitled1\\src\\part1\\data\\dataset");
            int correctPrediction =0;
            int numErr =0;//total number of error in prediction
            int iter =0;
            List<Integer> classList = percForBinaryClassification.getParsedData().get("Class");
            //max number of iteration for training weight
            int max_epoch = 200;
            while(iter< max_epoch) {
                int error =0;
                for(int pos = 0; pos< percForBinaryClassification.getParsedData().get("A").size(); pos++) {
                    int targetClass = classList.get(pos);
                    Integer[] inputFeatures = percForBinaryClassification.getFeatures(pos);
                    int predictOutput = predict(inputFeatures);
                    error = targetClass - predictOutput;

                    //if no error, increase the number of correct prediction
                    if(error == 0) { correctPrediction++;}

                    //The gradient descent
                    double learningRate = 1;
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
            int num = max_epoch * percForBinaryClassification.getParsedData().get("A").size();
            accuracy = (accuracy/num)*100.0;
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


}
