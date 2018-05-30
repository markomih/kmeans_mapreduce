package writables;

import com.sun.istack.NotNull;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Centroid implements WritableComparable<Centroid> {
    private Text label;
    private Point point;

    public Centroid() {
        label = new Text();
        point = new Point();
    }

    public Centroid(Text label, Point point) {
        this.label = label;
        this.point = point;
    }

    public Centroid(String value) {
        this();
        int index = value.indexOf("\t");
        if (index == -1) {
            index = value.indexOf(" ");
        }
        label.set(value.substring(0, index).replace(".", ""));
        point.parse(value.substring(index + 1, value.length()));
    }

    public String getLabel() {
        return label.toString();
    }

    public Text getText() {
        return label;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public int compareTo(@NotNull Centroid centroid) {
        return this.label.compareTo(centroid.getText());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        label.write(dataOutput);
        point.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        label.readFields(dataInput);
        point.readFields(dataInput);
    }

    @Override
    public String toString() {
        return this.getLabel() + " " + this.getPoint();
    }

}
