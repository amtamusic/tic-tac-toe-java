import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

@Data
public class Brain implements Serializable {

    HashMap<String, HashSet<Integer>> memoryOffensive;
    HashMap<String, HashSet<Integer>> memoryDefensive;

    public Brain() {
        this.memoryOffensive = new HashMap<>();
        this.memoryDefensive = new HashMap<>();
    }

    public void learn(String game, int move, boolean isOffense, int player) {
        if (isOffense) {
            learn(game, move, this.memoryOffensive);
        } else {
            learn(game, move, this.memoryDefensive);
        }
    }

    public void learn(String game, int move, HashMap<String, HashSet<Integer>> memory) {
        HashSet<Integer> moves;
        if (!memory.containsKey(game)) {
            moves = new HashSet<>();
        } else {
            moves = memory.get(game);
        }
        moves.add(move);
        memory.put(game, moves);
    }

    public HashSet<Integer> remember(String game) {
        HashSet<Integer> memoryDefensive = this.memoryDefensive.get(game);
        if (memoryDefensive == null || memoryDefensive.isEmpty()) {
            return this.memoryOffensive.get(game);
        }
        return memoryDefensive;
    }
}