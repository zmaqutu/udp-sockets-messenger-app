import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server extends JFrame{
	private JTextArea chatWindow;
	private JPanel contentPane;
        private JLabel bannerLabel;
	private JPanel panelSouth;
	public JLabel statusLabel; 
        public JButton startButton;
	private HashMap<String,clientHandler> connected = new HashMap<>();


	private DatagramSocket serverSocket;
	private DatagramSocket loginSocket;
	private InetAddress clientIP;
	private DatagramPacket receivePacket;

	//Constructor
	public Server()throws IOException{
		super("UCT Messenger");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		bannerLabel = new JLabel("CHAT SERVER by Zongo");
		bannerLabel.setForeground(new Color(0,128,0));
		bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bannerLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		contentPane.add(bannerLabel, BorderLayout.NORTH);

		//south panel that houses server run status and start button
		panelSouth = new JPanel();
		panelSouth.setBackground(new Color(152,251,152));
		contentPane.add(panelSouth,BorderLayout.SOUTH);


		//the start button
		startButton = new JButton("START");
		startButton.setOpaque(false);
		startButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
		startButton.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent event){
                                }
                        } 
                );
		//server run status
		statusLabel = new JLabel("Server offline");
		statusLabel.setBorder(new LineBorder(new Color(0,0,0),2));
		statusLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		statusLabel.setForeground(Color.WHITE);
		
		statusLabel.setBackground(new Color(220,20,60));
		statusLabel.setOpaque(true);
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

		GroupLayout gl_panel = new GroupLayout(panelSouth);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(statusLabel,GroupLayout.DEFAULT_SIZE,393,Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(startButton,GroupLayout.PREFERRED_SIZE,127,GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				);

		gl_panel.setVerticalGroup(
                                gl_panel.createParallelGroup(Alignment.TRAILING)
                                .addGroup(gl_panel.createSequentialGroup()
                                        .addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(statusLabel,Alignment.LEADING,GroupLayout.DEFAULT_SIZE,45,Short.MAX_VALUE)
                                        .addComponent(startButton,Alignment.LEADING,GroupLayout.PREFERRED_SIZE,50,GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap())
                                );

		panelSouth.setLayout(gl_panel);



		//text on the chat window
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		chatWindow = new JTextArea();
		chatWindow.setBackground(Color.BLACK);
		chatWindow.setForeground(Color.WHITE);
		chatWindow.setLineWrap(true);
		scrollPane.setViewportView(chatWindow);

		setVisible(true);
	}

	//setup and run the server, this will be called after GUI is setup
	public void startRunning()throws IOException{
		serverSocket = new DatagramSocket(9999);
		loginSocket = new DatagramSocket(1776);
		Scanner scan = new Scanner(System.in);
		while(true){
			byte [] loginData = new byte[1024];

			DatagramPacket loginPacket = new DatagramPacket(loginData,loginData.length);	
			loginSocket.receive(loginPacket);

			InetAddress ip = loginPacket.getAddress();
			//System.out.println("IP: " + ip);
			//System.out.println("Port: " + loginPacket.getPort());

			String [] chatInfo = new String(loginPacket.getData()).split(" ");
			String userName = chatInfo[0];
			String recipientName = chatInfo[1];

			clientHandler userProfile = new clientHandler(userName,recipientName,serverSocket,loginPacket.getAddress(),loginPacket.getPort());

			new Thread(userProfile).start();
			connected.put(userProfile.userName,userProfile);
			sendMessage(userProfile.getUserName(),userProfile.getRecipient());
		}
	}
	//this method sends a message to connected clients
	private void sendMessage(String message, String recipient){
		
		try{
			byte [] dataToSend = message.getBytes();
			int dataLength = dataToSend.length;
			
			if(connected.get(recipient) != null){
				InetAddress IP = connected.get(recipient).getIP();
				int portNo = 1996;
				DatagramPacket sendPacket = new DatagramPacket(dataToSend,dataLength,IP,portNo);
				serverSocket.send(sendPacket);
				//showMessage(message);
			}
		}catch(IOException e){
			chatWindow.append("Unable to send to client \n");
		}
	}
	//this method creates a new thread that appends a message to the gui
	public void showMessage(final String message){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						chatWindow.append(message + "\n");
					}
				}
		);
	}
	public class clientHandler implements Runnable{
        	private DatagramSocket clientSocket;
        	public String userName;
		private String recipient;
		public InetAddress IP;
		int portNo;
        	//constructor
        	public clientHandler(String name,String sendingTo, DatagramSocket socket,InetAddress userIP,int userPortNo) throws IOException{
                	//super();
                	this.clientSocket = socket;             //this is my socket that will be receiving messages
                	this.userName = name;
			this.recipient = sendingTo;
			this.IP = userIP;
			this.portNo = userPortNo;
			System.out.println(userName + " has just joined the chat");
			showMessage(userName + " has just joined the chat");
        	}
		//getters
		public String getUserName(){
			return this.userName;
		}
		public String getRecipient(){
			return this.recipient;
		}
		public InetAddress getIP(){
			return IP;
		}
		public int getPortNo(){
			return portNo;
		}
        	@Override
        	public void run(){
                	try{
                        	while(true){
                                	byte [] dataToReceive = new byte[1024];
                                	DatagramPacket receivePacket = new DatagramPacket(dataToReceive,dataToReceive.length);
                                	clientSocket.receive(receivePacket);
                                
					String str = new String(receivePacket.getData());

                                	System.out.println(userName + " : " + str);
                                	showMessage(userName +": " + str);
                        	}
                	}catch(Exception e){
                        	e.printStackTrace();
                	}
        	}
	}
	
	
}
