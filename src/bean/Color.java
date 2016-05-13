package bean;

public class Color {
	private byte r;
	private byte g;
	private byte b;
	
	public byte getR() {
		return r;
	}
	public void setR(byte r) {
		this.r = r;
	}
	public byte getG() {
		return g;
	}
	public void setG(byte g) {
		this.g = g;
	}
	public byte getB() {
		return b;
	}
	public void setB(byte b) {
		this.b = b;
	}
	
	public boolean equals(Object obj) {
		Color color2 = (Color) obj;
		if(this.getR()==color2.getR()&&this.getG()==color2.getG()&&this.getB()==color2.getB()){
			return true;
		}else{
			return false;
		}
	}
	
	public String toString() {
		return "R: "+this.getR()+" G: "+this.getG()+" B: "+this.getB();
	}
	
}
