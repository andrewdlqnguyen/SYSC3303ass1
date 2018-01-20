// @author Andrew Nguyen
// @ 100893165

import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Arrays;


public class Server {
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket sendPacket, receivePacket;
	private int port = 69;
	private boolean currentStatus;

	
	public Server() {
		
		// Creating the DatagramSocket send/receive
		// Send UDP Datagram packets.
		// Create a DatagramSocket that binds it to port 69
		// Thus the socket received is from port 23.
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(port); //port 69
		}
		catch(SocketException se) {
			se.printStackTrace();
			currentStatus = false; // Exit out of loop and close all socket.
			System.exit(1);
		}
	
	}
	
	// Server will receive datagram from Host and extract it
	public void receiveAndEcho() {
		//byte[] result = new byte[filenameByte.length + modeByte.length + 4];
		byte data[] = new byte[100];
	    receivePacket = new DatagramPacket(data, data.length);
	    System.out.println("Server: Waiting for packet from Host.\n");
		
	    try {
			receiveSocket.receive(receivePacket);
		}
		catch(IOException e) {
			System.out.print("IO Exception: likely:");
	        System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			currentStatus = false;
			// receiveSocket.close();
			System.exit(1);
		}
		
		System.out.println("***Server: Packet received from Host***");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.print("Containing: ");
		
		// Form a String from the byte Array. Print String + byte
		String received = new String(data, 0, receivePacket.getLength());
		System.out.println(received);
		System.out.println(receivePacket.getData() + "\n");
		
		// Slow things down (wait 5 seconds)
	    try {
	    	Thread.sleep(69);
	    } 
	    catch (InterruptedException e ) {
	        e.printStackTrace();
	        currentStatus = false;
	        System.exit(1);
	    }
	    
	    // Sending Packet back to Host to Client
	    byte serverResponse[] = Parse(receivePacket.getData());
	    sendPacket = new DatagramPacket(serverResponse, serverResponse.length,
                receivePacket.getAddress(), receivePacket.getPort());

	    System.out.println("***Server: Sending packet to Host***");
	    System.out.println("To host: " + sendPacket.getAddress());
	    System.out.println("Destination host port: " + sendPacket.getPort());;
	    System.out.print("Containing: ");
	    System.out.println(Arrays.toString(serverResponse));
	    //System.out.println(new String(sendPacket.getData(),0, sendPacket.getLength()));
	    System.out.println(sendPacket.getData() + "\n");
	    System.out.println("Server: Packet sent.");

	    // Send the datagram packet to the client via the send socket. 
	    try {
	    	sendSocket.send(sendPacket);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	currentStatus = false;
	    	System.exit(1);
	    }
	}
	
	// Method is for parsing purpose. Distinguish between read and write.
	private byte[] Parse(byte[] arr)
	{
		byte[] invalid = {0};
		if(arr[1] == 1) {
			System.out.println("***Packet Parsing***");
		    System.out.println("'Packet is a Read Request' \n");
			byte[] read = {0, 3, 0, 1};
			return read;
		}
		else if(arr[1] == 2) {
			System.out.println("***Packet Parsing***");
		    System.out.println("'Packet is a Write Request' \n");
			byte[] write = {0, 4, 0, 0};
			return write;
		}
		else {
			System.out.println("***Packet Parsing***");
		    System.out.println("'Invalid Request'");
			return invalid;
		}
	}
	
	// Loops forever till error
	public void foreverSystem() {
		currentStatus = true;
		while(currentStatus) {
			receiveAndEcho();
		}	
		
		// Closing up sockets
	    sendSocket.close();
	    receiveSocket.close();
	}
	
	public static void main(String args[]) {
		Server s = new Server();
		s.foreverSystem();
	}
}
