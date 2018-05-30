package distances;

import writables.Point;

public class EuclideanDistance extends Distance {
    @Override
    public double getDistance(Point p1, Point p2) throws Exception {
        double[] p1Vector = p1.getVector();
        double[] p2Vector = p2.getVector();

        if (p1Vector.length != p2Vector.length) throw new Exception("Invalid length");

        double sum = 0f;
        for (int i = 0; i < p1Vector.length; i++){
            sum += Math.pow(p1Vector[i] - p2Vector[i], 2);
        }

        return Math.sqrt(sum);
    }

    @Override
    public Point getExpectation(Iterable<Point> points) {
        Point result = sumPoints(points);

        if (result != null) {
            result.compress();
        }
        return result;
    }
}
