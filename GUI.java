import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class GUI extends JFrame implements ActionListener
{
	
	JFrame frame = new JFrame();
	JButton exitButton = new JButton("Çıkış"); 
	JButton fileButton = new JButton("Dosya Seç");
	//JLabel label = new JLabel("Geçerli Dosyayı Seçiniz");
	JLabel label;  
	JPanel panel = new JPanel();
	
    String file_path;
    String output;
	
	GUI()
	{	
		panel.setBorder(BorderFactory.createEmptyBorder(100, 230, 100, 250));
		panel.setLayout(new GridLayout(0,1));
		//panel.add(label);
		panel.add(fileButton);
		panel.add(exitButton);
				
		
		
	    label=new JLabel();  
	    label.setBounds(90,50, 600,30);  
	    frame.add(label); 
	    frame.setSize(700,700);  
		
		
		frame.add(panel,BorderLayout.CENTER);
	//	frame.setSize(150, 200);
		frame.setLocation(550, 300);
		frame.setTitle("GUI");
		frame.pack();
		frame.setVisible(true);
	
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fileButton.setFocusable(false);
		fileButton.addActionListener(this);	

		exitButton.addActionListener(this);

		
		
		
	}
		

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == fileButton)
		{
			JFileChooser file_upload = new JFileChooser();
			int res = file_upload.showOpenDialog(null);
			
			if(res == JFileChooser.APPROVE_OPTION) {
				file_path = file_upload.getSelectedFile().getAbsolutePath();
				System.out.println(file_path);
				
				Project myProject = new Project();
				try {
					
					output = myProject.findUndefinedMerchant(file_path);
					label.setText(output);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
		
		if(e.getSource() == exitButton)
		{
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		
		GUI myGui = new GUI();
	}
	
