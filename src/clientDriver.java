import java.io.*;
import javax.swing.JFrame;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class clientDriver {

	public static void main(String[] args) throws Exception{

		System.out.println("Back to Java code");
		//socket to accept data at port 9999
                //DatagramSocket serverSocket = new DatagramSocket(9999);
                //Scanner scan = new Scanner(System.in);

                Client udpClient = new Client("localhost");
                udpClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                udpClient.startRunning();
	}
}
