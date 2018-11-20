package lexer;

/**
 * Exception that is thrown when undefined symbol or token is found in the input text
 */
class ScanException extends RuntimeException {

    /**
     * Constructor. Uses super constructor.
     * @param string String to print along with the exception
     */
    ScanException(String string){
        super(string);
    }
}
