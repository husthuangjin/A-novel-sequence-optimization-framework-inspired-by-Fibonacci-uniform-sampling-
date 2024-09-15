package tour;

public final class ArrayTour implements Tour {

    private final int[] positions;
    private final int[] tour;

    public ArrayTour(int nCities) {
        this.positions = new int[nCities];
        this.tour = new int[nCities];
        for (int i = 0; i < nCities; i++) {
            positions[i] = i;
            tour[i] = i;
        }
    }

    public int next(int index) {
        int p = positions[index] + 1;
        if (p == tour.length) p = 0;
        return tour[p];
    }

    public int prev(int index) {
        int p = positions[index] - 1;
        if (p < 0) p = tour.length - 1;
        return tour[p];
    }

    public void flip(int a, int b) {
        int posA = positions[a];
        int posB = positions[b];

        if (posA == posB) {
            return;
        }

        if (posA > posB) {
            for (int nSwaps = (tour.length + 1 - posA + posB) / 2; nSwaps > 0; nSwaps--) {
                swap(posA++, posB--);
                posA = posA == tour.length ? 0 : posA;
                posB = posB < 0 ? tour.length - 1: posB;
            }
            return;
        }

        while (posA < posB) {
            swap(posA++, posB--);
        }
    }

    public void flip(int prev, int a, int b, int next) {
        flip(a, b);
    }

    private void swap(int l, int r) {
        int c1 = tour[l];
        int c2 = tour[r];
        int pos1 = positions[c1];
        int pos2 = positions[c2];
        tour[l] = c2;
        tour[r] = c1;
        positions[c1] = pos2;
        positions[c2] = pos1;
    }

    public int[] toArray() {
        int[] array = new int[tour.length];
        System.arraycopy(tour, 0, array,0, tour.length);
        return array;
    }
}
