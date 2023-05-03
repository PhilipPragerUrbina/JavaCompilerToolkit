package Lexicographer.FrontEnd;

/**
 * Helps format simple generated java code and makes the generation more readable
 */
public class JavaGenerator {
    private final StringBuilder java_code = new StringBuilder();
    private int depth = 0; //Curly brace depth for tab formatting
    boolean in_line = false; //If currently within a line. Used for formatting.

    /**
     * Start generating java code
     * @param package_name Name of package that the code belongs in
     * @param imports Names of classes to import
     */
    public JavaGenerator(String package_name, String... imports){
        addLine("package " + package_name + ";");
        for (String import_name : imports) {
            addLine("import " + import_name + ";");
        }
    }

    /**
     * Generate java code from scratch
     * For partial java code
     */
    public JavaGenerator(){}

    /**
     * Remove the last char from the code generated so far
     * Useful for removing the last comma in a list
     */
    private void removeLastChar(){
        java_code.deleteCharAt(java_code.length()-1);
    }

    /**
     * Start a public java class
     * @param name Name of class
     * @param parent Parent class or null
     * @param implementing Any interfaces the class should implement or null
     */
    public void openClass(String name,String parent, String... implementing){
        add("public class " + name);
        if(parent != null){
            add(" extends " + parent);
        }
        if(implementing != null){
            add(" implements ");
            for (String interface_name: implementing) {
                add(interface_name + ",");
            }
            removeLastChar(); //Remove last comma
        }
        addLine(" {");
        depth++;
    }

    /**
     * End a java class. Same as closeBlock but for better readability
     */
    public void closeClass(){
        closeBlock();
    }

    /**
     * Start an initialized array variable
     * @param type Array type(without brackets). You can also specify final and private/public
     * @param name Name of array
     */
    public void openList(String type, String name){
        add(type + "[] " + name + " = {");
    }

    /**
     * Add an element to an opened list
     * @param element The element to add. Does not need a comma.
     */
    public void addListElement(String element){
        add(element + ",");
    }

    /**
     * Close an opened list
     */
    public void closeList(){
        removeLastChar(); //remove last comma
        addLine("};");
    }

    /**
     * Get a string literal from a string
     * @param str What should be inside the string
     * @return String literal to put in generated code
     */
    public static String getStringLiteral(String str){
        return "\"" + str + "\"";
    }

    /**
     * Take a string of regex and add escape characters
     * @param regex The regex string
     * Make sure regex has been imported to the generated code
     * @return The declaration for a regex pattern
     */
    public static String getRegexPattern(String regex){
        //adds double escape characters where escape characters are present. Once to escape for java, and again to escape for regex.
        return "Pattern.compile(\"" + regex.replaceAll("\\\\(?!\")", "\\\\\\\\") + "\")";
    }


    /**
     * Open a method declaration
     * @param name Name of method
     * @param return_type Return type of method
     * @param parameters Any parameters. For example "int foo"
     * @param is_public Is the method public
     */
    public void openMethod(String name, String return_type,  boolean is_public,String... parameters){
        add((is_public ? "public " : "private ") + return_type + " " + name + "(");
        for (String parameter: parameters) {
            add(parameter + ",");
        }
        removeLastChar();
        addLine("){");

        depth++;
    }

    /**
     * End a java method. Same as closeBlock but for better readability
     */
    public void closeMethod(){
        closeBlock();
    }

    /**
     * Open a block
     * @param type if, while, for
     * @param contents Conditional statements or for loop statements.
     */
    public void openBlock(String type, String contents){
        addLine(type + "(" + contents + "){");
        depth++;
    }

    /**
     * Close a block
     */
    public void closeBlock(){
        depth--;
        addLine("}");
    }

    /**
     * Add a line with the code
     * @param text The text to be followed by a newline
     */
    public void addLine(String text){
        if(!in_line) java_code.append("\t".repeat(Math.max(0, depth))); //add a tab. Replace with spaces if you prefer.
        in_line = false;
        java_code.append(text).append('\n');

    }

    /**
     * Add a line comment
     * @param comment Line comment contents
     */
    public void comment(String comment){
        addLine("//" + comment);
    }

    /**
     * Add code
     * @param text The code to add
     */
    public void add(String text){
        if(!in_line) java_code.append("\t".repeat(Math.max(0, depth))); //add a tab. Replace with spaces if you prefer.
        in_line = true;
        java_code.append(text);
    }

    /**
     * Get the java code that has been generated
     * @throws RuntimeException Invalid code detected
     */
    public String getCode() throws RuntimeException{
        if(depth != 0){
            throw new RuntimeException("Unclosed blocks in generated code.");
        }
        return java_code.toString();
    }

}
