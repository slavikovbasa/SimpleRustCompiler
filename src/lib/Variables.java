package lib;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class that containes HashMap with all declared variables
 */
public class Variables {

    /**
     * HashMap of declared variables, key is the variable name, value is its features
     */
    private static HashMap<String, Variable> variables;
    static{
        variables = new HashMap<>();
    }

    /**
     * Private constructor
     */
    private Variables(){}

    /**
     * Variable getter
     * @param key Name of the variable, which features are returned
     * @return Variable class, features of the key variable
     */
    public static Variable get(String key){
        return variables.get(key);
    }

    /**
     * Add the variable to the Map
     * @param key Variable name
     * @param variable Variable features
     */
    public static void set(String key, Variable variable){
        variables.put(key, variable);
    }

    /**
     * Check if variable was declared
     * @param key Variable name
     * @return True if variables HashMap containes key variable, else false
     */
    public static boolean contains(String key){
        return variables.containsKey(key);
    }

    /**
     * Method that constructs declaration block in asm x86 code
     * @return String that is asm x86 code
     */
    public static String toAsm86(){
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Variable> entry : variables.entrySet()) {
            builder.append(entry.getKey()).append(" dd ?\n");
        }
        return builder.toString();
    }
}
