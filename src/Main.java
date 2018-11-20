import ast.Expression;
import lexer.Lexer;
import lexer.Token;
import lib.Variables;
import parser.Parser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Main class that launches the compiling process
 */
public class Main {

    /**
     * Executable method
     * @param args Comand line arguments
     * @throws IOException Throws IOEXception if input or output files are not allowed to read/write
     */
    public static void main(String[] args) throws IOException{
        String input = new String(Files.readAllBytes(Paths.get(args[0])), "UTF-8");
        input = input.replaceAll(System.lineSeparator(), "\n");
        Lexer lexer = new Lexer(input);
        ArrayList<Token> tokens = lexer.analyze();
        Parser parser = new Parser(tokens);
        Expression root = parser.parse();
        System.out.println("Compiling successful");
        String header = ".386\n.model flat, stdcall\n.data\n";
        StringBuilder builder = new StringBuilder(header);
        String vars = Variables.toAsm86();
        builder.append(vars).append(".code\nmain:\n").append(root.getAsm86()).append("end main");
        String res = builder.toString();
        res = optimise(res);
        Files.write(Paths.get(args[1]), res.replaceAll("\n", System.lineSeparator()).getBytes());
    }

    /**
     * Optimise input string
     * @param inputString String to optimise
     * @return Optimised string
     */
    private static String optimise(String inputString){
        return inputString.replaceAll("\npush eax\npop eax", "");
    }
}
