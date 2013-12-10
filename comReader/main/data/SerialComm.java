package data;

import gnu.io.*;

import java.io.*;
import java.util.*;

public class SerialComm implements gnu.io.SerialPortEventListener{
	
	SerialPort serialPort; 
	InputStream inputStream;
	OutputStream outputStream;
	public int isOpen = 0;
	
	public Vector<String> getPortList() {
		Enumeration<CommPortIdentifier> portList;
		Vector<String> portVect = new Vector<String>();
		portList = CommPortIdentifier.getPortIdentifiers();

		CommPortIdentifier portId;
		while (portList.hasMoreElements()) {
			portId = portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				portVect.add(portId.getName());
			}
		}
		System.out.println(portVect.size() + " found Ports:");
		for (int i = 0; i < portVect.size(); i++) {
			System.out.println(i + "  : " + portVect.elementAt(i));
		}
		
		return portVect;
	} // get Port list
	
	public void closeSio() {
            inputStream = null;
            outputStream = null;
            serialPort.close();
        }

	public boolean openSio(int aPortListNumber, int aBaud) {
 		CommPortIdentifier portId = null;
		Enumeration portList;
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (aPortListNumber == 0) break;
			aPortListNumber--;
			} 
		System.out.println("Oeffnen von Port Nr "+aPortListNumber);
		if (portId == null) {System.out.println("Port nicht gefunden");return false;};
// System.out.println("pos1");
		try {
			serialPort = (SerialPort) portId.open("Serial_Test", 1000);
		} catch (PortInUseException e) {System.out.println("Port in Use: "+e.toString()); }
// tem.out.println("pos2");
		if (serialPort != null)
		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {System.out.println("GetInpStream: "+e.toString()); }
		if (inputStream != null)
		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {System.out.println("ToMAnyListeners: "+e.toString()); }
		if (serialPort != null) serialPort.notifyOnDataAvailable(true);
		if (serialPort != null) try {
			serialPort.setSerialPortParams(aBaud, SerialPort.DATABITS_8, 
						   SerialPort.STOPBITS_1, 
						   SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {System.out.println("Uns.Comm.Op.Ex.: "+e.toString()); }

		if (serialPort != null) try {
			outputStream = serialPort.getOutputStream();
		} catch (IOException e) {System.out.println("IOExcp: "+e.toString()); }

                return (inputStream != null);


	}

	@Override
	public void serialEvent(SerialPortEvent event) {

		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:


		break;
		} // switch
	}  // method serialEvent 
        
    public void myWrite(String s) {
                try {

                    int len = s.length();
                    System.out.print("Send "+len+" numbers: ");
                    for(int i = 0; i<len;i++) {
			int c = s.charAt(i);
                        System.out.printf("%02X ", c);
			outputStream.write(s.charAt(i));
                    }
                    System.out.println();
                    
                } catch (IOException e) {
                    System.err.println("IOException: "  + e.getMessage());        
                }  // catch
            }
 
// ------ must implement this method ...
	public int readSio() throws java.io.IOException {
		byte[] readBuffer = new byte[1];
		int c;
		int numBytes = 0;
//		try {
                if (inputStream != null)
		   if (inputStream.available() > 0) {
			numBytes = inputStream.read(readBuffer);
			} 
//		catch (IOException e) {}
		if (numBytes ==1) {
			c = (readBuffer[0] >= 0 ? readBuffer[0] : readBuffer[0] +256);
 			return c;
		} 
		else return -1;		
	}


}
