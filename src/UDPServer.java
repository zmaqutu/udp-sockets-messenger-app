import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class UDPServer {

	public static void main(String [] args) throws Exception{
		//socket to accept data at port 9999
		DatagramSocket ds = new DatagramSocket(9999);

		
		byte [] dataReceived = new byte[1024];
		//To use a socket we use DatagramSocket 
		//to send / recieve the data we use DatagramPackets
		DatagramPacket dp = new DatagramPacket(dataReceived,dataReceived.length);
		ds.receive(dp);
		String str = new String(dp.getData());

		int num = Integer.parseInt(str.trim());
		int square = num*num;

		// now send the data the square back use similar code to client side
		byte [] dataToSendBack = (square + "").getBytes();
                //dataLength
                int dataLength = dataToSendBack.length;
                //ipAddress
                InetAddress ia = InetAddress.getLocalHost();
                //portNumber
                DatagramPacket dp1 = new DatagramPacket(dataToSendBack,dataLength,ia,dp.getPort());
                ds.send(dp1);
	}
}
