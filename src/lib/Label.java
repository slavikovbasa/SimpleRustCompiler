package lib;

/**
 * Singletone class for creating unique labels
 */
public class Label {

    /**
     * Label unique index
     */
    private static int i = 0;

    /**
     * Private constructor
     */
    private Label(){}

    /**
     * Get curr label identifier and inc i
     * @return Unique string for the label
     */
    public static String label(){
        String res = "label" + i;
        i++;
        return res;
    }
}
