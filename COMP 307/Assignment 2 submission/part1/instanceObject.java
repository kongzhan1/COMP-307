package part1;

public class instanceObject {
    public Integer[] features;

    instanceObject(int f1, int f2, int f3){
        features = new Integer[3];
        features[0] = f1;
        features[1] = f2;
        features[2] = f3;
    }

    /**
     * Getters and Setters
     * @return
     */
    public Integer[] getFeatures() {
        return features;
    }


}