import distances.Distance;
import iterators.CentroidIterator;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import writables.Centroid;
import writables.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class KMeansMapper extends Mapper<LongWritable, Text, Centroid, Point> {
    private Point point;
    private CentroidIterator centroidIterator;

    @Override
    protected void setup(Context context) throws IOException {
        point = new Point();
        centroidIterator = new CentroidIterator(Distance.getDistance(context.getConfiguration().get("distance")));

        List<URI> uris = Arrays.asList(context.getCacheFiles());
        for (URI uri: uris){
            FileSystem fs = FileSystem.get(context.getConfiguration());
            InputStreamReader ir = new InputStreamReader(fs.open(new Path(uri)));
            BufferedReader br = new BufferedReader(ir);

            String line = br.readLine();
            while (line != null){
                centroidIterator.add(line);
                line = br.readLine();
            }
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) {
        try {
            point.parse(value.toString());
            context.write(centroidIterator.closest(point), point);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
