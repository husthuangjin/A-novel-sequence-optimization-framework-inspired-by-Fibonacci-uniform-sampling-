package solver.cost;

import util.City;

public class CostMatrix implements Costs {

    private final double[][] costs;

    public CostMatrix(City[] cities) {
        costs = new double[cities.length][cities.length];
        for (int i = 0; i < cities.length; i++) {
            for (int j = 0; j < i; j++) {
                double cost = cities[i].distanceTo(cities[j]);
                costs[i][j] = cost;
                costs[j][i] = cost;
            }
        }
    }

    public double get(int a, int b) {
        return costs[a][b];
    }

    public int nCities() {
        return costs.length;
    }
}
