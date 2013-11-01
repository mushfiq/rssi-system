package tests;


public class PerformanceTestFloatVsDouble {

	 public static void doubleTest(int loop)
	    {
	        System.out.println("double: ");
	        for (int i = 0; i < loop; i++)
	        {
	            double a = 1000, b = 45, c = 12000, d = 2, e = 7, f = 1024;
	            a = Math.sin(a);
	            b = Math.asin(b);
	            c = Math.sqrt(c);
	            d = d + d - d + d;
	            e = e * e + e * e;
	            f = f / f / f / f / f;
	        }
	    }

	    public static void floatTest(int loop)
	    {
	        System.out.println("float: ");
	        for (int i = 0; i < loop; i++)
	        {
	            float a = 1000, b = 45, c = 12000, d = 2, e = 7, f = 1024;
	            a = (float) Math.sin(a);
	            b = (float) Math.asin(b);
	            c = (float) Math.sqrt(c);
	            d = d + d - d + d;
	            e = e * e + e * e;
	            f = f / f / f / f / f;
	        }
	    }

	    public static void main(String[] args)
	    {
	        long currentTime = System.currentTimeMillis();
	        doubleTest(15 * 1000000);
	        System.out.println("milliseconds: " + (System.currentTimeMillis() - currentTime));

	        currentTime = System.currentTimeMillis();
	        floatTest(15 * 1000000);
	        System.out.println("milliseconds: " + (System.currentTimeMillis() - currentTime));

	    }

}
