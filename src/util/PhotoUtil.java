package util;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import bean.Color;
import bean.Location;

public class PhotoUtil {
	
	public static final String IMAGE_TYPE_BMP = "BMP";
	
	/**
	 * 比较两张图片的不同之处  不同点用黑色填充
	 * @param imagePath1
	 * @param imagePath2
	 * @return
	 * @throws Exception 
	 */
	public static byte[] getDiff(byte[] image1,byte[] image2) throws Exception{
		byte[] newImage = image1;
		int width = getWidth(image1);
		int height = getHeight(image1);
		int x = 54;	//蓝颜色处于文件图像的第55位之后,这样从55位开始进行操作,每间隔3位都是处理蓝色
		int y = 55; //绿颜色处于文件图像的第56位之后,这样从56位开始进行操作,每间隔3位都是处理绿色
		int z = 56; //红颜色处于文件图像的第57位之后,这样从57位开始进行操作,每间隔3位都是处理红色
		//纠正位      因为每一扫描行的字节数必须是4的整数倍，所以加入纠正位
		int correct = width*3%4==0?0:4-width*3%4;
		for(int i = 0;i<height;i++){
			for(int j = 0;j<width;j++){
				int r1 = image1[z];	//在原图中,从第57位开始,每三位读一次,读出的是红色的数据
				int g1 = image1[y]; //在原图中,从第56位开始,每三位读一次,读出的是绿色的数据
				int b1 = image1[x]; //在原图中,从第55位开始,每三位读一次,读出的是蓝色的数据
				int r2 = image2[z];
				int g2 = image2[y];
				int b2 = image2[x];
				
				//比较两张图-原图片与比照图片的三原色是否不相同  在新的image里用黑色标识
				/*
				 * 0~255的颜色用byte-128~127表示 
					其中0~127对应0~127 
					-128~-1 对应128~255
				 */
				if(r1+g1+b1!=r2+g2+b2){
					newImage[x] = 0;
		            newImage[y] = 0;
                    newImage[z] = -1;
				}
				//每间隔三位是一种颜色
				x += 3;
				y += 3;
				z += 3;
			}
			//因为每一扫描行的字节数必须是4的整数倍，所以加入纠正位
			x += correct;
			y += correct;
			z += correct;
		}
		return newImage;
	}
	
	/**
	 * 把图片转成字节数组 
	 * @param imagePath
	 * @param imageType
	 * @return
	 * @throws IOException
	 */
	public static byte[] imageToByteArray(String imagePath) throws IOException {
		File file = new File(imagePath);
		InputStream input = new FileInputStream(file);
		byte[] a = new byte[(int) file.length()];
		input.read(a);
	    return a;
	}
	
	/**
	 * 屏幕截图
	 * @param rectangle
	 * @return
	 * @throws AWTException
	 * @throws IOException
	 */
	public static byte[] cutPhoto(Rectangle rectangle) throws Exception{
        Robot ro=new Robot();
        BufferedImage bufferedImage=ro.createScreenCapture(new Rectangle(rectangle));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    ImageIO.write(bufferedImage,PhotoUtil.IMAGE_TYPE_BMP,stream);
	    return stream.toByteArray();
	}

	/**
	 * 将字节集写出
	 * @param path
	 * @param imageArray
	 * @throws IOException
	 */
	public static void writeImage(String path,byte[] imageArray) throws IOException {
		FileOutputStream output = new FileOutputStream(path);
		output.write(imageArray);
	}
	
	/**
	 * 从左到右  从上到下
	 * 在当前屏幕上找指定的图片 找到返回 图片左上角对应屏幕的X,Y 坐标
	 * 否则返回-1，-1
	 * @return
	 * @throws Exception 
	 */
	public static Location findImageInFullScreen(byte[] image) throws Exception{
		byte[] fullImage;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		fullImage = PhotoUtil.cutPhoto(new Rectangle(0,0,screenWidth,screenHeight));
		for(int y1=0;y1<getHeight(fullImage)-getHeight(image);y1++){
			for(int x1=0;x1<getWidth(fullImage)-getWidth(image);x1++){
				boolean flag = true;
				if(PhotoUtil.getColorByLocation(new Location(x1,y1), fullImage).
					equals(PhotoUtil.getColorByLocation(new Location(0, 0), image))){
					//+3 提高识别速度
					for(int y2=0;y2<getHeight(image);y2+=3){
						for(int x2=1;x2<getWidth(image);x2+=3){
							if(!PhotoUtil.getColorByLocation(new Location(x1+x2,y1+y2), fullImage).
									equals(PhotoUtil.getColorByLocation(new Location(x2, y2), image))){
								flag = false;
								break;
							}
						}
						if(!flag){
							break;
						}
					}
				}else {
					flag = false;
				}
				if(flag){
					return new Location(x1,y1);
				}
			}
		}
		return new Location(-1,-1);
	}
	
	/**
	 * 在指定屏幕范围内查找指定的图片 找到返回true 否则false
	 * 这个方法比上面的要快很多 因为不需要找到具体坐标而且不是全屏找，只要判断有没有该图片
	 * @return
	 * @throws Exception 
	 */
	public static boolean haveImageInScope(int x,int y,int width,int heigth,byte[] image) throws Exception{
		byte[] fullImage;
		fullImage = PhotoUtil.cutPhoto(new Rectangle(x,y,width,heigth));
		for(int y1=0;y1<getHeight(fullImage)-getHeight(image);y1+=3){
			for(int x1=0;x1<getWidth(fullImage)-getWidth(image);x1+=3){
				boolean flag = true;
				if(PhotoUtil.getColorByLocation(new Location(x1,y1), fullImage).
					equals(PhotoUtil.getColorByLocation(new Location(0, 0), image))){
					//+3 提高识别速度
					for(int y2=0;y2<getHeight(image);y2+=3){
						for(int x2=1;x2<getWidth(image);x2+=3){
							if(!PhotoUtil.getColorByLocation(new Location(x1+x2,y1+y2), fullImage).
									equals(PhotoUtil.getColorByLocation(new Location(x2, y2), image))){
								flag = false;
								break;
							}
						}
						if(!flag){
							break;
						}
					}
				}else {
					flag = false;
				}
				if(flag){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 根据图片中某点坐标取该点的颜色
	 * @param location
	 * @param imageByte
	 * @return
	 * @throws Exception 
	 */
	private static Color getColorByLocation(Location location,byte[] image) throws Exception{
		Color color = new Color();
		int width = getWidth(image);
		int height = getHeight(image);
		if((location.getX()+1)>width){
			throw new Exception("宽度过大！");
		}
		if((location.getY()+1)>height){
			throw new Exception("高度过大！");
		}
		int ly = height-location.getY()-1;
		int correct = width*3%4==0?0:4-width*3%4;
		int x = ly*correct+location.getX()*3+ly*width*3+54;
		int y = x+1;
		int z = y+1;
		color.setR(image[z]);
		color.setG(image[y]);
		color.setB(image[x]);
		return color;
	}
	
	/**
	 * 取图片宽度
	 */
	private static int getWidth(byte[] image) throws Exception{
		return (int) (calc(image[21])*Math.pow(256, 3)+calc(image[20])*Math.pow(256, 2)+calc(image[19])*256+calc(image[18]));
	}
	
	/**
	 * 取图片高度
	 */
	private static int getHeight(byte[] image) throws Exception{
		return (int) (calc(image[25])*Math.pow(256, 3)+calc(image[24])*Math.pow(256, 2)+calc(image[23])*256+calc(image[22]));
	}
	
	private static int calc(byte num){
		return num>=0?num:256+num;
	}
	
	public static byte[] getImage(int level) throws Exception{
		return imageToByteArray("images/"+level+".bmp");
	}
}
