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
	 * �Ƚ�����ͼƬ�Ĳ�֮ͬ��  ��ͬ���ú�ɫ���
	 * @param imagePath1
	 * @param imagePath2
	 * @return
	 * @throws Exception 
	 */
	public static byte[] getDiff(byte[] image1,byte[] image2) throws Exception{
		byte[] newImage = image1;
		int width = getWidth(image1);
		int height = getHeight(image1);
		int x = 54;	//����ɫ�����ļ�ͼ��ĵ�55λ֮��,������55λ��ʼ���в���,ÿ���3λ���Ǵ�����ɫ
		int y = 55; //����ɫ�����ļ�ͼ��ĵ�56λ֮��,������56λ��ʼ���в���,ÿ���3λ���Ǵ�����ɫ
		int z = 56; //����ɫ�����ļ�ͼ��ĵ�57λ֮��,������57λ��ʼ���в���,ÿ���3λ���Ǵ����ɫ
		//����λ      ��Ϊÿһɨ���е��ֽ���������4�������������Լ������λ
		int correct = width*3%4==0?0:4-width*3%4;
		for(int i = 0;i<height;i++){
			for(int j = 0;j<width;j++){
				int r1 = image1[z];	//��ԭͼ��,�ӵ�57λ��ʼ,ÿ��λ��һ��,�������Ǻ�ɫ������
				int g1 = image1[y]; //��ԭͼ��,�ӵ�56λ��ʼ,ÿ��λ��һ��,����������ɫ������
				int b1 = image1[x]; //��ԭͼ��,�ӵ�55λ��ʼ,ÿ��λ��һ��,����������ɫ������
				int r2 = image2[z];
				int g2 = image2[y];
				int b2 = image2[x];
				
				//�Ƚ�����ͼ-ԭͼƬ�����ͼƬ����ԭɫ�Ƿ���ͬ  ���µ�image���ú�ɫ��ʶ
				/*
				 * 0~255����ɫ��byte-128~127��ʾ 
					����0~127��Ӧ0~127 
					-128~-1 ��Ӧ128~255
				 */
				if(r1+g1+b1!=r2+g2+b2){
					newImage[x] = 0;
		            newImage[y] = 0;
                    newImage[z] = -1;
				}
				//ÿ�����λ��һ����ɫ
				x += 3;
				y += 3;
				z += 3;
			}
			//��Ϊÿһɨ���е��ֽ���������4�������������Լ������λ
			x += correct;
			y += correct;
			z += correct;
		}
		return newImage;
	}
	
	/**
	 * ��ͼƬת���ֽ����� 
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
	 * ��Ļ��ͼ
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
	 * ���ֽڼ�д��
	 * @param path
	 * @param imageArray
	 * @throws IOException
	 */
	public static void writeImage(String path,byte[] imageArray) throws IOException {
		FileOutputStream output = new FileOutputStream(path);
		output.write(imageArray);
	}
	
	/**
	 * ������  ���ϵ���
	 * �ڵ�ǰ��Ļ����ָ����ͼƬ �ҵ����� ͼƬ���ϽǶ�Ӧ��Ļ��X,Y ����
	 * ���򷵻�-1��-1
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
					//+3 ���ʶ���ٶ�
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
	 * ��ָ����Ļ��Χ�ڲ���ָ����ͼƬ �ҵ�����true ����false
	 * ��������������Ҫ��ܶ� ��Ϊ����Ҫ�ҵ�����������Ҳ���ȫ���ң�ֻҪ�ж���û�и�ͼƬ
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
					//+3 ���ʶ���ٶ�
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
	 * ����ͼƬ��ĳ������ȡ�õ����ɫ
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
			throw new Exception("��ȹ���");
		}
		if((location.getY()+1)>height){
			throw new Exception("�߶ȹ���");
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
	 * ȡͼƬ���
	 */
	private static int getWidth(byte[] image) throws Exception{
		return (int) (calc(image[21])*Math.pow(256, 3)+calc(image[20])*Math.pow(256, 2)+calc(image[19])*256+calc(image[18]));
	}
	
	/**
	 * ȡͼƬ�߶�
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
