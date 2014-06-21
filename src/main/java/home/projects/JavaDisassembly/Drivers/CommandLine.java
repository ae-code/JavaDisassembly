package home.projects.JavaDisassembly.Drivers;

import java.io.IOException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import home.projects.JavaDisassembly.*;

public class CommandLine {

	public static void main(String[] args) throws IOException {
		// set up arguments parsing
        OptionParser parser = new OptionParser();
        parser.accepts("source", "source directory containing class files").withRequiredArg().required();
        parser.accepts("destination", "Destination directory for HTML files").withRequiredArg().required();
        parser.accepts("parallel", "Parallelization Factor (defaults to # cores)").withRequiredArg().ofType(Integer.class);
        parser.accepts("help", "Prints this usage information").forHelp();

        // actually parse args
        OptionSet options = null;
        
        try {
        	options = parser.parse(args);
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
            parser.printHelpOn(System.out);
            System.exit(1);
        }
        	
        // print help message
        if (options.has("help")) {
            parser.printHelpOn(System.out);
            System.exit(0);
        }
        
        // actually start
        System.out.println("JavaDisassembly Site Generator");
		System.out.println("==============================\n");
		
		final String sSourceDirectory = (String) options.valueOf("source");
		System.out.println("Source Directory: " + sSourceDirectory);
		
		final String sDestinationDirectory = (String) options.valueOf("destination");
		System.out.println("Destination Directory: " + sDestinationDirectory);
		
		Config config = new Config();
		config.setSourceDirectory(sSourceDirectory);
		config.setDestinationDirectory(sDestinationDirectory);
		if (options.has("parallel"))
			config.setParallelizationFactor((Integer) options.valueOf("parallel"));
		System.out.println("Parallelization Factor: " + config.getParallelizationFactor());
		
		CommandLineStatusHandler status = new CommandLineStatusHandler();
		
		System.out.println("\nStarting...\n");

		try {
			JavaDisassembly disassembler = new JavaDisassembly(config);
			disassembler.setStatusHandler(status);
			disassembler.generate();
		}
		catch (JavaDisassemblyException e) {
			System.out.println("An error was encountered:");
			System.out.println("\t" + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("JavaDisassembly completed");
	}
}
