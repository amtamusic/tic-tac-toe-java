import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class Brain {
    HashMap<String, ArrayList<Integer>>memory;
    public Brain()
    {
        this.memory=new HashMap<>();
    }

    public void learn(String game, int move)
    {
        if(!this.memory.containsKey(game))
        {
            ArrayList<Integer>moves = new ArrayList<>();
            moves.add(move);
            memory.put(game,moves);
        }else
        {
            ArrayList<Integer>moves=memory.get(game);
            moves.add(move);
            memory.put(game,moves);
        }
    }

    public ArrayList<Integer> remember(String game)
    {
        return memory.get(game);
    }
}
