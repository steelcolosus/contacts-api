package utils.exceptions;

/**
 * Created by eduardo on 12/03/15.
 */
public class NotFoundException extends Exception
{
    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
