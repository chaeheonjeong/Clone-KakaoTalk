import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Color;

public class ImageFrame extends JFrame {
	private Image big_img;
	
	public ImageFrame(ImageIcon img) {
		Image ori_image = img.getImage();
		
		int height = img.getIconHeight();
		int width = img.getIconWidth();
		double ratio;
		
		setBounds(100, 100, 450, 300);
		if(height > 600 || width > 600) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = 600;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = 600;
				width = (int) (height * ratio);
			}
		}
		
		big_img = ori_image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon new_icon = new ImageIcon(big_img);
		setSize(600, 600);
		setVisible(true);
		setResizable(false);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBackground(new Color(255, 255, 255));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new_icon);
		getContentPane().add(lblNewLabel, BorderLayout.CENTER);
		
		
	}
	
	

}
