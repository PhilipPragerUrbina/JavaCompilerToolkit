package JavaCompilerToolkit.Lexicographer.IO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Parse command line arguments
 */
public class CommandParser {
    private final HashMap<String, List<String>> argument_map = new HashMap<>();

    /**
     * Syntax error parsing command line args
     */
    public static class ArgumentSyntaxError extends RuntimeException{
        public ArgumentSyntaxError(String message){
            super(message);
        }
    }

    /**
     * Parses command line arguments and flags.
     * Flags start with '-'. Any number of following arguments are added to the flag.
     * If flag is specified multiple times, arguments will still be added to the flag.
     * Flags can be specified without arguments.
     * @param args Command line arguments passed in by java main
     * @throws ArgumentSyntaxError Problem parsing invalid arguments. All arguments must have a preceding flag.
     */
    public CommandParser(String[] args) throws ArgumentSyntaxError {
        String current_flag = "";
        for (int i = 0; i < args.length; i++) {
            if(args[i].startsWith("-")){ //Is a flag
                current_flag = args[i]; //Add flag
                if(!argument_map.containsKey(current_flag)){ //Check if it doesn't exist yet
                    argument_map.put(current_flag, new ArrayList<>());
                }
            }else{
                List<String> arguments = argument_map.get(current_flag);
                if(arguments == null) throw new ArgumentSyntaxError("Argument " + i + " does not have a preceding flag!"); //Check if flag exists
                arguments.add(args[i]); //Add arg to flag
            }
        }
    }

    /**
     * Get arguments corresponding to flag
     * @param flag Flag including '-'
     * @return Arguments or null if flag not found
     */
    public List<String> getArguments(String flag){
        return argument_map.get(flag);
    }

    /**
     * Check if a flag is specified at all
     * @param flag Flag to check
     * @return True if flag was specified at all.
     */
    public boolean flagSpecified(String flag){
        return argument_map.containsKey(flag);
    }

    /**
     * Validate that only supported flags are specified.
     * @param allowed_flags What flags are supposed to be used. Includes '-'.
     * @apiNote Useful to make sure user does not spell a flag wrong and not get what they want.
     * @throws ArgumentSyntaxError Unsupported flag found
     */
    public void validateFlags(List<String> allowed_flags) throws ArgumentSyntaxError {
        for (String flag : argument_map.keySet()) {
            if(!allowed_flags.contains(flag)) throw new ArgumentSyntaxError("Flag " + flag + " is not a supported flag!");
        }
    }

    /**
     * Validate that only supported flags are specified.
     * @param allowed_flags What flags are supposed to be used. Includes '-'.
     * @apiNote Useful to make sure user does not spell a flag wrong and not get what they want.
     * @throws ArgumentSyntaxError Unsupported flag found
     */
    public void validateFlags(String... allowed_flags) throws ArgumentSyntaxError {
       validateFlags(Arrays.asList(allowed_flags)); //convert array to list.
    }
}
