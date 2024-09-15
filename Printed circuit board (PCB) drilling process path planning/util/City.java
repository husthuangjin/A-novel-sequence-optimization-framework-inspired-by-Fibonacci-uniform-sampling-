package util;

public class City {

    private final boolean isPrime;

    public final int id;
    public final double[] coordinates;
    // public final double x;
    // public final double y;

    public City(int id, double... coordinates) {
        this.id = id;
        // this.x = x;
        // this.y = y;
        this.coordinates = coordinates;
        this.isPrime = isPrime(id);
    }

    public boolean isPrime() {
        return isPrime;
    }

    public double distanceTo(City that) {
        double sumOfSquares = 0;  
        for (int i = 0; i < this.coordinates.length; i++) {  
            sumOfSquares += Math.pow(this.coordinates[i] - that.coordinates[i], 2);  
        }  
        return Math.sqrt(sumOfSquares);  
    }

    private static boolean isPrime(int n) {
        for (int i = 2; 2 * i < n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
