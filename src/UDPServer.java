import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class UDPServer {

	public static void main(String [] args) throws Exception{
		//socket to accept data at port 9999
		DatagramSocket serverSocket = new DatagramSocket(9999);

		
		byte [] dataReceived = new byte[1024];
		//To use a socket we use DatagramSocket 
		//to send / recieve the data we use DatagramPackets
		DatagramPacket receivePacket = new DatagramPacket(dataReceived,dataReceived.length);
		serverSocket.receive(receivePacket);
		String str = new String(receivePacket.getData());
		
		System.out.println("IP Address is: " + receivePacket.getAddress());
		System.out.println("PortNumber is: " + receivePacket.getPort());


		int num = Integer.parseInt(str.trim());
		int square = num*num;

		// now send the data the square back use similar code to client side
		byte [] dataToSendBack = (square + "").getBytes();
                //dataLength
                int dataLength = dataToSendBack.length;
                //ipAddress
                InetAddress IP = receivePacket.getAddress();
                //portNumber
		int portNo = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(dataToSendBack,dataLength,IP,portNo);
                serverSocket.send(sendPacket);
	}
}
