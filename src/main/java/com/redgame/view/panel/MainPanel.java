package com.redgame.view.panel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.redgame.entity.User;
import com.redgame.util.GamePanelUtil;
import com.redgame.util.MainPanelUtil;
import com.redgame.view.frame.MainFrame;


public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int limit = 10;

	private JLabel lb_top = new JLabel("五  子  棋");
	private JLabel lb_user = new JLabel("用  户  名：");
	private JButton jb_Login = new JButton("登录");
	private JButton jb_register = new JButton("注册");
	private JLabel lb_pwd = new JLabel("密       码：");
	private JPasswordField pwd_input = new JPasswordField();
	private JTextField user_input = new JTextField(10);

	public JLabel getLb_top() {
		lb_top.setBounds(206, 74, 270, 100);
		lb_top.setFont(new Font("华文行楷", Font.BOLD, 50));
		return lb_top;
	}

	public JLabel getLb_user() {
		lb_user.setFont(new Font("华文行楷", Font.BOLD, 30));
		lb_user.setBounds(146, 199, 169, 63);
		return lb_user;
	}

	public JButton getJb_Login() {
		jb_Login.setBounds(126, 426, 120, 60);
		jb_Login.setFont(new Font("华文行楷", Font.BOLD, 35));
		jb_Login.setContentAreaFilled(false);
		jb_Login.setBorderPainted(false);
		jb_Login.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String name = user_input.getText();
				@SuppressWarnings("deprecation")
				String pwd = pwd_input.getText();
				if (MainPanelUtil.checkUserPwd(name, pwd)) {
					if (MainPanelUtil.checkUserInfo(name, pwd)) {
						GamePanelUtil.PlayMusic(GamePanelUtil.CLICK);
						MainFrame.mf.setContentPane(MainFrame.mf
								.getLp(new User(name, pwd)));
					} else {
						pwd_input.setText("");
						user_input.setText("");
					}
				}
			}
		});
		return jb_Login;
	}

	public JButton getJb_register() {
		jb_register.setBounds(370, 426, 120, 60);
		jb_register.setFont(new Font("华文行楷", Font.BOLD, 35));
		jb_register.setContentAreaFilled(false);
		jb_register.setBorderPainted(false);
		jb_register.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MainFrame.mf.setContentPane(MainFrame.mf.getRp());
				GamePanelUtil.PlayMusic(GamePanelUtil.CLICK);
			}
		});
		return jb_register;
	}

	public JLabel getLb_pwd() {
		lb_pwd.setFont(new Font("华文行楷", Font.BOLD, 30));
		lb_pwd.setBounds(146, 299, 169, 63);
		return lb_pwd;
	}

	public JPasswordField getPwd_input() {
		pwd_input.setBounds(322, 304, 159, 50);
		pwd_input.setFont(new Font("宋体", Font.BOLD, 20));
		//pwd_input.setText("111111");
		pwd_input.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("deprecation")
			public void keyTyped(KeyEvent e) {
				if (pwd_input.getText().length() >= limit) {
					getToolkit().beep();
					e.consume();
					JOptionPane.showMessageDialog(null, "密码不得超过10位！");
					pwd_input.setText("");
				}
			}

			@SuppressWarnings("static-access")
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyText(e.getKeyCode()).compareToIgnoreCase("Enter") == 0) {
					jb_Login.doClick();
				}
			}
		});
		pwd_input.requestFocus();
		return pwd_input;
	}

	public JTextField getUser_input() {
		user_input.setBounds(322, 204, 159, 50);
		user_input.setFont(new Font("宋体", Font.BOLD, 20));
		//user_input.setText("111111");
		user_input.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (user_input.getText().length() >= limit) {
					getToolkit().beep();
					e.consume();
					JOptionPane.showMessageDialog(null, "用户名不得超过10位！");
				}
			}
		});
		return user_input;
	}

	public MainPanel() {
		init();
	}

	public void paintComponent(Graphics g) {
		int x = 0, y = 0;
		ImageIcon icon = new ImageIcon("img/homePanel.png");
		g.drawImage(icon.getImage(), x, y, getSize().width, getSize().height,
				this);
		while (true) {
			g.drawImage(icon.getImage(), x, y, this);
			if (x > getSize().width && y > getSize().height)
				break;
			// 这段代码是为了保证在窗口大于图片时，图片仍能覆盖整个窗口
			if (x > getSize().width) {
				x = 0;
				y += icon.getIconHeight();
			} else {
				x += icon.getIconWidth();
			}
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		this.setLayout(null);
		this.setSize(640, 600);
		this.add(this.getLb_top());
		this.add(this.getLb_user());
		this.add(this.getLb_pwd());
		this.add(this.getJb_Login());
		this.add(this.getJb_register());
		this.add(this.getPwd_input());
		this.add(this.getUser_input());
	}
}
