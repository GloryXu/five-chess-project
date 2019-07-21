package com.redgame.view.frame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.redgame.entity.User;
import com.redgame.view.panel.GamePanel;
import com.redgame.view.panel.LoginPanel;
import com.redgame.view.panel.MainPanel;
import com.redgame.view.panel.RegisterPanel;


public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1211407413299135764L;

	private MainPanel mp;
	
	private GamePanel gp;
	
	private LoginPanel lp;
	
	private RegisterPanel rp;

	public static MainFrame mf;
	
	public static void main(String[] args) {
		mf = new MainFrame();
		mf.setContentPane(mf.getMp());
	}
	
	public MainFrame(){
		init();
	}
	
	public void init(){
		this.setResizable(false);
		this.setTitle("红游五子棋");
		this.setSize(640, 626);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public MainFrame(JPanel jpanel) {
		super();
		new MainFrame().setContentPane(jpanel);
	}
	
	public MainPanel getMp() {
		mp = new MainPanel();
		return mp;
	}

	public void setMp(MainPanel mp) {
		this.mp = mp;
	}

	public GamePanel getGp(String text,User user) {
		gp= new GamePanel(text,user);
		return gp;
	}

	public void setGp(GamePanel gp) {
		this.gp = gp;
	}

	public RegisterPanel getRp() {
		rp= new RegisterPanel();
		return rp;
	}

	public void setRp(RegisterPanel rp) {
		this.rp = rp;
	}

	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		if(str.equals("登录")){
			this.setContentPane(lp);
		}
	}

	public JPanel getLp(User user) {
		lp = new LoginPanel(user);
		return lp;
	}

}
