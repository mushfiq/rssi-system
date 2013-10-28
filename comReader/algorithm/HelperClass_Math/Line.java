package HelperClass_Math;
public class Line
{
    public Point p0, p1;

    public Line(Point p0_, Point p1_)
    {
        p0=p0_;
        p1=p1_;
    }

    // liefert den normierten Richtungsvektor der Geraden
    // in Richtung von p0 nach p1
    public Point getVector()
    {
        return p1.sub(p0).normalize();
    }

    // liefert den Abstand des Punktes p zur Geraden
    public double distanceOf(Point p)
    {
        return Math.abs(p.sub(p0).cross(getVector()));
    }

    public String toString()
    {
        return p0+" "+p1;
    }

}    // end class Line