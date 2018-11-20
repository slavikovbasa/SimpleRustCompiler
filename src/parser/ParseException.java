package parser;

/**
 * Exception that is thrown when unexpected token is found in the input text
 */
class ParseException extends RuntimeException {

    /**
     * Constructor to print the error message. Uses super constructor.
     */
    ParseException(String str){
        super(str);
    }
}