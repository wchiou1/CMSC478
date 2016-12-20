package Parser;

import java.util.Enumeration;
import java.util.Hashtable;

public class ColorMapper{
	private static Hashtable<String, Picture> RGBColorMaps = new Hashtable<String, Picture>();
	private static Hashtable<String, Integer> RGBColorFreq = new Hashtable<String, Integer>();
	/**
	 * Gets what RGBColor should be at the center of the picture
	 * Will console out an error if the center is already filled
	 * @param template
	 * @return
	 */
	public static RGBColor getRGBColor(Picture template){
		//First check if the center of the picture is null
		int size = template.x;
		if(template.pixels[size/2][size/2]!=null){
			System.out.println("WARNING: Attempted to use center filled picture as template.");
			return template.pixels[size/2][size/2];
		}
		
		//set distance to something superhigh
		double avgDistance = 10000000;
		String RGBColorBuffer = "";
		//Check passed, start iterating through all RGBColors, trying to find one that "fits" best
		//We only want to iterate once, this is expensive enough as it is
		
		//Iterate through all RGBColors in memory
		Enumeration<String> keys=RGBColorMaps.keys();
		while(keys.hasMoreElements()){
			String key=keys.nextElement();
			//Get the picture from memory
			Picture temp = RGBColorMaps.get(key);
			//Get the distance
			double tempDis=template.getDifference(temp);
			if(tempDis<=avgDistance){
				RGBColorBuffer = key;
				avgDistance = tempDis;
			}
		}		
		return stringToRGBColor(RGBColorBuffer);
	}
	/**
	 * Gets the RGBColorMap for a RGBColor
	 * @param RGBColor as a string format: r,g,b
	 * @return
	 */
	public static Picture getMap(RGBColor RGBColor){
		return RGBColorMaps.get(RGBColorToString(RGBColor));
	}
	public static RGBColor stringToRGBColor(String rgb){
		String[] values=rgb.split(",");
		int r=Integer.parseInt(values[0]);
		int g=Integer.parseInt(values[1]);
		int b=Integer.parseInt(values[2]);
		return new RGBColor(r,g,b);
	}
	public static String RGBColorToString(RGBColor RGBColor){
		return ""+RGBColor.r+","+RGBColor.g+","+RGBColor.b;
	}
	/**
	 * Given a RGBColor and it's surrounding area
	 * adds to the existing RGBColorMap for that RGBColor
	 * @param RGBColor
	 */
	public static void updatePicture(RGBColor RGBColor, Picture newPic){
		Picture current = getMap(RGBColor);
		String RGBColorStr = RGBColorToString(RGBColor);
		int count;
		//Get the total pictures counted
		if(!RGBColorFreq.containsKey(RGBColorStr))
			//Init count is new RGBColor
			count=1;
		else
			count = RGBColorFreq.get(RGBColorStr);
		
		//Check if the RGBColormap needs to be initialized
		if(current==null){
			System.out.println("New RGBColor added! "+RGBColorStr);
			//Init the new picture for the RGBColor
			RGBColorMaps.put(RGBColorStr, newPic);
			return;//We don't need to the rest of the code
		}
		
		//Count will be used to determine average
		RGBColor[][] currentPixels = current.pixels;
		RGBColor[][] newPixels = newPic.pixels;
		//Error check to avoid very bad things happening
		if(currentPixels.length!=newPixels.length||currentPixels[0].length!=newPixels[0].length){
			System.out.println("Error: new and existing pictures have unequal dimensions.");
			return;
		}
		
		int r,g,b;
		//Update the "average" surrounding picture
		for(int x=0;x<currentPixels.length;x++)
			for(int y=0;y<currentPixels[0].length;y++){
				if(currentPixels[x][y]==null){//Just overwrite them with the new
					currentPixels[x][y]=newPixels[x][y];
					continue;
				}
				if(newPixels[x][y]==null)//New pixels are null? No change!
					continue;
				//Both of them aren't null, apply change regular
				r = (currentPixels[x][y].r*count+newPixels[x][y].r)/(count+1);
				g = (currentPixels[x][y].g*count+newPixels[x][y].g)/(count+1);
				b = (currentPixels[x][y].b*count+newPixels[x][y].b)/(count+1);
				currentPixels[x][y].r=r;
				currentPixels[x][y].g=g;
				currentPixels[x][y].b=b;
			}
		
		//Update the pixels
		RGBColorMaps.put(RGBColorStr, new Picture(currentPixels));
		//Update the count value for future updates
		RGBColorFreq.put(RGBColorStr, count++);
		System.out.println("Updated "+RGBColorStr+" ("+count+" times)");
	}
}