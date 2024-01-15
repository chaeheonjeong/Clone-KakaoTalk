import java.awt.Color;
import java.awt.EventQueue;
import java.awt.TextField;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class ClientLogin extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnLogin;
	private JTextField InputUserName;
	private ImageIcon kakao = new ImageIcon("./icon/kakao-talk.png");
	
	// Lunch the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLogin frame = new ClientLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 382, 550);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBackground(new Color(254, 240, 27));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnLogin = new JButton("로그인");
		btnLogin.setForeground(new Color(255, 255, 255));
		btnLogin.setBorderPainted(false);
		btnLogin.setBackground(new Color(58, 29, 29));
		btnLogin.setBounds(70, 317, 231, 42);
		contentPane.add(btnLogin);
		
		InputUserName = new JTextField();
		InputUserName.setBorder(null);
		InputUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		InputUserName.setText(" ");
		InputUserName.setHorizontalAlignment(SwingConstants.CENTER);
		InputUserName.setBounds(70, 265, 231, 42);
		contentPane.add(InputUserName);
		
		JLabel logo = new JLabel(kakao);
		logo.setBounds(115, 103, 130, 130);
		contentPane.add(logo);
		
		Myaction action = new Myaction();
		InputUserName.addActionListener(action);
		btnLogin.addActionListener(action);
	}
	
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = InputUserName.getText().trim();
			String ip_addr = "127.0.0.1";
			String port_no = "30000";
			ImageIcon profile = new ImageIcon("./icon/basic_profile.png");
			ClientMain view = new ClientMain(username, ip_addr, port_no, profile);
			setVisible(false);
		}
	}
}