package test;

import java.awt.Rectangle;

import bean.Location;

import util.PhotoUtil;
import zhao.DiffFrame;

public class Test {

	public static void main(String[] args) throws Exception {
//		Location location = null;
//		long a = System.currentTimeMillis();
//		System.out.println(location = PhotoUtil.findImageInFullScreen("D:/1.bmp"));
//		System.out.println(System.currentTimeMillis()-a);
		//		/*
//		 * 22¬•À„¥Ì¡À  «Y+180 X+393
//		 */
//		//Õº1
//		PhotoUtil.writeImage("d:/p1.bmp", PhotoUtil.cutPhoto(new Rectangle(location.getX(), location.getY()+180, 381, 286)).getImageBytes());
//		//Õº2
//		PhotoUtil.writeImage("d:/p2.bmp",PhotoUtil.cutPhoto(new Rectangle(location.getX()+393, location.getY()+180, 381, 286)).getImageBytes());
		
		//PhotoUtil.writeImage("d:/p3.bmp", PhotoUtil.getDiff("d:/p1.bmp", "d:/p2.bmp", PhotoUtil.IMAGE_TYPE_BMP)); 
		long a = System.currentTimeMillis();
		System.out.println(PhotoUtil.findImageInFullScreen(PhotoUtil.cutPhoto(new Rectangle(1,1,400,402))));
		System.out.println(System.currentTimeMillis()-a);
		//DiffFrame df = new DiffFrame(200, 300);
//		df.setBackImage(PhotoUtil.cutPhoto(new Rectangle(500,300,400,402)));
	}

}
