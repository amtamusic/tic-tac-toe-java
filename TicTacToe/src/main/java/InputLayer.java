import java.util.ArrayList;

public class InputLayer {
    ArrayList<Double>weights;

    public InputLayer(int length)
    {
        weights=new ArrayList<>();
        for(int i=0;i<length;i++)
        {
            weights.add(0.5);
        }
    }

    public void updateWeights(ArrayList<Double>newValues)
    {
        for(int i =0;i<weights.size();i++)
        {
            double newWeight=weights.get(i) * newValues.get(i);
            if(newWeight<=1 && newWeight>=0) {
                weights.set(i, Math.round(newWeight * 1000.0) / 1000.0);
            }else if (newWeight>1.0)
            {
                weights.set(i,1.0);
            }else if (newWeight<0)
            {
                weights.set(i,0.0);
            }
        }
    }

    public ArrayList<Double> getWeights()
    {
        return this.weights;
    }
}
