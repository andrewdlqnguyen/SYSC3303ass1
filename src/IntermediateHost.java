// @author Andrew Nguyen
// @ 100893165

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class IntermediateHost {

	private DatagramSocket sendReceiveSocket, receiveSocket, sendSocket; // TESTING sendSocket.
	private DatagramPacket receivePacket, sendPacket;
	private int port = 23;
	private int tempPort; //Retrace back to client after Server
	private InetAddress tempAddress; //Retrace back to client after Server
	private boolean currentStatus; //Check whether the loop still goes on forever
	
	public IntermediateHost() {
		
		// Receiving Packet from client at port 23
		try {
			sendReceiveSocket = new DatagramSocket(); // Acts as a Client to Server.
			receiveSocket = new DatagramSocket(port); // packet at port 23 (Client)
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
	    System.out.println("Host: Waiting for packet from Client.\n");
	    
	    try {
	    	receiveSocket.receive(receivePacket); // Info from Client has been received
		}
		catch(IOException e) {
			System.out.println("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
//			receiveSocket.close();
//			sendReceiveSocket.close();
			currentStatus = false;
			System.exit(1);
		}
	    
	    tempPort = receivePacket.getPort();
	    tempAddress = receivePacket.getAddress();
	    System.out.println("***Host: Packet received from Client***");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.print("Containing: ");
		
		// Form a String from the byte array. Print String + byte
		System.out.println(new String(receivePacket.getData()));
		System.out.println(receivePacket.getData() + "\n");
		
		// Slow things down (wait 5 seconds)
	    try {
	    	Thread.sleep(23);
	    } 
	    catch (InterruptedException e ) {
	        e.printStackTrace();
	        currentStatus = false;
	        System.exit(1);
	    }
	    
	    // Now let's send these information to Server
		String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
		
		// Sending Host Packet to Server
		try {
			sendPacket = new DatagramPacket(received.getBytes(), receivePacket.getLength(), receivePacket.getAddress(), 69); // port 69 for Server
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
//			receiveSocket.close();
//			sendReceiveSocket.close();
			System.exit(1);
		}
		
		System.out.println("***Host: Sending packet to Server***");
	    System.out.println("To host: " + sendPacket.getAddress());
	    System.out.println("Destination host port: " + sendPacket.getPort());
	    System.out.print("Containing: ");
	    System.out.println(new String(sendPacket.getData(), 0 , sendPacket.getLength())); // alt; System.out.println(bytetoString(result));
	    System.out.println(sendPacket.getData() + "\n");
	    System.out.println("Host: Packet sent.");
	    System.out.println("Host: Waiting for packet from Server" + "\n");
	    
	    // Retrieved the Packet from Server
	    byte data1[] = new byte[4];
	    receivePacket = new DatagramPacket(data1, data1.length);
	    
	    try {
	        // Block until a datagram is received via sendReceiveSocket. 
	    	// Packet should be at port 69
	    	sendReceiveSocket.receive(receivePacket);
	    }
	    catch(IOException e) {
	    	e.printStackTrace();
	        System.exit(1);
	    }
	    // Process the received datagram.
	    // This will occur after the packet is sent to host>server and back.
	    System.out.println("***Host: Packet received from Server***");
	    System.out.println("From host: " + receivePacket.getAddress());
	    System.out.println("Host port: " + receivePacket.getPort());
	    System.out.print("Containing: ");
	    System.out.println(Arrays.toString(receivePacket.getData()));
	    // Form a String from the byte array.  
	    System.out.println(new String(receivePacket.getData()));
	    System.out.println("");
	    
	    // Sending Packet back to Client
	    sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), tempAddress, tempPort);
	    
	    System.out.println("***Host: Sending packet to Client***");
	    System.out.println("To host: " + sendPacket.getAddress());
	    System.out.println("Destination host port: " + sendPacket.getPort());;
	    System.out.print("Containing: ");
	    System.out.println(Arrays.toString(receivePacket.getData()));
	    System.out.println(sendPacket.getData() + "\n");
	    System.out.println("Host: Packet sent.");
	    
	 // Send the datagram packet to the client via the send socket. 
	    try {
	    	sendSocket.send(sendPacket);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	currentStatus = false;
	    	System.exit(1);
	    }
	}
	
	public void foreverSystem() {
		currentStatus = true;
		while(currentStatus) {
			clientToHostToServer();
		}
		receiveSocket.close();
		sendReceiveSocket.close();
	}
	
	public static void main(String args[]) {
		IntermediateHost h = new IntermediateHost();
		h.foreverSystem();
	}
}
