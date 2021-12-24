package andytech.game;

import lombok.Data;

/**
 * Author: Andres Toledo
 * Player classes representing tic-tac-toe player.
 *
 * @author amtam
 * @version $Id: $Id
 */
@Data
public class Player {
    /**
     * Represents x,o.
     */
    String name;

    /**
     * Initializes player
     *
     * @param name x or o.
     */
    public Player(String name) {
        this.name = name;
    }
}
