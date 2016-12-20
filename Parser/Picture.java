package Parser;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Picture{
	public int x,y;
	public RGBColor[][] pixels;
	public Picture(RGBColor[][] pixels){
		x=pixels.length;
		y=pixels[0].length;
		this.pixels=pixels;
	}
	private double getDistance(float x1, float y1, float z1, float x2, float y2, float z2)
 	{
	    float dx = x1 - x2;
	    float dy = y1 - y2;
	    float dz = z1 - z2;

	    // We should avoid Math.pow or Math.hypot due to perfomance reasons
	    return Math.sqrt(dx * dx + dy * dy + dz * dz);
 	}
	/**
	 * Gets the average color distance(rgb direct) or all existing pixels which can be compared
	 * @param other
	 * @return
	 */
	public double getDifference(Picture other){
		//Use distance formula on all RGBColors and add up the "distance"
		double total=0;
		int count=0;
		RGBColor[][] otherPixels = other.pixels;
		for(int i=0;i<pixels.length;i++){
			for(int j=0;j<pixels[0].length;j++){
				RGBColor ot = otherPixels[i][j];
				RGBColor tt = pixels[i][j];
				if(ot!=null&&tt!=null){
					total=+getDistance(ot.r,ot.g,ot.b,tt.r,tt.g,tt.b);
					count++;
				}
			}
		}
		return total/count;
	}
	
	/**
	 * Gets the CENTER of the sub picture and the size(MUST BE ODD)
	 * @param x
	 * @param y
	 * @param d
	 * @return
	 */
	public Picture GetSubPixels(int x, int y, int d){
		RGBColor [][] pdxd = new RGBColor[d][d];
		int h=d/2;
		for(int r=0; r<d; r++){
			for(int c=0; c<d; c++){
				if(x+c-h<0||x+c-h>=this.x||y+r-h<0||y+r-h>=this.y||this.pixels[x+c-h][y+r-h]==null){
					pdxd[c][r]=null;
				}
				else{
					pdxd[c][r]=new RGBColor(this.pixels[x+c-h][y+r-h].r,this.pixels[x+c-h][y+r-h].g,this.pixels[x+c-h][y+r-h].b);
				}
			}
		}
		return new Picture(pdxd);
	}
	
	public BufferedImage getImage(String filename) throws IOException{
		BufferedImage image = new BufferedImage(x/*Width*/, y/*height*/, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<x;i++)
			for(int j=0;j<y;j++){
				Color c;
				if(pixels[i][j]==null)
					c = new Color(255,255,255);
				else
					c = new Color(pixels[i][j].r, pixels[i][j].g, pixels[i][j].b);
		        image.setRGB(i, j, c.getRGB());
			}
		ImageIO.write(image, "jpg", new File(filename+".jpg"));
		return image;
	}
}