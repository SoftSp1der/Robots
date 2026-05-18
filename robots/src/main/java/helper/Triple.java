package main.java.helper;

public class Triple<A, B, C> {
    public final A a;
    public final B b;
    public final C c;

    public Triple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean equals(A aa, B bb, C cc) {
        return aa.equals(a) && bb.equals(b) && cc.equals(c);
    }
    public boolean equals(Triple<A, B, C> t) {
        return t.a.equals(a) && t.b.equals(b) && t.c.equals(c);
    }
}
