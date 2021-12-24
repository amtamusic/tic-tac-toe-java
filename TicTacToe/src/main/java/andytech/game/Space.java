package andytech.game;

import lombok.Data;
import lombok.ToString;

/**
 * Author: Andres Toledo
 * Class representing board spaces.
 *
 * @author amtam
 * @version $Id: $Id
 */
@Data
@ToString
public class Space {
    /**
     * Represents content of a space on the board.
     */
    String value;

    /**
     * Initializes space with given value.
     *
     * @param value x,o, or -
     */
    public Space(String value) {
        this.value = value;
    }
}
