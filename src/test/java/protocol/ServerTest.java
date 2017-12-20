package protocol;

import org.junit.Test;

/**
 * Created by hasee on 2017/12/20.
 */
public class ServerTest {

    @Test
    public void bind() {
        try {
            new Server().bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
