import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import writables.Centroid;
import writables.Point;


import java.io.IOException;

public class JobIterator extends Job {
    private static final String SEP = System.getProperty("file.separator");
    public JobIterator(Configuration conf) throws IOException {
        super(conf);
        setJarByClass(getClass());
        addCacheFiles();

        FileOutputFormat.setOutputPath(this, new Path(getOutputPath()));
        FileInputFormat.addInputPath(this, new Path(conf.get("input")));

        setMapperClass(KMeansMapper.class);
        setCombinerClass(KMeansCombiner.class);
        setPartitionerClass(KMeansPartitioner.class);
        setReducerClass(KMeansReducer.class);

        setMapOutputKeyClass(Centroid.class);
        setMapOutputValueClass(Point.class);
        setOutputKeyClass(Text.class);
        setOutputValueClass(NullWritable.class);

        setNumReduceTasks(conf.getInt("number", getNumReduceTasks()));
    }
    private void addCacheFiles() throws IOException {
        Integer iteration = conf.getInt("iteration", 0);
        if (iteration > 1) {
            String output = conf.get("output") + SEP + String.valueOf(conf.getInt("iteration", 0) - 1);
            Path out = new Path(output, "part-r-[0-9]*");
            System.out.println("Checking path " + out.toString());
            FileSystem fs = FileSystem.get(conf);
            FileStatus[] ls = fs.globStatus(out);
            for (FileStatus fileStatus : ls) {
                Path pfs = fileStatus.getPath();
                System.out.println("Adding " + pfs.toUri().toString());
                addCacheFile(pfs.toUri());
            }
        } else {
            Path pfs = new Path(conf.get("state"));
            System.out.println("First iteration adding " + pfs.toUri().toString());
            addCacheFile(pfs.toUri());
        }
    }
    private String getOutputPath(){
        return conf.get("output") + SEP + String.valueOf(conf.getInt("iteration", 0));
    }

    private void deleteOutputDirectory() {
        Path output = new Path(getOutputPath());
        try {
            FileSystem dfs = FileSystem.get(getConfiguration());
            if (dfs.isDirectory(output)) {
                dfs.delete(output, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean run() {
        deleteOutputDirectory();

        Boolean result = false;
        try {
            result = waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
