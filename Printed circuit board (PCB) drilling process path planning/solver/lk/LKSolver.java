package solver.lk;
import solver.Solver;
import solver.cost.CostMatrix;
import solver.cost.Costs;
import tour.ArrayTour;
import tour.Tour;
import util.*;

import java.util.Random;

public class LKSolver extends Solver {

    private final Random rng;

    private static final double EPSILON = 0.00001;
    private static final int MAX_LK_ORDERING = 15;

    // A local pointer to the cities to avoid useless indirection.
    private final City[] cities;
    private final int nCities;

    private final Tour tour;
    private final Costs costs;

    private LKOrdering[] allLkOrderings;

    private Stack<SearchLevel> searchStack = new Stack<>();
    private Stack<TupleInt> flipSequence = new Stack<>();
    private boolean improvingSequenceFound = false;

    public LKSolver(Instance instance, int seed) {
        rng = new Random(seed);
        cities = instance.getCities();
        nCities = cities.length;
        // Build the first tour.
        tour = new ArrayTour(nCities);
        // Build cost matrix.
        costs = new CostMatrix(cities);
        // Structure to hold LK Ordering.
        allLkOrderings = new LKOrdering[nCities];
    }

    @Override
    public Solution solve(int[] seq) {
        printTourCost();
        linKernighanSearch(seq);
        int[] bestTour = tour.toArray();
        double bestdis = getTourCost();
        return new Solution(bestTour, bestdis);
    }

    private double getTourCost() {
        int[] initialTour = tour.toArray();
        double cost = costs.get(initialTour[initialTour.length - 1], initialTour[0]);
        for (int i = 1; i < initialTour.length; i++) {
            cost += costs.get(initialTour[i - 1], initialTour[i]);
        }
        return cost;
    }

    private void printTourCost() {
        System.out.println(String.format("Current tour cost: %s.", getTourCost()));
    }

    public void linKernighanSearch(int[] seq) {
        // Mark all node.
        Stack<Integer> marked = new Stack<Integer>();
        for (int c = 0; c < nCities; c++) {
            marked.push(seq[c]);
        }

        while (!marked.isEmpty()) {
            clearLKOrdering();
            int base = marked.pop();

            boolean foundSequence = searchFlipSequence(base);

            if (!foundSequence) continue;

            //System.out.println("Flip sequence: " + flipSequence);

            marked.push(base);
            /*while (!flipSequence.isEmpty()) {
                TupleInt flip = flipSequence.pop();
                tour.flip(flip.u, flip.v);
            }*/

            printTourCost();
        }
    }

    private boolean searchFlipSequence(int base) {
        // Init stack.
        searchStack.push(new SearchLevel(tour, costs,1, base, 0.0));
        improvingSequenceFound = false;

        // Search.
        while (!searchStack.isEmpty()) {
            searchStep();
        }

        return improvingSequenceFound;
    }

    private LKOrdering getOrCreateLkOrdering(int base) {
        if (allLkOrderings[base] == null) {
            allLkOrderings[base] = new LKOrdering(base, tour, costs, MAX_LK_ORDERING);
        }
        return allLkOrderings[base];
    }

    private void clearLKOrdering() {
        for (int c = 0; c < nCities; c++) {
            allLkOrderings[c] = null;
        }
    }

    private void searchStep() {
        final SearchLevel currentSearchLevel = searchStack.top();

        if (improvingSequenceFound) {
            searchStack.pop(); // TODO
            //flipSequence.push(searchStack.pop().getLastFlip());
            return;
        }

        final int base = currentSearchLevel.base;
        final double delta = currentSearchLevel.delta;
        final int level = currentSearchLevel.level;

        // Get current LK Ordering or create a fresh one if it doesn't exist yet.
        LKOrdering lkOrdering = currentSearchLevel.getLkOrdering();

        if (currentSearchLevel.hasLastFlip()) {
            TupleInt lastFlip = currentSearchLevel.getLastFlip();
            tour.flip(lastFlip.v, lastFlip.u); // unflip
        }

        // If we've explored everything in the ordering, stop.
        if (!lkOrdering.hasNextNeighbor(level)) {
            searchStack.pop();
            return;
        }

        final int a = lkOrdering.getNextNeighbor();
        final boolean isMakMortonMove = lkOrdering.isMakMortonMove();

        if (isMakMortonMove) {

            final int nextBase = tour.next(base);
            final int nextA = tour.next(a);

            final double old1 = costs.get(base, nextBase);
            final double old2 = costs.get(a, nextA);
            final double new1 = costs.get(base, a);
            final double new2 = costs.get(nextBase, nextA);
            final double g = old1 + old2 - new1 - new2;

            /*double expectedG = checkMove(nextA, base);
            if (Math.abs(expectedG - g) > 1) {
                System.out.println(String.format("g = %s, real = %s", g, expectedG));
            }*/

            currentSearchLevel.setLastFlip(nextA, base);
            tour.flip(nextA, base);

            if (delta + g > EPSILON) {
                //System.out.println("Solution found at level: " + level);
                improvingSequenceFound = true;
                return;
            }

            searchStack.push(new SearchLevel(tour, costs,level + 1, nextA, delta + g));
            return;
        }

        final int nextBase = tour.next(base);
        final int prevA = tour.prev(a);

        final double old1 = costs.get(base, nextBase);
        final double old2 = costs.get(prevA, a);
        final double new1 = costs.get(base, prevA);
        final double new2 = costs.get(nextBase, a);
        final double g = old1 + old2 - new1 - new2;

        /*double expectedG = checkMove(tour.next(base), tour.prev(a));
        if (Math.abs(expectedG - g) > 1) {
            System.out.println(String.format("g = %s, real = %s", g, expectedG));
        }*/

        currentSearchLevel.setLastFlip(nextBase, prevA);
        tour.flip(nextBase, prevA);

        if (delta + g > EPSILON) {
            //System.out.println("Solution found at level: " + level);
            improvingSequenceFound = true;
            return;
        }

        searchStack.push(new SearchLevel(tour, costs,level + 1, base, delta + g));
    }

    private double checkMove(int a, int b) {
        double cost = getTourCost();
        tour.flip(a, b);
        double cost2 = getTourCost();
        tour.flip(b, a);
        return cost - cost2;
    }
}
