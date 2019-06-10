package server;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;

public class Server extends JFrame {

	JTextArea textShow;
	JButton start;
	Vector socketsss = new Vector();//�����õ��˱䳤�������飬�����洢���Կͻ��˵�socket����
	ServerSocket server = null;
	Socket clients;

	Server() { // �������Ĺ��캯�������ҳ�ʼ��
		init();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(450, 150, 340, 455);
		setTitle("123chat server");
		setResizable(false);
	}

	void init() { // ���ò��ֺ��¼�������
		setLayout(new FlowLayout());
		getContentPane().setBackground(Color.yellow);
		textShow = new JTextArea(21, 29);
		textShow.setBackground(Color.white );
		start = new JButton(" Begin the server ");
		start.setBackground(Color.red);
		add(start);
		add(new JScrollPane(textShow));
		textShow.setEditable(false);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {   //�����������������߳�
				Listen listen = new Listen();
				Thread go = new Thread(listen);
				go.start();
			}
		});

		addWindowListener(new WindowAdapter() { // ��Ӧ�رհ�ť����
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane
						.showConfirmDialog(null, "leave?",
								" Hi", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION)
					System.exit(0);

			}
		});

	} // init()����

	class ServerThread extends Thread { // ��������Ϣ������߳�
		Socket socket;
		DataOutputStream out = null;
		DataInputStream in = null;
		String s = null;
		Vector sockets = new Vector();
		int j = 0;

		ServerThread(Socket t, Vector socketss) {
			socket = t;
			sockets = socketss;
			try { 
				in = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
			}
		}

		public void run() {
			while (true) {
				try {
					String r = in.readUTF();// ����״̬�����Ƕ�ȡ����Ϣ
					for (int j = 0; j < sockets.size(); j++) {
						out = new DataOutputStream(
								((Socket) sockets.get(j)).getOutputStream()); // ����ÿ�������ڵ�socket���󶼽��������
						out.writeUTF(r);
					}
				} catch (IOException e) {
					textShow.append("someone leave\n");
					return;
				}
			}
		}
	}

	class Listen implements Runnable { // �����������߳�
		ServerSocket server;
		Socket clients;

		public void run() {
			while (true) {
				try {
					server = new ServerSocket(8888);
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ "�������ѿ���\n");
				} catch (IOException e1) {
					textShow.append("���ڼ���\n"); // ServerSocket�������ظ�����
				}
				try {
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ " �ȴ��û�����......\n");

					clients = server.accept();
					socketsss.add(clients);
					ServerThread handlers = new ServerThread(clients, socketsss);
					handlers.start(); // Ϊÿ���û�������������Ϣ�����߳�
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ "���û�����,�û��ĵ�ַ:" + clients.getInetAddress() + "\n");
				} catch (IOException e1) {
					textShow.append(new java.text.SimpleDateFormat(
							"yy-MM-dd HH:mm:ss").format(new Date())
							+ "���ڵȴ���������......\n");
				}
			}
		}
	}

	public static void main(String args[]) {
		Server server = new Server();
	}
}