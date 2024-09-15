package util;

public class TupleInt {
    public int u;
    public int v;

    public TupleInt(int u, int v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", u, v);
    }
}