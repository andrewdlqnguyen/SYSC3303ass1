import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class Server {
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket receivePacket;
	private byte[] data;
	
	public Server() {
		
		// Creating the DatagramSocket send/receive
		try {
			this.sendSocket = new DatagramSocket();
			this.receiveSocket = new DatagramSocket(69);
		}
		catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		
		// Creating the DatagramPacket
		data = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		try {
			System.out.println("Waiting...");
			receiveSocket.receive(receivePacket);
		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
	}
}
