package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.corba.se.pept.transport.ReaderThread;

public class Rxtxsample {
	/**
	 * @param args
	 */
	static SerialComm s;
        static COMPortDataReader rdt;

        public static void main(String[] args) {
      
          BufferedReader in = new BufferedReader(
                          new InputStreamReader( System.in ) );
	  int p = -1;
	  s = new SerialComm();
	  s.getPortList();
	  boolean isOpen = false;

	  System.out.println( "Enter the number and then press enter " );
	  try 
	  {
		  
	    String l = in.readLine();
	    p = Integer.parseInt(l);
	  } 
	  catch( IOException ex ) 
	  { 
		  System.out.println( ex.getMessage() ); 
	  } 
	  if (p>-1) isOpen = s.openSio(p, 115200);
	 if (isOpen) rdt = new COMPortDataReader ();
 	 System.out.println("Enter the String and finish by pressing enter");
	 while (isOpen) {
		 
	    try
	    {
	       String l = in.readLine();
	       s.myWrite(l);
	     } catch( IOException ex ) { System.out.println( ex.getMessage() ) ;}  ;
          }
	 // while isOpen
 	} 

        


}
