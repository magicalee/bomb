/*
 * Class Name:bombgame
 * 
 * Author:TDL
 * 
 * Date:2013.12.26 
 * 
 * Note:This is a Minesweep game.The code content in this class is the setting and starting.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import javax.swing.*;

public class bombgame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static int Weight=500;
	public static int Height=400;
	private static int row;
	private static int column;
	private TextField text1,text2;
	private JLabel label1,label2;
	private JFrame startwindow = new JFrame();
	
	public bombgame(){
		//set a frame
		super();		
		startwindow.setSize(Weight,Height);
		startwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startwindow.setVisible(true);
		startwindow.setLayout(new GridLayout(5,1));
		text1=new TextField();
		text2=new TextField();
		label1=new JLabel("row");
		label2=new JLabel("column");
		JButton play = new JButton("play");		
		play.setSize(100, 50);	
		startwindow.setTitle("bomb");		
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (text1.getText().equals("") || text2.getText().equals("")) {
					//not enter the number of row or column
					JOptionPane.showMessageDialog(null,"row and column must be integer!!!","error", JOptionPane.ERROR_MESSAGE);
				}
				
				if (isinteger(text1.getText()) && isinteger(text2.getText())) {
					row=Integer.parseInt(text1.getText());
					column=Integer.parseInt(text2.getText());
					startwindow.dispose();
					new bombframe(row,column);				
				}
				else{
					JOptionPane.showMessageDialog(null,"row and column must be integer!!!","error", JOptionPane.ERROR_MESSAGE);
				}
			}});
		startwindow.add(label1);
		startwindow.add(text1);
		startwindow.add(label2);
		startwindow.add(text2);
		startwindow.add(play);		
	}	
	
	public static void main(String[] args) {
		new bombgame();
	}		
	
	public boolean isinteger(String str){
		Pattern pat=Pattern.compile("[0-9]*");
		return pat.matcher(str).matches();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			
	}

}
