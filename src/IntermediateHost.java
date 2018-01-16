import java.net.DatagramSocket;
import java.net.SocketException;

public class IntermediateHost {

	private DatagramSocket sendReceiveSocket;
	
	public IntermediateHost() {
		
		try {
			sendReceiveSocket = new DatagramSocket();
		}
		catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
}
