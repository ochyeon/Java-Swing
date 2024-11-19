import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GraphicEx03 extends JFrame {
	private MyPanel panel = new MyPanel();
	private ImageIcon icon = new ImageIcon("images/sunset.JPG");
	private Image img = icon.getImage();
	private String message = "Game Clear";
	private int clipX = 0, clipY = 0;
	private int stringX = 0, stringY = 0;
	private int clipSize = 30;
	private boolean showFlag = false;
	private Random random = new Random();
	
	public GraphicEx03(){
		setTitle("GraphicEx03");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setContentPane(panel);
		createMenu();
		setSize(400, 400);
		setVisible(true);
		
		panel.setFocusable(true);
		panel.requestFocus();
		
		setStringPosition(); // 문자열 위치 초기화
	}
	
	private void setStringPosition() {
		int menuBarHeight = getJMenuBar().getHeight();
	    stringX = random.nextInt(panel.getWidth() - message.length());
	    stringY = random.nextInt(panel.getHeight() - menuBarHeight);
	}
	
	private void createMenu() {
		JMenuBar mb = new JMenuBar();
		JMenu GameMenu = new JMenu("GameMenu");
		JMenuItem CloseMenu = new JMenuItem("GameClose");
		JMenuItem sizeItem = new JMenuItem("Set clip size");
		JMenuItem reStartItem = new JMenuItem("Restart");
		JMenuItem WholeImgItem = new JMenuItem("Show whole image");
		
		sizeItem.addActionListener(new SizeActionListener());
		reStartItem.addActionListener(new ReStartActionListener());
		WholeImgItem.addActionListener(new WholeImgActionListener());
		
		GameMenu.add(sizeItem);
		GameMenu.add(reStartItem);
		GameMenu.addSeparator();
		GameMenu.add(WholeImgItem);
		mb.add(GameMenu);
		mb.add(CloseMenu);
		
		// 종료 메뉴
		CloseMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
		});
		
		setJMenuBar(mb);
	}
	// 클립 사이즈 조절 (팝업 다이얼로그로 사이즈 입력 받아 조절)
	public class SizeActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String newSize = (JOptionPane.showInputDialog("사이즈 변경"));
			if(newSize == null || newSize.isEmpty()) {
				clipSize = 30;
				return;
			}
			
			clipSize = Integer.parseInt(newSize);
			if(clipSize <= 0) {
				JOptionPane.showMessageDialog(null, "잘못된 입력입니다.", "Message", JOptionPane.ERROR_MESSAGE);
				clipSize = 30;
			}
			repaint();
		}
	}
	// 게임 재시작 (클립 사이즈 초기화, 문자 위치 랜덤 변경)
	public class ReStartActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			clipSize = 30;
			int menuBarHeight = getJMenuBar().getHeight();
			stringX = random.nextInt(panel.getWidth()) - message.length();
			stringY = random.nextInt(panel.getHeight() - menuBarHeight);
			showFlag = false;
			repaint();
		}
	}
	// 정답 보기 (전체 이미지 및 문자 위치까지 보여줌 -> 클립 적용 안된 화면)
	public class WholeImgActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			showFlag = true;
			repaint();
		}
	}
	
	public class MyPanel extends JPanel{
		public MyPanel() {
			setLayout(null);
			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					switch(e.getKeyCode()) {
					case KeyEvent.VK_UP:
						clipY = Math.max(0, clipY - 10);
						break;
					case KeyEvent.VK_DOWN:
						clipY = Math.min(getHeight() - clipSize, clipY + 10);
						break;
					case KeyEvent.VK_RIGHT:
						clipX = Math.min(getWidth() - clipSize, clipX + 10);
						break;
					case KeyEvent.VK_LEFT:
						clipX = Math.max(0, clipX - 10);
						break;
					}
					repaint();
				}
			});
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if(showFlag) {
				g.setClip(null);
			}
			else {
				g.setClip(clipX, clipY, clipSize, clipSize);
			}
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			g.setColor(Color.GREEN);
			g.drawString("Game Clear", stringX, stringY);
		}
	}
	public static void main(String args[]) {
		new GraphicEx03();
	}
}
