import ai.NeuralNetwork;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.util.*;

@Data
public class Brain {
    HashMap<String, HashSet<Integer>>memoryOffensive;
    HashMap<String, HashSet<Integer>>memoryDefensive;
    NeuralNetwork nn;
    Boolean useNN=true;
    public Brain()
    {
        this.memoryOffensive=new HashMap<>();
        this.memoryDefensive=new HashMap<>();
        this.nn=new NeuralNetwork(10, 10, 10);
    }

    public void learn(String game, int move,boolean isOffense,int player)
    {
        if(!useNN){
            if(isOffense)
            {
                learn(game,move,this.memoryOffensive);
            }else
            {
                learn(game,move,this.memoryDefensive);
            }
        }else{
            learnNN(game,move,player);
        }
    }

    public void learn(String game, int move,HashMap<String, HashSet<Integer>>memory)
    {
        HashSet<Integer> moves;
        if(!memory.containsKey(game))
        {
            //System.out.println(game);
            moves = new HashSet<>();
        }else
        {
            moves = memory.get(game);
            //System.out.println("Game Found: "+game+" : "+moves.size());
        }
        moves.add(move);
        memory.put(game,moves);
    }
    public void learnNN(String game, int move,int player){
        //Neural net
//        System.out.println(game);
        ArrayList<String> a = new ArrayList<>(Arrays.asList(game.split("")));
        double[] f = new double[a.size()+1];

        for(int i =0;i<a.size();i++){
            f[i]=Double.parseDouble(a.get(i).replace("-","0.5").replace("x","0").replace("o","1"));
        }
        f[f.length-1]=player;
        double [][] X= {
                f
        };
        f[move]=1;
        double [][] Y= {
                f
        };
        nn.fit(X, Y, 1000);
//        double [][] input ={{0,0,1},{0,1,0},{1,0,1},{1,1,0}};
//        for(double d[]:input)
//        {
//            List<Double> output = nn.predict(d);
//            System.out.println(output.toString());
//        }
    }

    public HashSet<Integer> remember(String game)
    {
        HashSet<Integer>memoryDefensive=this.memoryDefensive.get(game);
        if(memoryDefensive==null || memoryDefensive.isEmpty())
        {
            return this.memoryOffensive.get(game);
        }
        return memoryDefensive;
    }
}
