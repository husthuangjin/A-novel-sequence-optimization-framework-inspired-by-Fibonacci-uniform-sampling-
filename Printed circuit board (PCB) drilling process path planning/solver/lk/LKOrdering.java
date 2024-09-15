package solver.lk;

import solver.cost.Costs;
import tour.Tour;
import util.CityScore;

// import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class LKOrdering {

    private final Costs costs;
    private final Tour tour;

    private final int base;
    private final int prevBase;
    private final int nextBase;

    private final int nCities;
    private final int maxSize;
    private final int[] lkOrdering;
    private int nextNeighbors;


    public LKOrdering(int base, Tour tour, Costs costs, int maxSize) {
        this.costs = costs;
        this.tour = tour;

        this.base = base;
        prevBase = tour.prev(base);
        nextBase = tour.next(base);

        this.maxSize = maxSize;
        this.nCities = costs.nCities();
        lkOrdering = getLkOrdering();
        nextNeighbors = 0;
    }

    public boolean hasNextNeighbor(int level) {
        if (level > 3) {
            return false;
        }
        return nextNeighbors < lkOrdering.length;
    }

    public int getNextNeighbor() {
        return Math.abs(lkOrdering[nextNeighbors++]);
    }

    public boolean isMakMortonMove() {
        if (nextNeighbors == 0) throw new RuntimeException();
        return lkOrdering[nextNeighbors-1] < 0;
    }

    private double linKernighanScore(int a, int b) {
        return costs.get(tour.prev(a), a) - costs.get(tour.next(b), a);
    }

    private double makMortonScore(int a, int b) {
        return costs.get(a, tour.next(a)) - costs.get(b, a);
    }

    private int[] getLkOrdering() {
        final int size = nCities - 3;
        CityScore[] scoredCities = new CityScore[size * 2];
        int nScoredCities = 0;
        for (int c = 0; c < nCities; c++) {
            if (c == base || c == nextBase || c == prevBase) {
                continue;
            }
            scoredCities[nScoredCities++] = new CityScore(c, linKernighanScore(c, base));
            scoredCities[nScoredCities++] = new CityScore(-c, makMortonScore(c, base));
        }
        Arrays.sort(scoredCities, Comparator.comparingDouble((CityScore a) -> -a.score));
        // Only keep the best neighbors.
        int[] neighbors = new int[maxSize];
        for (int i = 0; i < neighbors.length; i++) {
            neighbors[i] = scoredCities[i].index;
        }
        return neighbors;
    }
}