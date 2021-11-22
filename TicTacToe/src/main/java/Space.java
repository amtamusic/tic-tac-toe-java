import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Space {
    String value;
    public Space()
    {
        this.value="";
    }
    public Space(String value)
    {
        this.value=value;
    }
}
