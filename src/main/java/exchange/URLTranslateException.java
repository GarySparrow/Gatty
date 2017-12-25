package exchange;

/**
 * Created by hasee on 2017/12/24.
 */
public class URLTranslateException extends Exception{

    private String message = "";

    public URLTranslateException() {
        message = "translate failed. check your url format please.";
    }

    public URLTranslateException(String message) {
        super(message);
    }
}
