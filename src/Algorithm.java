import enums.KMeansCounter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;

import java.io.IOException;

public class Algorithm {
    private Integer iteration;
    private Configuration conf;
    private JobIterator currJobIterator;

    public Algorithm(Configuration configuration) {
        this.conf = configuration;
        iteration = 0;
        currJobIterator = null;
    }

    public void createNextJobIteration() {
        try {
            iteration++;
            conf.setInt("iteration", iteration);
            currJobIterator = new JobIterator(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean runCurrJob(){
        return currJobIterator.run();
    }

    public Boolean converged() {
        Boolean result = false;
        if (currJobIterator != null){
            Integer max = conf.getInt("max", 0);
            if (iteration < max){
                Counters counters;
                try {
                    counters = currJobIterator.getCounters();
                    Counter counter = counters.findCounter(KMeansCounter.ADJUSTED);
                    result = (0L == counter.getValue());
                    System.out.printf("Counter value = %s\n", counter.getValue());
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                System.out.print("Algorithm converged max iterations reached\n");
                result = true;
            }
        }
        if  (currJobIterator != null) System.out.print("Algorithm converged!\n");

        return result;
    }
}
