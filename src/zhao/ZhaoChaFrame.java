package zhao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import bean.Location;

import util.PhotoUtil;
import util.ZhaoChaUtil;

public class ZhaoChaFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	DiffFrame df=null;
	Timer timer = null;
	Location game = null;

	public ZhaoChaFrame(){
		this.setTitle("�Ҳ縨��--by��������");
		this.setLayout(null);
		this.setSize(100,80);
		this.setResizable(false);
		this.setVisible(true);
		JButton b = new JButton("��ʼ�Ҳ�");
		b.setSize(100, 50);
		b.setVisible(true);
		//���������ǲ��ǻ���һ��ͼ��
		timer = new Timer(800, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ZhaoChaUtil.count>=5){
					if(ZhaoChaUtil.level==6){
						try {
							//�ж�Ӯ��֮��ر��Ӵ���
							if(PhotoUtil.haveImageInScope(game.getX()-1,game.getY()+153,133,16,PhotoUtil.imageToByteArray("images/win.bmp"))){
								df.dispose();
								df = null;
								timer.stop();
								ZhaoChaUtil.level=2;
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}else{
						try {
							if(PhotoUtil.haveImageInScope(game.getX()+393,game.getY()+273,15,40,PhotoUtil.imageToByteArray("images/"+ZhaoChaUtil.level+".bmp"))){
								ZhaoChaUtil.count = 0;
								ZhaoChaUtil.level++;
								Thread.sleep(600);
								//��ʼ�Ҳ�ͬ
								game = ZhaoChaUtil.zhaoCha(df);
								//5ͼ��level���ܳ���5
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		b.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!timer.isRunning()){
					timer.start();
				}
				if(df==null){
					df = new DiffFrame();
				}
				try {
					//��ʼ�Ҳ�ͬ
					game = ZhaoChaUtil.zhaoCha(df);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		this.add(b);
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		});
	}
	
	public static void main(String[] args) {
		new ZhaoChaFrame();
	}
}
