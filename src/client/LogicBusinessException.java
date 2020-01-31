package client;

/**
 * An Exception that is throwed in some client methods.
 *
 * @author Daira Eguzkiza
 */
public class LogicBusinessException extends Exception {

    public LogicBusinessException(String msg) {
        super(msg);
    }
}
