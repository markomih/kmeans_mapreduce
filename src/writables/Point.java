package writables;

import org.apache.hadoop.io.ArrayPrimitiveWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Point implements Writable {
    private ArrayPrimitiveWritable vector = null;
    private IntWritable number = null;

    public Point(){
        vector = new ArrayPrimitiveWritable();
        number = new IntWritable(0);
    }
    public Point(double[] vector, int number) {
        this();
        setVector(vector);
        setNumber(number);
    }
    public Point(Point point){
        this();
        double[] vector = point.getVector();
        setVector(Arrays.copyOf(vector, vector.length));
        setNumber((int)point.getNumber());
    }

    public double[] getVector() {
        return (double[]) vector.get();
    }

    public double getNumber() {
        return (double) number.get();
    }

    public void setVector(double[] vector) {
        this.vector.set(vector);
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        vector.write(dataOutput);
        number.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        vector.readFields(dataInput);
        number.readFields(dataInput);
    }

    @Override
    public String toString() {
        double[] thisVector = this.getVector();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = thisVector.length; i < j; i++) {
            sb.append(thisVector[i]);
            if (i < thisVector.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public void parse(String values) {
        String[] coords = values.split(" ");
        double[] tmp = new double[coords.length];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = Double.valueOf(coords[i]);
        }

        vector.set(tmp);
        number.set(1);
    }

    public void add(Point point){
        double[] thisVector = this.getVector();
        double[] pointVector = point.getVector();
        for (int i =0; i<thisVector.length; i++){
            thisVector[i] += pointVector[i];
        }
        number.set(number.get() + (int) point.getNumber());
    }

    public void add(double[] otherVector, int num){
        double[] thisVector = this.getVector();
        for(int i = 0; i < thisVector.length; i++){
            thisVector[i] = thisVector[i] + otherVector[i];
        }
        number.set(number.get() + num);
        vector.set(thisVector);
    }

    public void compress(){
        double [] thisVector = this.getVector();
        double currentNum = number.get();
        for (int i = 0; i < thisVector.length; i++){
            thisVector[i] /= currentNum;
        }

        number.set(1);
        this.vector.set(thisVector);
    }

}
