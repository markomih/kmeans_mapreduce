import distances.Distance;
import enums.KMeansCounter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Reducer;
import writables.Centroid;
import writables.Point;


public class KMeansReducer extends Reducer<Centroid, Point, Text, NullWritable> {
    private Double delta = 0.;
    private Text outKey = new Text();
    private Counter adjusted;
    private Distance distance;

    @Override
    protected void setup(Context context) {
        adjusted = context.getCounter(KMeansCounter.ADJUSTED);
        Configuration conf = context.getConfiguration();
        delta = conf.getDouble("delta", 0.);
        distance = Distance.getDistance(conf.get("distance"));
    }

    @Override
    protected void reduce(Centroid key, Iterable<Point> values, Context context) {
        Centroid newKey = new Centroid(key.getText(), distance.getExpectation(values));
        try {
            double similarity = distance.getDistance(key.getPoint(), newKey.getPoint());
            if (similarity > delta){
                adjusted.increment(1L);
            }

            outKey.set(newKey.toString());
            context.write(outKey, NullWritable.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
