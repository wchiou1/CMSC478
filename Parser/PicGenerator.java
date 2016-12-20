package Parser;

import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PicGenerator{
	int fragmentSize;//FragmentSize MUST BE ODD
	ArrayList<Picture> sourcePics;
	public PicGenerator(int fragSize) throws Exception{
		if(fragSize%2==0)
		{
			throw new Exception("EVEN FRAGMENT SIZE: Please use an odd fragment size");
		}
		this.fragmentSize=fragSize;
		sourcePics = new ArrayList<Picture>();
	}
	
	public void addPicture(Picture source){
		//Add the picture to the list of sources
		sourcePics.add(source);
		//How many colors do we need to process?
		int x = source.x;
		int y = source.y;
		
		//Iterate through ALL pixels
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				//Get the fragment
				Picture fragment = source.GetSubPixels(i,j,fragmentSize);
				//Store that fragment in the colormapper
				ColorMapper.updatePicture(source.pixels[i][j], fragment);
			}
		}
		System.out.println("Added new picture, Size:("+x+","+y+")");
	}
	
	public void createNewPic(int width,int height,ImageIcon progressIcon,JPanel panel) throws IOException{
		//Init the picture
		System.out.println("Creating new picture");
		RGBColor[][] pixels= new RGBColor[width][height];
		Picture WIP = new Picture(pixels);
		
		Picture seed = getSeed();
		
		//Display the seed gotten
		seed.getImage("seed");

		
		//Seed is twice the size of a fragment
		//Copy the seed onto the picture.
		
		
		//Get where the upper left should be
		int seedx = width/2-fragmentSize;
		int seedy = height/2-fragmentSize;
		
		for(int i=0;i<fragmentSize*2;i++){
			for(int j=0;j<fragmentSize*2;j++){
				pixels[seedx+i][seedy+j]=seed.pixels[i][j];
			}
		}
		
		progressIcon.setImage(WIP.getImage("WIP"));
		panel.repaint();
		
		int largerSide = Math.max(width, height);
		
		//Now go in an increasing circle outward until all pixels are filled
		
		//Start in the upperleft corner
		for(int layer=1;layer<=(largerSide-fragmentSize*2)/2;layer++){
			//Each layer is a square of pixels
			if(seedy-layer>=0&&seedy+layer+fragmentSize*2-1<height)
				for(int topx=-layer;topx<fragmentSize*2+layer;topx++){
					//Top side first
					RGBColor temptop=ColorMapper.getRGBColor(WIP.GetSubPixels(seedx+topx, seedy-layer, fragmentSize));
					pixels[seedx+topx][seedy-layer]=temptop;
					
					//Do bottom now(bot is same as top)
					RGBColor tempbot=ColorMapper.getRGBColor(WIP.GetSubPixels(seedx+topx, seedy+layer+fragmentSize*2-1, fragmentSize));
					pixels[seedx+topx][seedy+layer+fragmentSize*2-1]=tempbot;
				}
			
			//Do sides now
			if(seedx-layer>=0&&seedx+layer+fragmentSize*2-1<width)
				for(int sidey=-layer+1;sidey<fragmentSize*2+layer-1;sidey++){
					//Left side first
					RGBColor temptop=ColorMapper.getRGBColor(WIP.GetSubPixels(seedx-layer, seedy+sidey, fragmentSize));
					pixels[seedx-layer][seedy+sidey]=temptop;
					
					//Do bottom now(bot is same as top)
					RGBColor tempbot=ColorMapper.getRGBColor(WIP.GetSubPixels(seedx+layer+fragmentSize*2-1, seedy+sidey, fragmentSize));
					pixels[seedx+layer+fragmentSize*2-1][seedy+sidey]=tempbot;
				}
			progressIcon.setImage(WIP.getImage("WIP"));
			panel.repaint();
			System.out.println("Layer "+layer+" done");
			
		}
		
		try {
			WIP.getImage("DONE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	private Picture getSeed(){
		Picture seedSource=sourcePics.get((int)Math.floor(Math.random()*sourcePics.size()));
		int x = (int)Math.floor((Math.random()*(seedSource.x-fragmentSize*2))+fragmentSize);
		int y = (int)Math.floor((Math.random()*(seedSource.y-fragmentSize*2))+fragmentSize);
		Picture seed = seedSource.GetSubPixels(x, y, fragmentSize*2);
		return seed;
	}
	
}