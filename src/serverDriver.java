import java.io.*;
import javax.swing.JFrame;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class serverDriver {

	public static void main(String [] args) throws Exception{
		//socket to accept data at port 9999
		//DatagramSocket serverSocket = new DatagramSocket(9999);
		Scanner scan = new Scanner(System.in);	

		Server udpServer = new Server();
		udpServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		udpServer.startRunning();
	}
}
