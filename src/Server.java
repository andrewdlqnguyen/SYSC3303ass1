import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class Server {
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket sendPacket, receivePacket;
	private int port = 69;
	private boolean currentStatus;

	
	public Server() {
		
		// Creating the DatagramSocket send/receive
		try {
			this.sendSocket = new DatagramSocket();
			this.receiveSocket = new DatagramSocket(port);
		}
		catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	
	}
	
	public void receiveAndEcho() {
		//byte[] result = new byte[filenameByte.length + modeByte.length + 4];
		byte data[] = new byte[100];
	    receivePacket = new DatagramPacket(data, data.length);
	    System.out.println("Server: Waiting for Packet.\n");
		
	    try {
			receiveSocket.receive(receivePacket);
		}
		catch(IOException e) {
			e.printStackTrace();
			receiveSocket.close();
			System.exit(1);
		}
		
		System.out.println("Server: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.print("Containing: ");
		String received = new String(receivePacket.getData());
		System.out.println(received);
		
		// Slow things down (wait 5 seconds)
	    try {
	    	Thread.sleep(5000);
	    } 
	    catch (InterruptedException e ) {
	        e.printStackTrace();
	        System.exit(1);
	    }
	    
	    // Sending Packet back to Host to Client
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
		//this.Parse(receivePacket.getData());
	}
	
	public void foreverSystem() {
		currentStatus = true;
		while(currentStatus) {
			receiveAndEcho();
		}		
	    sendSocket.close();
	    receiveSocket.close();
	}
	
	public static void main(String args[]) {
		Server s = new Server();
		s.foreverSystem();
	}
}
