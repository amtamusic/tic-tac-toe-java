package ai;

import java.util.List;

public class Main {
    static double [][] X= {
            {1,0,1},
            {0,1,0},
            {1,0,1},
            {1,1,0}
    };
    static double [][] Y= {
            {1},{1},{1},{0}
    };
    public static void main(String[] args){
        NeuralNetwork nn = new NeuralNetwork(3,10,1);
        nn.fit(X, Y, 50000);
        double [][] input ={{0,0,1},{0,1,0},{1,0,1},{1,1,0}};
        for(double d[]:input)
        {
            List<Double> output = nn.predict(d);
            System.out.println(output.toString());
        }
    }
}
