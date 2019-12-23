import de.daikol.motivator.Messages;
import org.junit.Test;

public class FormatterTest {

    @Test
    public void format() {
        System.out.println(String.format(Messages.NEWS_COMPETITION_CREATED, "Competition", "User"));
    }

}
