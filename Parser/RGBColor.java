package Parser;
public class RGBColor{
	public int r,g,b;
	public RGBColor(int r, int g, int b){
		this.r=r;
		this.g=g;
		this.b=b;
	}
	public String toString(){
		return "("+r+","+g+","+b+")";
	}
}