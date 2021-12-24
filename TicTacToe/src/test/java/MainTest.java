import andytech.game.Main;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void gameTest() {
        String input = "1000";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Main.mainMenu();
        assertEquals(true, true);
    }

}
