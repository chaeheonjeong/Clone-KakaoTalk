import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class FriendMsgPanel extends JPanel {
	public FriendMsgPanel(String username, ImageIcon profile, String msg, String nowDate) {
		setLayout(null);
		setOpaque(false);
		//setBackground(new Color(255,0,0,0)); //투명
		
		Image img = profile.getImage();
		Image update = img.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
		ImageIcon ud_profile = new ImageIcon(update);
		
		JLabel lblProfile = new JLabel(ud_profile);
		lblProfile.setBounds(12, 10, 45, 45);
		add(lblProfile);
		
		JLabel lblName = new JLabel(username);
		lblName.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lblName.setBounds(70, 8, 83, 22);
		add(lblName);
		
		JPanel BundlePanel = new JPanel();
		BundlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		BundlePanel.setBounds(65, 30, 275, 40);
		BundlePanel.setBackground(new Color(255,0,0,0)); //투명
		add(BundlePanel);
		
		
		JPanel chatColor = new JPanel() {
		     @Override
		     protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        Dimension arcs = new Dimension(15,15); //Border corners arcs {width,height}, change this to whatever you want
		        int width = getWidth();
		        int height = getHeight();
		        Graphics2D graphics = (Graphics2D) g;
		        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


		        //Draws the rounded panel with borders.
		        graphics.setColor(Color.white);
		        graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint background
		        graphics.setColor(Color.white);
		        graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint border
		     }
		  };
		  
		  chatColor.setBounds(65, 30, 275, 40);
		  chatColor.setOpaque(false);
		  BundlePanel.add(chatColor);
		  
		JLabel lblText = new JLabel(msg);
		lblText.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblText.setBounds(65, 30, 275, 40);
		chatColor.add(lblText);
		
		JLabel lblTime = new JLabel(nowDate);
		BundlePanel.add(lblTime);
		lblTime.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
	}
}
