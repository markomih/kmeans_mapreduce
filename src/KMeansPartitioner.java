import org.apache.hadoop.mapreduce.Partitioner;
import writables.*;

public class KMeansPartitioner extends Partitioner<Centroid, Point> {
    @Override
    public int getPartition(Centroid key, Point value, int numPartitions) {

        return Integer.valueOf(key.getLabel()) % numPartitions;
    }
}
