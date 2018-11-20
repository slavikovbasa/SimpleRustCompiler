package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import static lexer.TokenType.*;

/**
 * Lexical analyze is done within ths class
 */
public class Lexer {

    /**
     * List of tokens, that is the result of analyze
     */
    private ArrayList<Token> tokens;

    /**
     * Input string with the code to be analyzed
     */
    private String inputText;

    /**
     * Position of the current token while analyzing
     */
    private static int pos;

    /**
     * Absolute position of the current token in the input text in rows and columns
     */
    private static int row, col;

    /**
     * String that contains all possible symbols
     */
    private static final String SYMBOLS = ":;=+-*/%&|^<>!.(){}";

    /**
     * HashMap that contains all operators with their string representations
     */
    private static final HashMap<String, TokenType> OPERATORS;
    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put(":", COLON);
        OPERATORS.put(";", SEMICOLON);
        OPERATORS.put("=", ASSIGN);
        OPERATORS.put("+=", PLUS_ASSIGN);
        OPERATORS.put("-=", MINUS_ASSIGN);
        OPERATORS.put("*=", ASTERISK_ASSIGN);
        OPERATORS.put("/=", SLASH_ASSIGN);
        OPERATORS.put("%=", MOD_ASSIGN);
        OPERATORS.put("&=", AND_ASSIGN);
        OPERATORS.put("|=", OR_ASSIGN);
        OPERATORS.put("^=", XOR_ASSIGN);
        OPERATORS.put("<<=", LSHIFT_ASSIGN);
        OPERATORS.put(">>=", RSHIFT_ASSIGN);
        OPERATORS.put("+", PLUS);
        OPERATORS.put("-", MINUS);
        OPERATORS.put("*", ASTERISK);
        OPERATORS.put("/", SLASH);
        OPERATORS.put("%", MOD);
        OPERATORS.put(">>", RSHIFT);
        OPERATORS.put("<<", LSHIFT);
        OPERATORS.put("&", AND);
        OPERATORS.put("|", OR);
        OPERATORS.put("&&", LAZY_AND);
        OPERATORS.put("||", LAZY_OR);
        OPERATORS.put("^", XOR);
        OPERATORS.put("!", EXCL);
        OPERATORS.put("==", EQ);
        OPERATORS.put("!=", NEQ);
        OPERATORS.put("<", LT);
        OPERATORS.put(">", GT);
        OPERATORS.put("<=", LTE);
        OPERATORS.put(">=", GTE);
        OPERATORS.put("..", RANGE);
        OPERATORS.put("(", LPARENT);
        OPERATORS.put(")", RPARENT);
        OPERATORS.put("{", LBRACE);
        OPERATORS.put("}", RBRACE);

    }

    /**
     * HashMap that contains all keywords with their string representations
     */
    private static final HashMap<String, TokenType> KEYWORDS;
    static {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("let", LET);
        KEYWORDS.put("mut", MUT);
        KEYWORDS.put("if", IF);
        KEYWORDS.put("else", ELSE);
        KEYWORDS.put("loop", LOOP);
        KEYWORDS.put("while", WHILE);
        KEYWORDS.put("for", FOR);
        KEYWORDS.put("in", IN);
        KEYWORDS.put("break", BREAK);
        KEYWORDS.put("continue", CONTINUE);
        KEYWORDS.put("i32", I32);
        KEYWORDS.put("bool", BOOL);
        KEYWORDS.put("fn", FN);
        KEYWORDS.put("main", MAIN);
        KEYWORDS.put("true", TRUE);
        KEYWORDS.put("false", FALSE);

    }

    /**
     * Constructor for the Lexer. Initializes pos = 0
     * @param inputText String that will be analyzed
     */
    public Lexer(String inputText){
        this.inputText = inputText;
        pos = 0;
        row = 0;
        col = 0;
        tokens = new ArrayList<>();
    }

    /**
     * Method that performs lexical analyze
     * Calls analyzeNumber() or analyzeWord() or analyzeOperator(), depending on the first found char
     * @return The list of analyzed tokens
     */
    public ArrayList<Token> analyze(){
        while(pos < inputText.length()){
            char curr = inputText.charAt(pos);
            if(curr >= '0' && curr <= '9')
                analyzeNumber();
            else if((curr >= 'A' && curr <= 'Z') || (curr >= 'a' && curr <= 'z') || curr == '_')
                analyzeWord();
            else if(SYMBOLS.contains(curr + ""))
                analyzeOperator();
            else if(curr == ' ' || curr == '\t' || curr == '\n')
                nextPos();
            else {
                throw new ScanException(String.format("Undefined char: \"%s\" at %d:%d", curr, row, col));
            }
        }
        return tokens;
    }

    /**
     * Analyze constant numbers
     */
    private void analyzeNumber(){
        boolean undefined = false;
        StringBuilder buf = new StringBuilder();
        char curr;
        try {
            curr = inputText.charAt(pos);
        } catch (IndexOutOfBoundsException e){
            return;
        }
        while(true){
            buf.append(curr);
            nextPos();
            try {
                curr = inputText.charAt(pos);
            } catch (IndexOutOfBoundsException e){
                break;
            }
            if(!undefined && ((curr >= 'a' && curr <= 'z') || (curr >= 'A' && curr <= 'Z')))
                undefined = true;

            if(!((curr >= 'a' && curr <= 'z') || (curr >= 'A' && curr <= 'Z') || (curr >= '0' && curr <= '9') || curr == '_'))
                break;
        }
        if(undefined) {
            throw new ScanException(String.format("Undefined token: \"%s\" at %d:%d", buf.toString(), row, col - buf.toString().length()));
        }
        tokens.add(new Token(buf.toString(), TokenType.NUM, row, col - buf.toString().length()));
    }

    /**
     * Analyze keywords and variables
     */
    private void analyzeWord(){
        StringBuilder buf = new StringBuilder();
        char curr;
        try {
            curr = inputText.charAt(pos);
        } catch (IndexOutOfBoundsException e){
            return;
        }
        while(true){
            buf.append(curr);
            nextPos();
            try {
                curr = inputText.charAt(pos);
            } catch (IndexOutOfBoundsException e){
                break;
            }
            if(!((curr >= 'a' && curr <= 'z') || (curr >= 'A' && curr <= 'Z') || (curr >= '0' && curr <= '9') || curr == '_'))
                break;
        }

        tokens.add(new Token(buf.toString(), KEYWORDS.getOrDefault(buf.toString(), TokenType.VAR),
                                                                                    row, col - buf.toString().length()));
    }

    /**
     * Analyze operators
     */
    private void analyzeOperator(){
        char curr;
        try {
            curr = inputText.charAt(pos);
        } catch (IndexOutOfBoundsException e){
            return;
        }
        if(pos < inputText.length() - 2 && OPERATORS.containsKey(curr + "" + inputText.charAt(pos + 1) + "" + inputText.charAt(pos + 2))){
            tokens.add(new Token(curr + "" + inputText.charAt(pos + 1) + "" + inputText.charAt(pos + 2),
                                OPERATORS.get(curr + "" + inputText.charAt(pos + 1) + "" + inputText.charAt(pos + 2)), row, col));
            nextPos();
            nextPos();
            nextPos();
            return;
        }
        if(pos < inputText.length() - 1 && OPERATORS.containsKey(curr + "" + inputText.charAt(pos + 1))){
            tokens.add(new Token(curr + "" + inputText.charAt(pos + 1),
                    OPERATORS.get(curr + "" + inputText.charAt(pos + 1)), row, col));
            nextPos();
            nextPos();
            return;
        }
        if(SYMBOLS.contains(curr + "") && !OPERATORS.containsKey(curr + "")){
            throw new RuntimeException(String.format("Undefined token: \"%s\" at %d:%d", curr, row, col));
        }
        tokens.add(new Token(curr + "", OPERATORS.get(curr + ""), row, col));
        nextPos();
    }

    /**
     * Get next position and change the column and row count
     */
    private void nextPos(){
        pos++;
        if(pos < inputText.length() && inputText.charAt(pos) != '\n'){
            col++;
        } else {
            row++;
            col = 0;
        }
    }
}
