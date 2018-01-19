import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;
import java.net.UnknownHostException;

public class IntermediateHost {

	private DatagramSocket sendReceiveSocket, receiveSocket, sendSocket; // TESTING sendSocket.
	private DatagramPacket receivePacket, sendPacket;
	private int port = 23;
	private boolean currentStatus;
	
	public IntermediateHost() {
		
		try {
			sendReceiveSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(port);	
			sendSocket = new DatagramSocket();
		}
		catch(SocketException se) {
			se.printStackTrace();
			currentStatus = false;
			System.exit(1);
		}
	}
	
	public void clientToHostToServer() {
		byte data[] = new byte[100];
	    receivePacket = new DatagramPacket(data, data.length);
	    System.out.println("Host: Waiting for Packet.\n");
	    
	    try {
	    	receiveSocket.receive(receivePacket);
		}
		catch(IOException e) {
			System.out.println("IO Exception: likely:");
			e.printStackTrace();
			receiveSocket.close();
			sendReceiveSocket.close();
			currentStatus = false;
			System.exit(1);
		}
	    
	    System.out.println("Host: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.print("Containing: ");
		
		// Form a String from the byte array.
		System.out.println(new String(receivePacket.getData()));
		
		String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
		
		try {
			sendPacket = new DatagramPacket(received.getBytes(), receivePacket.getLength(), receivePacket.getAddress(), 69);
			sendReceiveSocket.send(sendPacket);
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
			currentStatus = false;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			currentStatus = false;
			receiveSocket.close();
			sendReceiveSocket.close();
			System.exit(1);
		}
		
		// TRYING OUT. check again, was high
		sendPacket = new DatagramPacket(data, receivePacket.getLength(),
                receivePacket.getAddress(), receivePacket.getPort());

	    System.out.println( "Server: Sending packet:");
	    System.out.println("To host: " + sendPacket.getAddress());
	    System.out.println("Destination host port: " + sendPacket.getPort());;
	    System.out.print("Containing: ");
	    System.out.println(new String(sendPacket.getData()));

	    // Send the datagram packet to the client via the send socket. 
	    try {
	    	sendSocket.send(sendPacket);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
	}
	
	public void foreverSystem() {
		currentStatus = true;
		while(currentStatus) {
			clientToHostToServer();
		}
	}
	
	public static void main(String args[]) {
		IntermediateHost h = new IntermediateHost();
		h.foreverSystem();
	}
}
