package GeometryObjects;
public class Point
{
    // Ungenauigkeit der Koordinatendarstellung
    private static final double delta=1e-10;

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Copy-Konstruktor
    public Point(Point p) {
        this(p.x, p.y);
    }

    // ---- Operationen ----

    // Addition eines Punktes
    public Point add(Point p) {
        return new Point(x + p.x, y + p.y);
    }

    // Negation eines Punktes
    public Point neg() {
        return new Point(-x, -y);
    }

    // Subtraktion eines Punktes
    public Point sub(Point p) {
        return add(p.neg());
    }

    // Multiplikation eines Punktes mit einer Zahl
    public Point mul(double k) {
        return new Point(k * x, k * y);
    }

    // Verschieben eines Punktes
    public void offset(double x0, double y0) {
        x += x0;
        y += y0;
    }

    // Skalarprodukt
    public double dot(Point p) {
        return x * p.x + y * p.y;
    }

    // Kreuzprodukt
    public double cross(Point p) {
        return x * p.y - y * p.x;
    }

    // ---- Normen und Abstände ----

    // Betragsnorm
    public double norm1() {
        return Math.abs(x) + Math.abs(y);
    }

    // euklidische Norm
    public double norm2() {
        return Math.sqrt(x * x + y * y);
    }

    // Maximumnorm
    public double norm8() {
        return Math.max(Math.abs(x), Math.abs(y));
    }

    // Manhattan-Abstand
    public double mdist(Point p) {
        return sub(p).norm1();
    }

    // euklidischer Abstand
    public double dist(Point p) {
        return sub(p).norm2();
    }

    // Kantenlänge quadratischer Bounding-Box
    public double bdist(Point p) {
        return sub(p).norm8();
    }

    public Point normalize() {
        return mul(1 / norm2());
    }

    // ---- Hilfsfunktionen für konvexe Hülle ----

    public boolean isLower(Point p) {
        return y < p.y || y == p.y && x < p.x;
    }

    public boolean isFurther(Point p) {
        return norm1() > p.norm1();
    }

    public boolean isBetween(Point p0, Point p1) {
        return p0.mdist(p1) >= mdist(p0) + mdist(p1);
    }

    public boolean isLess(Point p) {
        double f = cross(p);
        return f > 0 || f == 0 && isFurther(p);
    }

    public double area2(Point p0, Point p1) {
        return sub(p0).cross(sub(p1));
    }

    public boolean isRightOf(Line g) {
        return area2(g.p0, g.p1) < 0;
    }

    public boolean isConvex(Point p0, Point p1) {
        double f = area2(p0, p1);
        return f < 0 || f == 0 && !isBetween(p0, p1);
    }

    // ---- allgemeine Methoden ----

    private boolean isEqual(double a, double b) {
        return Math.abs(a - b) < delta;
    }

    public boolean equals(Point p) {
        return isEqual(x, p.x) && isEqual(y, p.y);
    }

    public String toString() {
        return x + " " + y;
    }

    public void makeRelTo(Point p1) {
      this.x -= p1.x;
      this.y -= p1.y;
            
    }

    public Point reversed() {
    	return new Point(-this.x, -this.y);
    }

    

}    // end class Point