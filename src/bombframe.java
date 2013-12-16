/*
 * Class Name:bombframe
 *  
 * Author:TDL
 * 
 * Date:2013.12.26 
 * 
 * Note:This is a Minesweep game.The code content in this class is the core of the game. 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class bombframe extends JFrame implements ActionListener {
	private static final long serialVersionUID = 3L;
	private static  int Weight;
	private static  int Height;
	private static bombproperty[] blank;
	private static int totalbomb = 0;
	private Timer time;
	private JLabel label1;
	private boolean notbombyet=true;
	private int counter=0;
	private int notbomb=0;
	private JFrame pg;
	
	public bombframe(final int rrow,final int rcolumn) {
		//set a new frame
		pg=new JFrame();
		Weight=rrow*100+100;
		Height=rcolumn*50+50;        
        final int row=rrow+2;
		final int column=rcolumn+2;
		label1=new JLabel("0");			//use to count time 
		Timer timer=new Timer(1000,this);
		time=timer;		
		pg.setSize(Weight, Height);
		blank = new bombproperty[row*column];			
		GridLayout border1 = new GridLayout(row, column);
		JPanel bigpanel = new JPanel(border1);			//include blank
		bigpanel.setSize(100*rrow,50*rcolumn);			
		JPanel time_reset = new JPanel();			//include time-counter and reset button 
		JButton setrc=new JButton("reset row and column");
		setrc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				counter=0;
				notbomb=0;
				pg.dispose();
				new bombgame();
			}
		});
		JButton reset = new JButton("play again");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset(rrow,rcolumn);
			}
		});		
		for (int i = 0; i < blank.length; i++) {
			blank[i] = new bombproperty();
			bigpanel.add(blank[i], border1);
		}
		reset(rrow,rcolumn);
		for (int i = 0; i < blank.length; i++) {
			//set the reaction for each blank when it is clicked.
			blank[i].addActionListener(new ActionListener() {			
				public void actionPerformed(ActionEvent e) {
					if (((bombproperty) e.getSource()).notclick) {
						if(notbombyet)
							time.start();				
						if (((bombproperty) e.getSource()).isbomb()) {
							((bombproperty) e.getSource()).setText("*");
							((bombproperty) e.getSource()).notclick=false;
							time.stop();
							notbombyet=false;
							for (int i = 0; i < blank.length; i++) {
								//show where all the bomb is
								if(blank[i].isbomb()){
									blank[i].setText("*");
							}								
						}						
						JOptionPane.showMessageDialog(null, "bomb~~", "you lose", JOptionPane.DEFAULT_OPTION );
					} else if (aroundbomb( ((bombproperty) e.getSource()) , rrow , rcolumn ) == 0) {
						show(((bombproperty) e.getSource()),rrow,rcolumn);
					}
					else{ ((bombproperty) e.getSource()).setText("" + aroundbomb(((bombproperty) e.getSource()) , rrow , rcolumn ));
							((bombproperty) e.getSource()).setBackground(Color.LIGHT_GRAY);
							((bombproperty) e.getSource()).notclick=false;
							win_or_not();					
					}}}});
		}
		time_reset.add(reset);
		time_reset.add(setrc);
		time_reset.add(label1);
		pg.setLayout(new GridLayout(2,1));
		pg.add(bigpanel);
		pg.add(time_reset);		   	
		time_reset.setVisible(true);
		bigpanel.setVisible(true);
		pg.setVisible(true);
	}

	public void reset(int rrow , int rcolumn) {
		//reset all the value
		counter=0;
		notbombyet=true;
		label1.setText("0");
		time.stop();
		notbomb=0;
		totalbomb = 0;
		/*	I only use a one dimension matrice to deal the blank.
		 *	The logic of blank which is on the edge is too complicated to write,
		 *	so I build two imagined column to both right and left side,and so do the row.
		 *	Then I let the 4 imagined blank line which seem like the frame of the bottun be invisible.  
		 *	Because of them,I can write the same code on any blank inside it. 
		 */
		
		int row=rrow+2;			//imagined row
		int column=rcolumn+2;			//imagined row
		
		//set invisible
		for (int i = 0; i < blank.length; i = i + column) {
			blank[i].setVisible(false);
		}
		for (int i = column-1; i < blank.length; i = i + column) {
			blank[i].setVisible(false);
		}
		for (int i = (row-1)*column; i < blank.length; i++) {
			blank[i].setVisible(false);
		}
		for (int i = 0; i < column; i++) {
			blank[i].setVisible(false);
		}		
		for (int i = 0; i < blank.length; i++) {
			//initialize
			blank[i].setBackground(null);
			blank[i].setText("");
			blank[i].notclick=true;
			blank[i].setBomb(0);
			if(blank[i].isVisible())
				counter++;
		}		
		while (totalbomb <= (int)(rcolumn*rrow/4.5)) {
			//set where the bomb is
			Random r=new Random();
			int ratio=r.nextInt(column*row-1);
			if( blank[ratio].isVisible() && blank[ratio].isbomb() == false ){			
				blank[ratio].setBomb(1);
				totalbomb++;
				counter--;
			}
		}		
	}

	public int aroundbomb(bombproperty a,int rrow,int rcolumn) {
		//a method to show the total bomb number of the 8 blank around it 
		int ab = 0;
		int row=rrow+2;
		int column=rcolumn+2;
		for (int i = 0; i < (row*column); i++) {			
			if (a.equals(blank[i])) {
				ab = blank[i + 1].getBomb() + blank[i - 1].getBomb()
						+ blank[i + column].getBomb() + blank[i - column].getBomb()
						+ blank[i + column-1].getBomb() + blank[i - column+1].getBomb()
						+ blank[i + column+1].getBomb() + blank[i - column-1].getBomb();
			}
		}
		return ab;
	}

	
	public void actionPerformed(ActionEvent a) {
		//time counter
		int x=Integer.parseInt(label1.getText());
		label1.setText(""+(++x));		
	}
	
	public void show(Object a,int rrow,int rcolumn){
		// when there is no bomb around it,show the 8 blank 
		int row=rrow+2;
		int column=rcolumn+2;		
		for(int i=0;i<row*column;i++){
			if(a.equals(blank[i])){
				blank[i].setText("0");
				blank[i].notclick=false;				
				int drc[]={1,(-1),(-column+1),(-column),(column-1),
						(column),(-column-1),(column+1)};			//8 blank around
				int[] qx=new int[row*column];
				int count=0;
				int trace=0;
				qx[trace]=i;			//storage of which blank also has 0 bomb around it 
				trace++;
				
				while(count<trace){
					int mx=qx[count];					
					if (aroundbomb(blank[mx],rrow,rcolumn) == 0 && blank[mx].isVisible() ){
						for(int k=0;k<8;k++){
							int tx=mx+drc[k];							
							if ( tx<(row*column-1) && tx>=0 && blank[tx].notclick && blank[tx].isVisible()) {
							blank[tx].notclick=false;
							qx[trace]=tx;
							trace++;
						}}						
					}count++;
				}				
				for(int n=0;n<trace;n++){
					//show the 8 blank or more
					win_or_not();					
					blank[qx[n]].setText(""+aroundbomb(blank[qx[n]],rrow,rcolumn));
					blank[qx[n]].setBackground(Color.LIGHT_GRAY);
				}
			}
		}		
	}
	
	public void win_or_not(){
		//if all the not bomb blamk have been clicked,then you win 
		if(notbombyet){
			notbomb++;
		if(notbomb==counter){
			time.stop();
			JOptionPane.showMessageDialog(null, "Congratulation~~\ntime="+label1.getText()+"s", "you win", JOptionPane.DEFAULT_OPTION );
			}
		}
	}
	
	public class bombproperty extends JButton{
		private static final long serialVersionUID = 2L;
		private int bomb;
		public boolean notclick=true;
		
		public bombproperty(){
			super();
		}
			
		public int getBomb() {
			return bomb;
		}
		public void setBomb(int bomb) {
			this.bomb = bomb;
		}
		public boolean isbomb(){
			return(bomb == 1 ? true : false);			
		}
	
	}

}
	
