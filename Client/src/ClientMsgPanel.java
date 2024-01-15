import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientMsgPanel extends JPanel {
	public ClientMsgPanel(String username, ImageIcon profile, String msg, String nowDate) {
		
		setSize(380, 50);
		
		//setBackground(new Color(255,0,0,0));
		setOpaque(false);
		
		//int len = msg.length();
		setLayout(null);

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
		        graphics.setColor(Color.yellow);
		        graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint background
		        graphics.setColor(Color.yellow);
		        graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint border
		     }
		  };
		  
			JPanel BundlePanel = new JPanel();
			BundlePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			BundlePanel.setBounds(90, 10, 275, 40);
			BundlePanel.setBackground(new Color(255,0,0,0)); //투명
			add(BundlePanel);
			
			  JLabel lblTime = new JLabel(nowDate);
			  lblTime.setBounds(9, 36, 52, 14);
			  BundlePanel.add(lblTime);
			  lblTime.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
		  
		  JLabel lblText = new JLabel(msg, SwingConstants.RIGHT);
		  lblText.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		  lblText.setBounds(23, 10, 100, 40);
		  lblText.setHorizontalAlignment(SwingConstants.RIGHT);
		  chatColor.add(lblText);
		
		  //lblText.setHorizontalAlignment(SwingConstants.RIGHT);
		  chatColor.setBounds(10, 10, 129, 40);
		  chatColor.setOpaque(false);
		  BundlePanel.add(chatColor);
		  
	}
}
