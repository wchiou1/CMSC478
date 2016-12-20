package Parser;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;



public class ImageParser {

public static final String IMG = "pix.jpg";

public static void main(String[] args) {
	Picture test = parsePic(IMG);
	System.out.println(test.x+","+test.y);
	System.out.println(test.pixels[0][0]);
   

}

public static Picture parsePic(String fileName){
	BufferedImage img;
	RGBColor[][] pixels = null;
    try {
        img = ImageIO.read(new File(fileName));
        pixels = new RGBColor[img.getWidth()][img.getHeight()];
        int[] rgb;
        
        for(int x = 0; x < img.getWidth(); x++){
        	for(int y=0; y < img.getHeight(); y++){
        		rgb = getPixelData(img, x, y);
        		pixels[x][y] = new RGBColor(rgb[0],rgb[1],rgb[2]);
        	}
        }
        


    } catch (IOException e) {
        e.printStackTrace();
    }
    return new Picture(pixels);
	    
}

private static int[] getPixelData(BufferedImage img, int x, int y) {
int argb = img.getRGB(x, y);

int rgb[] = new int[] {
    (argb >> 16) & 0xff, //red
    (argb >>  8) & 0xff, //green
    (argb      ) & 0xff  //blue
};
return rgb;
}


}