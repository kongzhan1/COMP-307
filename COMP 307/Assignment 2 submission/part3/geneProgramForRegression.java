package part3;

import org.apache.log4j.Logger;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPProblem;
import org.jgap.gp.function.Add;
import org.jgap.gp.function.Divide;
import org.jgap.gp.function.Exp;
import org.jgap.gp.function.Log;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.function.Multiply3;
import org.jgap.gp.function.Subtract;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import org.jgap.util.SystemKit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Running the main will run the GP program for symbolic regression
 *
 */
public class geneProgramForRegression {
    private static Logger loggingInfo= Logger.getLogger(geneProgramForRegression.class);

    /**
     * main method to run the algorithm
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            GPProblem parse;
            //please set file path here
            parse = new geneticProgramming("C:\\Users\\Admin\\IdeaProjects\\untitled1\\src\\part3\\data\\regression");
            GPGenotype gp = parse.create();
            gp.setVerboseOutput(true);

            //stopping criteria that of being 0 or the max evolution number
            int offset = gp.getGPConfiguration().getGenerationNr();

            int evolve;
            if (100 >= 0) {
                 evolve = 100;
            } else {
                evolve = Integer.MAX_VALUE;
            }
            for (int i = 0; i < evolve; i++) {
                // Stopping Criteria
                if (i >= 1) {
                    //the closer to 0 the better
                    if (gp.getAllTimeBest().getFitnessValue() == 0) {
                        loggingInfo.info("------Found Best Solution------");
                        break;
                    }
                }
                //logging info in every 5 generation
                if (i % 5 == 0) {
                    String freeMB = SystemKit.niceMemory(SystemKit.getFreeMemoryMB());
                    loggingInfo.info(
                            "Evolving generation " + (i + offset) + ", memory free: " + freeMB + " MB");
                }
                gp.evolve();
                gp.calcFitness();
            }

            System.out.println();
            gp.outputSolution(gp.getAllTimeBest());
        } catch (InvalidConfigurationException e) {
            System.out.println("Genetic Program Failed");
            e.printStackTrace();
        }
    }

    /**
     * class for GP.
     */
    public static class geneticProgramming extends GPProblem{

        private Variable variable_x;

        //stores the regression dataset
        Map<String,ArrayList<Double>> dataSet = new HashMap<String,ArrayList<Double>>();

        geneticProgramming(String filename) throws InvalidConfigurationException {
            super(new GPConfiguration());
            try {
                readFile(filename);
                //config setup
                configSetUp();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /**
         *Configuration of the genetic program
         *
         * @throws InvalidConfigurationException
         */
        private void configSetUp() throws InvalidConfigurationException {
            GPConfiguration config = getGPConfiguration();
            // config.setRandomGenerator(new SeededRandomGenerator(1));
            variable_x = Variable.create(config, "X", CommandGene.DoubleClass);
            config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
            config.setMaxInitDepth(6);
            config.setPopulationSize(2000);
            config.setMaxCrossoverDepth(12);
            config.setCrossoverProb(0.95f);
            config.setMutationProb(0.05f);
            config.setReproductionProb(0.05f);
            config.setFitnessFunction(new fitness(getX(), getY(), variable_x));
        }

        /**
         * parses and stores the inputs in a map
         * @param fileName
         * @throws FileNotFoundException
         */
        void readFile(String fileName) throws FileNotFoundException{
            try{
                BufferedReader buffer = new BufferedReader(new FileReader(fileName));
                String instance = buffer.readLine();
                String[] split = instance.split("\\s+");

                //adds the attribute names in the list
                for(int i=0; i<2; i++){
                    dataSet.put(split[i], new ArrayList<>());
                }

                //populates the map
                while((instance= buffer.readLine()) != null){
                    split = instance.split("\\s+");
                    int num=0;
                    for(String feature: dataSet.keySet()) {
                        //System.out.print(feature);
                        dataSet.get(feature).add(Double.valueOf(split[num]));
                        num++;
                    }
                }

            } catch(IOException e){
                e.printStackTrace();
            }
        }

        /**
         * @return the list of x
         */
        private ArrayList<Double> getX() {
            return dataSet.get("X");
        }

        /**
         * @return the list of y
         */
        private ArrayList<Double> getY() {
            return dataSet.get("Y");
        }

        /**
         * create GP.
         * @return
         * @throws InvalidConfigurationException
         */
        @Override
        public GPGenotype create() throws InvalidConfigurationException {
            GPConfiguration config = getGPConfiguration();

            Class[] types = { CommandGene.DoubleClass };

            Class[][] argTypes = { {} };

            CommandGene[][] nodeSets = { { variable_x,
                    new Add(config, CommandGene.DoubleClass),
                    new Multiply(config, CommandGene.DoubleClass),
                    new Multiply3(config, CommandGene.DoubleClass),
                    new Divide(config, CommandGene.DoubleClass),
                    new Subtract(config, CommandGene.DoubleClass),
                    new Exp(config, CommandGene.DoubleClass),
                    new Log(config, CommandGene.DoubleClass),
                    new Terminal(config, CommandGene.DoubleClass, -5.0, 5.0, true) } };

            return GPGenotype.randomInitialGenotype(config, types, argTypes, nodeSets, 20,true);
        }



    }


}