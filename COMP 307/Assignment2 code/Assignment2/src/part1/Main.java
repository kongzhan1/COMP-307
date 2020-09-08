package part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
	
	//stores the dataSet
	public Map<String,ArrayList<Integer>> dataSet = new HashMap<String,ArrayList<Integer>>();
	
	public Main(String filename) {
		parseFile(new File(filename));
	}
	
	/**
	 * parses the dataSet and stores it in a Map
	 * @param file
	 */
	public void parseFile(File file) {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(file));
			String instance = buffer.readLine();
			String[] split = instance.split("\\s+");
			//creates a list of each feature
			for(int i =0;i < 4 ;i++) {
				dataSet.put(split[i], new ArrayList<Integer>());
			}
			
			//adding the features in the list			
			while((instance= buffer.readLine()) != null) {
				split = instance.split("\\s+");
				int num=0;
				for(String feature: dataSet.keySet()) {
					//System.out.print(feature);
					dataSet.get(feature).add(Integer.valueOf(split[num]));
					num++;
				}
			}
	
//			for(int k=0; k<dataSet.get("A").size();k++) {
//				System.out.println();
//				for(String f: dataSet.keySet()) {
//					System.out.print(dataSet.get(f).get(k));
//				}
//			}
			
			
		} catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}   	 	
	}
	
	/**
	 * returns features of instance
	 */
	public Integer[] getFeatures(int pos) {
		InstanceObject test;
			int f1 = dataSet.get("A").get(pos);
			int f2 = dataSet.get("B").get(pos);
			int f3 = dataSet.get("C").get(pos);
			test = new InstanceObject(f1,f2,f3);
			return test.getFeatures();
			//System.out.println(test.getFeature1());
		
	}
	
	/**
	 * @return the map of the parsed data
	 */
	public Map<String,ArrayList<Integer>> getParsedData() {
		return dataSet;
	}
	
	public static void main(String[] args) {
		Main start = new Main("dataset");//change the filename here to change the training set
		PerceptronBinaryClassification perceptron = new PerceptronBinaryClassification();
		perceptron.trainWeight("dataset",start.getParsedData());
	}
}



