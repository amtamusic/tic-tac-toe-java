import lombok.Data;

@Data
public class Player {
    String name;

    public Player(String name) {
        this.name = name;
    }
}
