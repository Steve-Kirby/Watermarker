import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class ImageSelect {
		
	static File watermarkImage = null;
	static File imageToWatermark = null;
	static File save = null;
	static ImageIcon watermarkIcon = null;
	
	public static void main(String[] args) {
	
		final JFrame frame = new JFrame("Photo Watermarker");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton folderButton = new JButton("<html><center>"+"Watermark an"+"<br>"+"entire folder"+"</center></html>");
				folderButton.setPreferredSize(new Dimension(200,100));
				folderButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						FolderWatermark(frame);
					}
				
				});
				
		JButton singleButton = new JButton("<html><center>"+"Watermark a"+"<br>"+"singular image"+"</center></html>");
				singleButton.setPreferredSize(new Dimension(200,100));
				singleButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						SingleWatermark(frame);
					}
				
				});
		
				
		JButton editWatermarkButton = new JButton("<html><center>"+"Choose image to"+"<br>"+"use as Watermark"+"</center></html>");
				editWatermarkButton.setPreferredSize(new Dimension(200,100));
				editWatermarkButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						
						EditWatermark(frame);
					}
				
				});
				
		frame.getContentPane().add(editWatermarkButton,BorderLayout.NORTH);
		frame.getContentPane().add(folderButton, BorderLayout.CENTER);
		frame.getContentPane().add(singleButton, BorderLayout.SOUTH);
		
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	static void EditWatermark(JFrame frame) {
		
		JFileChooser chooser = FileSelect("Choose Image to use as Watermark", JFileChooser.FILES_ONLY);
	    
	    if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
	         watermarkImage = chooser.getSelectedFile();
	         watermarkIcon = new ImageIcon(watermarkImage.getPath()); 
	    } else {
	    	JOptionPane.showMessageDialog(frame, "Please pick an image to use as a watermark!");
	    }
	    
	}
	
	static void SingleWatermark(JFrame frame) {
		
		if (checkForWatermark(frame)) {
			JFileChooser chooser = FileSelect("Choose Image to Watermark",JFileChooser.FILES_ONLY);
		    
		    if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
		        SaveToFolder(frame);   
		        SaveWatermarkedImage(chooser.getSelectedFile());
		    } else {
		    	JOptionPane.showMessageDialog(frame, "Please pick an image you would like to watermark!");
		    }
		}
	  
	}
	
	static void FolderWatermark(JFrame frame) {
		
		if (checkForWatermark(frame)) {
			JFileChooser chooser = FileSelect("Choose Folder to Watermark", JFileChooser.DIRECTORIES_ONLY);
		    
		    File folder = null;
		    if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
		         
		    	folder = chooser.getSelectedFile();
		    }
		    
		    SaveToFolder(frame);
		    
		    
		    if(save != null) {
		    	for (File fileEntry : folder.listFiles()) {
		    		SaveWatermarkedImage(fileEntry);
		    	}
		    }
		    
		}
	}
	
	static boolean checkForWatermark(JFrame frame) {
		
		if(watermarkIcon == null) {
			JOptionPane.showMessageDialog(frame, "Please pick an image to use as a watermark!");
			return false;
		}
		return true;
		
	}
	
	
	static void SaveWatermarkedImage(File fileEntry) {
		
		
			ImageIcon icon = new ImageIcon(fileEntry.getPath());
			
			BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
					BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
			graphics.drawImage(icon.getImage(), 0, 0, null);
			
			
			graphics.drawImage(watermarkIcon.getImage(),0,0,bufferedImage.getHeight(), bufferedImage.getWidth(), null);
			graphics.dispose();
			
			File newFile = new File(save + "/WM_" + fileEntry.getName());
			 
			try {
				ImageIO.write(bufferedImage, "png", newFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
	   
	}
	
	static JFileChooser FileSelect(String title, int type) {
		
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(title);
		chooser.setFileSelectionMode(type);
		chooser.setAcceptAllFileFilterUsed(false);
		
		if (type == JFileChooser.FILES_ONLY) {
		chooser.setFileFilter(new FileFilter() {

			   public String getDescription() {
			       return "JPG Images (*.jpg), PNG Images (*.png)";
			   }

			   public boolean accept(File f) {
			       if (f.isDirectory()) {
			           return true;
			       } else {
			           String filename = f.getName().toLowerCase();
			           return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png");
			       }
			   }
			});
		}
		return chooser;
		
	}
	
	static void SaveToFolder(JFrame frame) {
 	    
		JFileChooser chooser = FileSelect("Choose Folder to Save to", JFileChooser.DIRECTORIES_ONLY);
		
	    
	    while(!(chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION)) {
	    	
	    	JOptionPane.showMessageDialog(frame, "Please pick a folder to save the image to!");
	    	chooser = FileSelect("Choose Folder to Save to", JFileChooser.DIRECTORIES_ONLY);
	 	    
	    }
	    
	    save = chooser.getSelectedFile(); 
	}
	
}