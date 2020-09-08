package part3;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.terminal.Variable;

import java.util.ArrayList;

/**
 * The fitness function for Part2
 *
 */
public class fitness extends GPFitnessFunction {

    /**
     * stores x values.
     */
    private ArrayList<Double> storeX;

    /**
     * stores y values.
     */
    private ArrayList<Double> storeY;

    /**
     *
     */
    private Variable x;

    /**
     *
     */
    private Object[] NO_ARGS = new Object[0];

    public fitness(ArrayList<Double> input, ArrayList<Double> output, Variable x) {
        this.storeX = new ArrayList<>(input);
        this.storeY = new ArrayList<>(output);
        this.x = x;
    }

    /**
     * evaluate.
     * @param arg0
     * @return
     */
    @Override
    protected double evaluate(IGPProgram arg0) {
        double result ;

        long longResult = 0;
        for (int i = 0; i < storeX.size(); i++) {
            x.set(storeX.get(i));
            double value = (double) arg0.execute_double(0, NO_ARGS);
            longResult += Math.abs(value - storeY.get(i));
        }

        //root mean squared
        double root = Math.sqrt(longResult);
        result = longResult;
        return result;
    }

}
