package Lexicographer.FrontEnd.Generators;

/**
 * Defines how different constructs are created in the target language of java
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
     * Start a java class
     * @param name Name of class
     * @param parent Parent class or null
     * @param implementing Any interfaces the class should implement or null
     */
    public void openClass(String name,String parent, String... implementing){


        add("class " + name);
        if(parent != null){
            add(" extends " + parent);
        }
        if(implementing != null){
            add(" implementing ");
            for (String interface_name: implementing) {
                add(interface_name + ",");
            }
            removeLastChar(); //Remove last comma
        }
        addLine(" {");
        depth++;
    }

    /**
     * End a java class
     */
    public void closeClass(){
        depth--;
        addLine("}");
    }

    /**
     * Create a variable
     * @param type Type name
     * @param name Variable name
     * @param default_value The default value or null
     * @param is_final If the variable is considered final
     * @param is_public Is it a public variable
     */
    public void createVariable(String type, String name, String default_value, boolean is_final, boolean is_public){
        addLine((is_public ? "public " : "private ")+(is_final ? "final " : "")+ type + " " + name + (default_value != null ? " = " + default_value : "") + ";");
    }

    /**
     * Create a local variable
     * @param type Type name
     * @param name Variable name
     * @param default_value The default value or null
     */
    public void createVariable(String type, String name, String default_value){
        addLine(type + " " + name + (default_value != null ? " = " + default_value : "") + ";");
    }

    /**
     * Start an initialized array variable
     * @param type Array type(without brackets)
     * @param name Name of array
     * @param is_final If the array is final
     */
    public void openList(String type, String name, boolean is_final){
        add((is_final ? "final " : "")+ type + "[] " + name + " = {");
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
    public static String formatString(String str){
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
     * End a java method
     */
    public void closeMethod(){
        depth--;
        addLine("}");
    }


    /**
     * Remove the last char from the code generated so far
     * Useful for removing the last comma in a list
     */
    private void removeLastChar(){
        java_code.deleteCharAt(java_code.length()-1);
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
