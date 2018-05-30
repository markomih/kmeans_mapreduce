import org.apache.commons.cli.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Main extends Configured implements Tool {
    private static Options getOptions() {
        Options options = new Options();
        options.addOption(new Option("i", "input", true, "Path to the input points data"));
        options.addOption(new Option("s", "state", true, "Path to the input clusters data"));
        options.addOption(new Option("n", "number", true, "Number of reducers to suggest to the Hadoop job/n"));
        options.addOption(new Option("o", "output", true, "Path to write the output for iteration n - i.e. output/n"));
        options.addOption(new Option("d", "delta", true, "Delta definining the maximum difference between the last and current centroids"));
        options.addOption(new Option("m", "max", true, "Maximum number of iterations"));
        options.addOption(new Option("dist", "distance", true, "Distance measure"));

        return options;
    }

    private static void printOptions(CommandLine commandLine) {
        System.out.println("####################");
        System.out.println("input=" + commandLine.getOptionValue("input"));
        System.out.println("state=" + commandLine.getOptionValue("state"));
        System.out.println("output=" + commandLine.getOptionValue("o"));
        System.out.println("max=" + commandLine.getOptionValue("m"));
        System.out.println("number=" + commandLine.getOptionValue("number"));
        System.out.println("delta=" + commandLine.getOptionValue("delta"));
        System.out.println("distance=" + commandLine.getOptionValue("distance"));
        System.out.println("####################");
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Main(), args);
        System.exit(exitCode);
    }

    @Override
    public int run(String[] args) throws Exception {
        Options options = getOptions();

        CommandLineParser parser = new GnuParser();
        CommandLine commandLine = parser.parse(options, args);  // parse input parameters
        printOptions(commandLine); // print out input parameters

        // setting configuration parameters
        Configuration conf = getConf();
        conf.set("input", commandLine.getOptionValue("input"));
        conf.set("state", commandLine.getOptionValue("state"));
        conf.set("distance", commandLine.getOptionValue("distance"));
        conf.setInt("number", Integer.valueOf(commandLine.getOptionValue("number")));
        conf.set("output", String.valueOf(commandLine.getOptionValue("output")));
        conf.setDouble("delta", Double.valueOf(commandLine.getOptionValue("delta")));
        conf.setInt("max", Integer.valueOf(commandLine.getOptionValue("max")));

        // execute several MapReduce programs until kmeans converge
        Algorithm kmeans = new Algorithm(conf);
        while (!kmeans.converged()){
            kmeans.createNextJobIteration();
            if (!kmeans.runCurrJob()) throw new Exception("Current job failed!");
        }

        return 0;
    }
}
