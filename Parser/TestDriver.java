package Parser;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TestDriver {
	
	public static void main(String[] args) throws Exception{
		//We are not currently using the parsed picture yet
		Picture test = ImageParser.parsePic("pix.jpg");
		
		JPanel panel = new JPanel();
		ImageIcon progressIcon = new ImageIcon(test.getImage("test"));
	    ImageLabel label = new ImageLabel(progressIcon);
	    label.setLocation(29, 37);
	    panel.add(label);

	    JFrame frame = new JFrame();
	    frame.getContentPane().add(panel);
	    frame.pack();
	    frame.setVisible(true);
	    
	    //progressIcon.setImage(test.GetSubPixels(50, 50, 10).getImage("testing"));
	    
		PicGenerator gen = new PicGenerator(11);
		gen.addPicture(test);
		ColorMapper.getMap(new RGBColor(183,188,208)).getImage("test");
		gen.createNewPic(300, 300,progressIcon,panel);
		System.out.println("DONE");
	
	}
	
}
class ImageLabel extends JLabel {

	  public ImageLabel(String img) {
	    this(new ImageIcon(img));
	  }

	  public ImageLabel(ImageIcon icon) {
	    setIcon(icon);
	    // setMargin(new Insets(0,0,0,0));
	    setIconTextGap(0);
	    // setBorderPainted(false);
	    setBorder(null);
	    setText(null);
	    setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
	  }

	}
