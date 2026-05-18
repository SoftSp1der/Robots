package main.java.helper;

public class Pair<A, B> {
    public final A a;
    public final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public boolean equals(A aa, B bb) {
        return aa.equals(a) && bb.equals(b);
    }
    public boolean equals(Pair<A, B> p) {
        return p.a.equals(a) && p.b.equals(b);
    }
}
