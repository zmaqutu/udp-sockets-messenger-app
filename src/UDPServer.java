import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
i
public class UDPServer {

	public static void main(String [] args){
		//socket to accept data at port 9999
		DatagramSocket ds = new DatagramSocket(9999);

		
		byte [] dataReceived = new byte[1024];
		//To use a socket we use DatagramSocket 
		//to send / recieve the data we use DatagramPackets
		DatagramPacket dp = new DatagramPacket(dataRecieved,dataRecieved.length);
		ds.receive(dp);
		String str = new String(dp.getData);

		int num = Integer.parseInt(str);
		int square = num*num;

		// now send the data the square back use similar code to client side
	}
}
