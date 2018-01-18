import java.net.DatagramSocket; // Sending and Receiving UDP datagrams
import java.io.IOException;
import java.net.DatagramPacket; // User Datagram Protocol datagram
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress; //Represents Internet Protocol address; Determines the address of a host, host name and IP
import java.util.Scanner;

public class Client {

	private DatagramSocket sendReceiveSocket;
	private DatagramPacket sendPacket, receivePacket;
	
	
	// Construct a Client which initially creates the send and receive DatagramSocket.
	public Client() {
		
		// Construct socket datagram and find local host to bind with.
		try {
			sendReceiveSocket = new DatagramSocket();
		}
		catch(SocketException se) { // Exit, no socket created.
			se.printStackTrace();
			System.exit(1);
		}
		
	}
	
	// Method gets a read request format as specified: 01name0netascii0
	public void sendAndReceive(String filename, String mode, byte readWrite, int port) {
		byte[] filenameByte = filename.getBytes();
		byte[] modeByte = mode.getBytes();
		byte[] result = new byte[filenameByte.length + modeByte.length + 4]; // This will give the total byte array space needed for the datagram
		result[0] = 0;
		result[1] = readWrite; // This input is distinguished whether it's a read(1) or write(2).
		System.arraycopy(filenameByte, 0, result, 2, filenameByte.length);
		result[filenameByte.length +2] = 0;
		System.arraycopy(modeByte, 0, result, filenameByte.length + 3, modeByte.length);
		result[filenameByte.length + modeByte.length + 3] = 0;
		
		
		try {
			sendPacket = new DatagramPacket(result, result.length, InetAddress.getLocalHost(), port);
		}
		catch(UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Client: Sending packet");
	    System.out.println("To host: " + sendPacket.getAddress());
	    System.out.println("Destination host port: " + sendPacket.getPort());
	    System.out.print("Containing: ");
	    System.out.println(new String(sendPacket.getData())); // alt; System.out.println(bytetoString(result));
	    System.out.println(result);
	    
	    // Send PacketDatagram to server via send/receive socket.
	    try {
	         sendReceiveSocket.send(sendPacket);
	      } catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }

	    System.out.println("Client: Packet sent.\n");
	    
	    byte data[] = new byte[filenameByte.length + modeByte.length + 4];
	    receivePacket = new DatagramPacket(data, data.length);
	}

	//  Method repeats the send/receive procedure 11 times as specified.
	public void repeatPrint(String filename, String mode, int port) {
		byte readWrite = 1;
		for(int n = 0; n < 1; n++) {
			sendAndReceive(filename, mode, readWrite, port);

		}
		System.out.println("Invalid Request");
	}
	
	// Changing byte format to String
//	public String bytetoString(byte[] result) {
//		String resultInString = "";
//		
//		for(int i = 0; i < result.length; i++) {
//			resultInString += (char)result[i];
//		}
//		return resultInString;
//	}
	
	public static void main(String args[]) {
		Client client1 = new Client();
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter the filename (eg. filename.txt): ");
		String filename = sc.next();
		System.out.print("Enter the mode (eg. 'netascii' or 'octet': ");
		String mode = sc.next();
		System.out.print("Enter the port address: ");
		int port = sc.nextInt();
		client1.repeatPrint(filename, mode, port);
		sc.close();
	}
}
