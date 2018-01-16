import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;

public class Client {

	private DatagramSocket sendReceiveSocket;
	private DatagramPacket sendPacket;
	
	public Client() {
		
		try {
			sendReceiveSocket = new DatagramSocket();
		}
		catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public void repeatPrint() {
		for(int n = 0; n < 10; n++) {
			
		}
	}
	
	public void readRequest(String filename) {
		byte[] firstTwoByte = {0,1};
		byte[] filenameByte = filename.getBytes();
		byte[] singleByte = {0};
		byte[] modeByte = "netascii".getBytes();
		byte[] result = new byte[filenameByte.length + modeByte.length + 4];
		System.arraycopy(firstTwoByte, 0, result, 0, firstTwoByte.length);
		System.arraycopy(filenameByte, 0, result, firstTwoByte.length, filenameByte.length);
		System.arraycopy(singleByte, 0, result, firstTwoByte.length, singleByte.length);
		System.arraycopy(modeByte, 0, result, singleByte.length, modeByte.length);
		
	}
	//read request format: 01name0netascii0
	//write request format: 02name0netascii0
	//sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), 5000);
	
	public static void main(String []args) {
		
		String s = ("Anyone there?");
		byte[] msg = s.getBytes();

		
		Client client1 = new Client();
		client1.repeatPrint();
		
		IntermediateHost host1 = new IntermediateHost();
		Server server1 = new Server();

	}
}
