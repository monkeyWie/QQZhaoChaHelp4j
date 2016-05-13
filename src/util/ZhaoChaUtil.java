package util;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import zhao.DiffFrame;

import bean.Location;

public class ZhaoChaUtil {
	
	public static int count = 0;
	public static int level = 2;
	
	//模拟鼠标点击
	public static void click() throws Exception{
        Robot robot = new Robot();  
        robot.mousePress(InputEvent.BUTTON1_MASK);  
        robot.delay(100);  
        robot.mouseRelease(InputEvent.BUTTON1_MASK);  
	}
	
	//找茬逻辑
	public static Location zhaoCha(DiffFrame frame) throws Exception{
		frame.setVisible(false);
		String tempPath = "images/temp.bmp";
		//找出星星的坐标
		Location xx = PhotoUtil.findImageInFullScreen(PhotoUtil.imageToByteArray("images/xx.bmp"));
		//截取找茬中左边的图片 图1的矩形为(X,Y+180,381,286)
		byte[] image1 = PhotoUtil.cutPhoto(new Rectangle(xx.getX(),xx.getY()+180,381,286));
		//截取找茬中右边的图片 图2的矩形为(X+393,Y+180,381,286)
		byte[] image2 = PhotoUtil.cutPhoto(new Rectangle(xx.getX()+393,xx.getY()+180,381,286));
		//对比出来的图片，不同处画黑线
		byte[] diffImage = PhotoUtil.getDiff(image1, image2);
		//把窗口移动到图1的位置，完全覆盖图1
		frame.setLocation(xx.getX(),xx.getY()+180);
		PhotoUtil.writeImage(tempPath, diffImage);
		frame.setBackImage(new ImageIcon(ImageIO.read(new File(tempPath))));
		frame.setVisible(true);
		return xx;
	}
	
}
