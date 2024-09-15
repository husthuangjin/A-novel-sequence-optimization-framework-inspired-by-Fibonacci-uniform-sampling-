package tour;

public interface Tour {

    void flip(int a, int b);

    void flip(int prev, int a, int b, int next);

    int next(int index);

    int prev(int index);

    int[] toArray();
}
