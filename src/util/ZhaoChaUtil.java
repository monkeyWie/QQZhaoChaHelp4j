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
	
	//ģ�������
	public static void click() throws Exception{
        Robot robot = new Robot();  
        robot.mousePress(InputEvent.BUTTON1_MASK);  
        robot.delay(100);  
        robot.mouseRelease(InputEvent.BUTTON1_MASK);  
	}
	
	//�Ҳ��߼�
	public static Location zhaoCha(DiffFrame frame) throws Exception{
		frame.setVisible(false);
		String tempPath = "images/temp.bmp";
		//�ҳ����ǵ�����
		Location xx = PhotoUtil.findImageInFullScreen(PhotoUtil.imageToByteArray("images/xx.bmp"));
		//��ȡ�Ҳ�����ߵ�ͼƬ ͼ1�ľ���Ϊ(X,Y+180,381,286)
		byte[] image1 = PhotoUtil.cutPhoto(new Rectangle(xx.getX(),xx.getY()+180,381,286));
		//��ȡ�Ҳ����ұߵ�ͼƬ ͼ2�ľ���Ϊ(X+393,Y+180,381,286)
		byte[] image2 = PhotoUtil.cutPhoto(new Rectangle(xx.getX()+393,xx.getY()+180,381,286));
		//�Աȳ�����ͼƬ����ͬ��������
		byte[] diffImage = PhotoUtil.getDiff(image1, image2);
		//�Ѵ����ƶ���ͼ1��λ�ã���ȫ����ͼ1
		frame.setLocation(xx.getX(),xx.getY()+180);
		PhotoUtil.writeImage(tempPath, diffImage);
		frame.setBackImage(new ImageIcon(ImageIO.read(new File(tempPath))));
		frame.setVisible(true);
		return xx;
	}
	
}
