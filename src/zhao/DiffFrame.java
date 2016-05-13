package zhao;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import util.ZhaoChaUtil;

public class DiffFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel label = new JLabel();
	public DiffFrame(){
		this.setLayout(null);
		this.setSize(381,286);
		this.setUndecorated(true);
		label.setBounds(0, 0, 381,286);
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			//左键按下时，隐藏窗口模拟点击,然后再显示窗口(相当于点击了找茬游戏窗口)
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1){
					setVisible(false);
					try {
						Thread.sleep(150);
						ZhaoChaUtil.click();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					setVisible(true);
					ZhaoChaUtil.count++;
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		this.add(label);
	}
	
	public void setBackImage(ImageIcon image){
		label.setIcon(image);
	}
	
}
