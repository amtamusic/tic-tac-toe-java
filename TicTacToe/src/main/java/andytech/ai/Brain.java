package andytech.ai;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Author: Andres Toledo
 * Class used to remember past games and plays.
 *
 * @author amtam
 * @version $Id: $Id
 */
@Data
public class Brain implements Serializable {

    /**
     * Used to store offensive plays.
     */
    HashMap<String, HashSet<Integer>> memoryOffensive;
    /**
     * Used to store defensive plays.
     */
    HashMap<String, HashSet<Integer>> memoryDefensive;

    /**
     * Initializes class variables.
     */
    public Brain() {
        this.memoryOffensive = new HashMap<>();
        this.memoryDefensive = new HashMap<>();
    }

    /**
     * Remembers plays passed in from games played by {@link andytech.ai.AI} class
     *
     * @param game      Game board as string.
     * @param move      Proper move to make.
     * @param isOffense Tag play as offensive or defensive.
     * @param player    0 or 1 mapped to x or y.
     */
    public void learn(String game, int move, boolean isOffense, int player) {
        if (isOffense) {
            learn(game, move, this.memoryOffensive);
        } else {
            learn(game, move, this.memoryDefensive);
        }
    }

    /**
     * Remembers plays based on weather they are offensive or defensive.
     *
     * @param game   Game board as string.
     * @param move   Proper move to make.
     * @param memory {@link java.util.HashMap} representing {@link andytech.ai.Brain#memoryOffensive} or {@link andytech.ai.Brain#memoryDefensive}
     */
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

    /**
     * Remember play based on status of a board.
     *
     * @param game Game board as string.
     * @return Possible moves to make.
     */
    public HashSet<Integer> remember(String game) {
        HashSet<Integer> memoryDefensive = this.memoryDefensive.get(game);
        if (memoryDefensive == null || memoryDefensive.isEmpty()) {
            return this.memoryOffensive.get(game);
        }
        return memoryDefensive;
    }
}
