import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

public class ImageSelect {

	public static void main(String[] args) {
	
		//Dialog to pick the folder to watermark (does whole folder)
		JFileChooser chooser = new JFileChooser();
	    chooser.setDialogTitle("Choose Directory to Watermark");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
	    //If use has chosen, then use this folder to watermark
	    File folder = null;
	    if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
	         folder = chooser.getSelectedFile();
	    }
	    
	    //Choose where to save watermarked photos to
	    chooser.setDialogTitle("Choose Directory to Save Photos to");
	   
	    File saveFolder = null;
	    if(chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
	         saveFolder = chooser.getSelectedFile();
	    }

	    //Each file/picture in the folder
		for (File fileEntry : folder.listFiles()) {
			ImageIcon icon = new ImageIcon(fileEntry.getPath());
			//System.out.println(fileEntry.getName()); //DEBUG

			// create BufferedImage object of same width and height as of
			// original image
			BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
					BufferedImage.TYPE_INT_RGB);

			// create graphics object and add original image to it
			Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
			graphics.drawImage(icon.getImage(), 0, 0, null);

			// set font for the watermark text
			//.6 alpha(transparency)
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .6f));
			//text size is a 6th of the picture height in bold
			graphics.setFont(new Font("Arial", Font.BOLD, icon.getIconHeight()/6));
			//angle the text
			graphics.rotate(-Math.PI/12);
			
			// unicode character for (c) is \u00a9
			String watermark = "Steven Kirby";
			String watermark2 = "Photography \u00a9";
			
			// add the watermark text in place
			graphics.drawString(watermark, (int)(icon.getIconWidth()*0.05),(int) (icon.getIconHeight()*0.6));
			graphics.drawString(watermark2, (int)(icon.getIconWidth()*0.03),(int) (icon.getIconHeight()*0.8));
			//clear for next picture
			graphics.dispose();

			//write the file to 
			File newFile = new File(saveFolder+"/WM_" + fileEntry.getName());
			try {
				ImageIO.write(bufferedImage, "JPG", newFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			//System.out.println(newFile.getPath() + " created successfully!"); //DEBUG
		}
		
	}
}