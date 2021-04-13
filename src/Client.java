import java.io.*;
import java.util.Scanner;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.lang.Math;

public class Client extends JFrame{
        public JTextField userText;
        private JTextArea chatWindow;
        private JPanel contentPane;

	private JPanel headerBanner;
	private JLabel bannerLabel;
	
	private JPanel inputHeader;
	public JButton groupChatButton;
	private JLabel userNameLabel;
	public JTextField userName;
	private JLabel recipientLabel;
	public JTextField recipientName;
	public JButton loginButton;
	
	private JPanel panelSouth;
	private JButton sendButton;

	private DatagramSocket clientSocket;
	private DatagramSocket loginSocket;
	private DatagramSocket groupSocket;
	private DatagramSocket receiveSocket;
	private DatagramSocket retrieveSocket;
	private String serverIP;
	private int i = 8;

        //Constructor
        public Client(String host){
                super("UCT Messenger Client");
		this.serverIP = host;
                //this.userText = new JTextField();
                
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(200,200,0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		headerBanner = new JPanel();
		contentPane.add(headerBanner, BorderLayout.NORTH);
		headerBanner.setLayout(new BorderLayout(0, 0));
		headerBanner.setBackground(new Color(200,200,0));

		bannerLabel = new JLabel("CHAT CLIENT");
		bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bannerLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		headerBanner.add(bannerLabel, BorderLayout.NORTH);

		inputHeader = new JPanel();
		headerBanner.add(inputHeader, BorderLayout.SOUTH);
		inputHeader.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		groupChatButton = new JButton("Join group chat");
                groupChatButton.setBackground(new Color(96,154,75));
                inputHeader.add(groupChatButton,BorderLayout.EAST);

		userNameLabel = new JLabel("username");
		inputHeader.add(userNameLabel);
		inputHeader.setBackground(new Color(135,135,135));

		userName = new JTextField();
		userName.setColumns(10);
		inputHeader.add(userName);

		recipientLabel = new JLabel("send to");
		inputHeader.add(recipientLabel);
		recipientName = new JTextField();
		recipientName.setColumns(10);
		inputHeader.add(recipientName);

		loginButton = new JButton("chat with");
		inputHeader.add(loginButton);
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 12));


                JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane,BorderLayout.CENTER);

		chatWindow = new JTextArea();
		chatWindow.setBackground(Color.BLACK);
		chatWindow.setForeground(Color.WHITE);
		chatWindow.setLineWrap(true);
		scrollPane.setViewportView(chatWindow);

		panelSouth = new JPanel();
		panelSouth.setBackground(new Color(135,135,135));
		FlowLayout fl_panelSouth = (FlowLayout) panelSouth.getLayout();
		fl_panelSouth.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panelSouth, BorderLayout.SOUTH);


		userText = new JTextField();
		panelSouth.add(userText);
		userText.setColumns(35);
		userText.setEditable(false);

		sendButton = new JButton("SEND");
		sendButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelSouth.add(sendButton);

		
		
		
		groupChatButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					recipientName.setText("GROUP");
					recipientName.setEditable(false);
					groupChatButton.setEnabled(false);
				}
			} 
		);

		userText.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand(),userName.getText(),recipientName.getText());	
                                        userText.setText("");
                                }
                        } 
                );
                setVisible(true);
        }
	//setup and run the server, this will be called after GUI is setup
        public void startRunning()throws IOException{
                
		clientSocket = new DatagramSocket();
		loginSocket = new DatagramSocket();
		receiveSocket = new DatagramSocket();
		//retrieveSocket = new DatagramSocket();

		new Thread(new messageHandler()).start();
        }
	//this method sends a message to connected clients
        public void sendMessage(String message, String sender, String recipient){
		try
		{
			int hashedText = hashNumber(message); //to be used for error detection
			String messageInfo = message + "\n" + sender + "\n" + recipient + "\n" + hashedText;
			byte [] dataToSend = messageInfo.getBytes();
			int dataLength = dataToSend.length;
			InetAddress IP = InetAddress.getLocalHost();
			int portNo = 9999;
		
			DatagramPacket sendPacket = new DatagramPacket(dataToSend,dataLength,IP,portNo);
		
			clientSocket.send(sendPacket);
			//showMessage(userName.getText() + " : " + message);
		}catch(IOException e){
			chatWindow.append("There was a problem sending the message");
		}
        }
	public void login(String name, String recip){
		String localPort = receiveSocket.getLocalPort() + "";	
		//String rtrvPort = retrieveSocket.getLocalPort() + "";		//this is the port we will be sending retrieve messages to
		System.out.println("Local Port/: " + localPort);
		String loginStr = name + "\n" + recip + "\n" + localPort;			//userName + port number
		try{
			byte [] loginData = loginStr.getBytes();
			int dataLength = loginData.length;
			InetAddress IP = InetAddress.getLocalHost();
			int portNo = 1776;

			DatagramPacket loginPacket = new DatagramPacket(loginData,dataLength,IP,portNo);
			loginSocket.send(loginPacket);
			DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");;
			//DateFormat forTime = new SimpleDateFormat("hh:mm:ss");

			Date localDate = new Date();
			showMessage("[" + dateTimeFormat.format(localDate) + "] " + "Welcome to your chat with " + recip + ", " + name);

			/*String eofFlag = "";
			while(!eofFlag.equals("\0")){
				byte [] dataToReceive = new byte[2048];
				DatagramPacket receivePacket = new DatagramPacket(dataToReceive,dataToReceive.length);
				receiveSocket.receive(receivePacket);
				eofFlag = new String(receivePacket.getData()).trim();
				showMessage(eofFlag);
			}*/
		}catch(IOException e){
			e.printStackTrace();
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
	
	public int hashNumber(String plainText) {
		int sum = 0;
		for (int i = 0; i < plainText.length(); i++) {
			sum += plainText.charAt(i) * Math.pow(31, i);
		}
		return sum;
	}
	
	public class messageHandler implements Runnable{
                private DatagramSocket messageSocket;
                public String userName;
                private String recipient;
                public InetAddress IP;
                int portNo;
                //constructor
                public messageHandler() throws IOException{
                        //super();
                        //this.messageSocket = socket;             //this is my socket that will be receiving messages
                        /*this.userName = name;
                        this.recipient = sendingTo;
                        this.IP = userIP;
                        this.portNo = userPortNo;
                        System.out.println(userName + " has just joined the chat");
                        showMessage(userName + " has just joined the chat");*/
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
                                        byte [] dataToReceive = new byte[2048];
                                        DatagramPacket receivePacket = new DatagramPacket(dataToReceive,dataToReceive.length);
                                        receiveSocket.receive(receivePacket);

					String [] packetData = new String(receivePacket.getData()).split("\n");
					if(packetData.length > 1){
						String message = packetData[0];
						String sender = packetData[1];
						//String myName = packetData[2].trim();
						int hashedText = Integer.parseInt(packetData[3].trim());
						
						if (hashedText == hashNumber(message)) {// error detection: verify that messages are correctly received
							
							if (sender.equals("Server")) { //loss detection:verify/confirm that all messages sent are received
								if (message == "s") {
									showMessage("Your message was received by the Server.");
								}
								else if (message.equals("r")) {
									showMessage("Your message was received by the Receiver.");
								}
							}
							else {
								sendMessage(sender,getUserName(),"Server");//tell Server that message is received
								
								DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");;
								DateFormat forTime = new SimpleDateFormat("hh:mm:ss");
	
								Date localDate = new Date();
								showMessage("[" + dateTimeFormat.format(localDate) + "] " + " [" + sender + "]: " + message);
								//showMessage(message);
							}
						}
						else {
							showMessage("Original message was lost.");
						}

					}
					else{
						showMessage(packetData[0]);
					}
                                }
                        }catch(Exception e){
                                e.printStackTrace();
                        }
                }
	}
                
}


