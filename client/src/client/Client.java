package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

public class Client extends JFrame {

	Socket clientsocket = null;
	DataInputStream in = null;
	DataOutputStream out = null;
	JTextArea inputText;
	String SerAddress = "172.20.178.190";
	
	
	
	
	
	int SendPort = 8888;
	JTextField NickName;
	JTextArea textShow;
	JButton button, setbutton;
	ArrayList<String> host_ip_list;
	
	
	
	
	
	
	
	public Client() { // ���캯��������һ�����ֲ���ʼ��
		init();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(480, 160, 340, 460);
		setTitle("123chat");
		setResizable(false);

	}
   
	void init() { // ��ʼ�����������ò��ֲ������ü�����

		inputText = new JTextArea(4, 29);

		button = new JButton("Send ");
		JLabel label = new JLabel("Name");
		setbutton = new JButton(" Login   ");
		textShow = new JTextArea(15, 29);
		textShow.setEditable(false);
		NickName = new JTextField(10);
		inputText.setBackground(Color.white);
		setLayout(new FlowLayout());
		getContentPane().setBackground(Color.yellow);
		add(new JScrollPane(textShow));
		textShow.setBackground(Color.white);
		setbutton.setBackground(Color.red);
		button.setBackground(Color.red);
		NickName.setBackground(Color.white);
		label.setForeground(Color.red);
		add(label);
		add(NickName);
		add(setbutton);
		add(new JScrollPane(inputText));
		add(button);
		setbutton.addActionListener(new ActionListener() {   //��Ӽ�����
			public void actionPerformed(ActionEvent e) {

				Thread readData;
				Read read = null;
				try {
					clientsocket = new Socket();
					read = new Read();
					readData = new Thread(read);
					if (clientsocket.isConnected()) {

					} else {
						InetAddress address = InetAddress.getByName(SerAddress);
						InetSocketAddress socketAddress = new InetSocketAddress(
								address, SendPort);
						clientsocket.connect(socketAddress);
						textShow.append(new java.text.SimpleDateFormat(
								"yy-MM-dd HH:mm:ss").format(new Date())
								+ "\n����������ӳɹ�\n�ѵ�¼������\n");
						in = new DataInputStream(clientsocket.getInputStream());
						out = new DataOutputStream(clientsocket
								.getOutputStream());
						read.setDataInputStream(in);
						readData.start();
					}
				} catch (Exception e1) {
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ "\n����������ʧ��\n");
				}

			}
		});

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Send send = new Send();
				Thread sendData = new Thread(send);
				send.setDataOutputStream(out);
				sendData.start();

			}

		});
		addWindowListener(new WindowAdapter() {    //��Ӧ�رհ�ť�Ĺ���
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane
						.showConfirmDialog(null, "Would you want to leave?",
								"Hi", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		
		
	} // init����

	
	class Read implements Runnable {   //��ȡ���������߳�
		DataInputStream in;

		public void setDataInputStream(DataInputStream in) {
			this.in = in;
		}

		public void run() {
			String result;
			while (true) {
				try {
					result = in.readUTF();
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ "\n"
							+ result);
				} catch (IOException e) {
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ "\n��������Ͽ�����\n");
					break;
				}
			}
		}
	}

	class Send implements Runnable {    // ������Ϣ��������߳� 
		DataOutputStream out;

		public void setDataOutputStream(DataOutputStream out) {
			this.out = out;
		}

		public void run() {
			String message = null;
			message = NickName.getText() + ":" + inputText.getText() + "\n";
			try {
				out.writeUTF(message);
				inputText.setText("");
			} catch (Exception e2) {
				textShow.append("fail to find server\n");
			}

		}
	}

	public static void main(String args[]) {
		Client client = new Client();
	}
}
