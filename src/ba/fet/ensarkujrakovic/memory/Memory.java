package ba.fet.ensarkujrakovic.memory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Ne radi!

public class Memory {
	public static void main(String[] args) {
		MemoryFrame frame = new MemoryFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
class MemoryFrame extends JFrame{
	static final int DEFAULT_WIDTH = 384;
	static final int DEFAULT_HEIGHT = 458;
	ButtonPanel bPanel = new ButtonPanel();
	static GraphicsPanel gPanel = new GraphicsPanel();
	public MemoryFrame(){
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		add(gPanel, BorderLayout.CENTER);
		add(bPanel, BorderLayout.SOUTH);
	}
	
}
class GraphicsPanel extends JPanel implements MouseListener{
	static final int CARD_SIDE = 96;
	static int x1, y1, x2, y2, c = 0;
	List<Integer> imgArray = new ArrayList<Integer>();
	List<Card> listCards = new ArrayList<Card>();
	int[][] imgArrayTmp = new int[16][2];
	Image img;
	static int gameCount = 0;
	static int mouseCount = 0;
	public GraphicsPanel(){
		addMouseListener(this);
		try{
			img = ImageIO.read(new File("img.jpg"));
		}catch(IOException e){}
		randomize();
	}
	
	public void randomize(){
		listCards.clear();
		
		for(int k= 0; k < 8; ++k){
			Card karta1 = new Card();
			karta1.index = k;
			karta1.xI = k*96;
			listCards.add(karta1);
			Card karta2 = new Card();
			karta2.index = 16-k-1;
			karta2.xI = (8-k)*96;
			listCards.add(karta2);
		}
		Collections.shuffle(listCards);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(gameCount == 16){
			//pobjeda
			return;
		}
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				Graphics2D g1 = (Graphics2D) g;
				g1.setColor(Color.BLUE);
				Rectangle2D bckgr =  new Rectangle2D.Double(i*CARD_SIDE, j*CARD_SIDE, CARD_SIDE, CARD_SIDE);
				g1.fill(bckgr);
				g1.setColor(Color.WHITE);
				g1.draw(bckgr);
			}
		}
		for(int i = 0; i < 16; ++i){
			Card karta = listCards.get(i);
			karta.xP = i/4*CARD_SIDE;
			karta.yP = (i%4)*CARD_SIDE;
			if(karta.visible == true){
				listCards.set(i, karta);
				Graphics2D g2 = (Graphics2D) g;
					g2.drawImage(img, karta.xP, karta.yP, karta.xP+CARD_SIDE, karta.yP+CARD_SIDE,
								karta.xI, 0,
						karta.xI+CARD_SIDE, CARD_SIDE, null);
			}
		}
	}
	public void click(){
		
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	int ii = 0, jj = 0;
	public void mousePressed(MouseEvent e){
		++mouseCount;
		int x = e.getX();
		int y = e.getY();
		x = x - x%96;
		y = y - y%96;
		if(mouseCount == 1){
			while(ii < 16){
				if(x == listCards.get(ii).xP && y == listCards.get(ii).yP) break;
				++ii;
			}
			if(ii == 16){ ii = 0; return;}
			setVisible(true, ii);
			repaint();
			return;
		}
		if(mouseCount == 2){
			
			while(jj < 15){
				if(x == listCards.get(jj).xP && y == listCards.get(jj).yP && jj != ii) break;
				++jj;
			}
			if(jj == 16){ jj = 0; return;}
			setVisible(true, jj);
			repaint();
			mouseCount = 0;
		}
		try{
		    Thread.sleep(500);
		}catch(Exception err){}
		if(listCards.get(jj).xI != listCards.get(ii).xI){
			setVisible(false, ii);
			setVisible(false, jj);
			
			ii = 0; jj = 0;
			repaint();
			return;
		}
	}
	public void mouseReleased(MouseEvent e){}
	public void setVisible(boolean b, int i){
		Card c = listCards.get(i);
		c.visible = b;
		listCards.set(i, c);
	}
}

class ButtonPanel extends JPanel{
	JButton button1 = new JButton("Nova igra");
	JButton button2 = new JButton("Izlaz");
	String actionString;
	ActionListener listener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			actionString = e.getActionCommand();
			if(actionString.equals("Izlaz"))
				System.exit(0);
			if(actionString.equals("Nova igra")){
				MemoryFrame.gPanel.mouseCount = 0;
				MemoryFrame.gPanel.gameCount = 0;
				MemoryFrame.gPanel.randomize();
				MemoryFrame.gPanel.repaint();
			}
		}
	};
	
	public ButtonPanel(){
		add(button1);
		add(button2);
		
		button1.addActionListener(listener);
		button2.addActionListener(listener);
	}
}

class Card{
	public int index = 0;
	public int xI = 0;
	public int xP = 0;
	public int yP = 0;
	public boolean visible = false;
}