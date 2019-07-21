package com.redgame.view.panel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.redgame.util.GamePanelUtil;
import com.redgame.util.JXLUtil;
import com.redgame.view.frame.MainFrame;


public class RegisterPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static int limit = 10;

	public RegisterPanel() {
		init();
	}

	public void init() {
		this.setLayout(null);
		this.setSize(640, 600);
		this.add(getLb_pwd());
		this.add(getLb_pwd1());
		this.add(getLb_user());
		this.add(getLb_wel());
		this.add(getPf_pwd());
		this.add(getPf_pwd1());
		this.add(getTf_user());
		this.add(getJb_reg());
		this.add(getJb_return());
	}

	/**
	 * “欢迎注册”标签
	 */
	private JLabel lb_wel = new JLabel("欢迎注册RedGame");

	public JLabel getLb_wel() {
		lb_wel.setFont(new Font("华文楷体", Font.BOLD, 30));
		lb_wel.setBounds(180, 52, 300, 65);
		return lb_wel;
	}

	/**
	 * 用户名
	 */
	private JLabel lb_user = new JLabel("输入用户名：");

	public JLabel getLb_user() {
		lb_user.setFont(new Font("华文楷体", Font.BOLD, 30));
		lb_user.setBounds(126, 172, 200, 50);
		return lb_user;
	}

	private JTextField tf_user = new JTextField();

	public JTextField getTf_user() {
		tf_user.setBounds(320, 172, 170, 50);
		tf_user.setFont(new Font("宋体", Font.BOLD, 20));
		tf_user.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (tf_user.getText().length() >= limit) {
					getToolkit().beep();
					e.consume();
					GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
					JOptionPane.showMessageDialog(null, "用户名不得超过10位！");
				}
			}
		});
		return tf_user;
	}

	/**
	 * 输入密码
	 */
	private JLabel lb_pwd = new JLabel("输 入 密 码：");

	public JLabel getLb_pwd() {
		lb_pwd.setFont(new Font("华文楷体", Font.BOLD, 30));
		lb_pwd.setBounds(126, 263, 200, 52);
		return lb_pwd;
	}

	private JPasswordField pf_pwd = new JPasswordField();

	public JPasswordField getPf_pwd() {
		pf_pwd.setBounds(320, 263, 170, 50);
		pf_pwd.setFont(new Font("宋体", Font.BOLD, 20));
		pf_pwd.addKeyListener(new KeyAdapter() {

			@SuppressWarnings("deprecation")
			@Override
			public void keyTyped(KeyEvent e) {
				if (pf_pwd.getText().length() >= limit) {
					getToolkit().beep();
					e.consume();
					GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
					JOptionPane.showMessageDialog(null, "密码不得超过10位！");
				}
			}

		});
		return pf_pwd;
	}

	/**
	 * 确认密码
	 */
	private JLabel lb_pwd1 = new JLabel("确 认 密 码：");

	public JLabel getLb_pwd1() {
		lb_pwd1.setFont(new Font("华文楷体", Font.BOLD, 30));
		lb_pwd1.setBounds(126, 357, 200, 52);
		return lb_pwd1;
	}

	private JPasswordField pf_pwd1 = new JPasswordField();

	public JPasswordField getPf_pwd1() {
		pf_pwd1.setBounds(320, 357, 170, 50);
		pf_pwd1.setFont(new Font("宋体", Font.BOLD, 20));
		pf_pwd1.addKeyListener(new KeyAdapter() {

			@SuppressWarnings("deprecation")
			@Override
			public void keyTyped(KeyEvent e) {
				if (pf_pwd1.getText().length() >= limit) {
					getToolkit().beep();
					e.consume();
					GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
					JOptionPane.showMessageDialog(null, "密码不得超过10位！");
				}
			}

		});
		return pf_pwd1;
	}

	/**
	 * 注册按钮
	 */
	private JButton jb_reg = new JButton("注册");

	public JButton getJb_reg() {
		jb_reg.setFont(new Font("华文楷体", Font.BOLD, 40));
		jb_reg.setBorderPainted(false);
		jb_reg.setContentAreaFilled(false);
		jb_reg.setBounds(230, 468, 156, 54);
		jb_reg.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				String name = tf_user.getText();
				String pwd = pf_pwd.getText();
				String pwd1 = pf_pwd1.getText();
				if (checkType(name, pwd, pwd1)) {
					if (JXLUtil.checkFileExist(GamePanelUtil.FILEPATH)) {
						JXLUtil.register(GamePanelUtil.FILEPATH, name, pwd);
					}
				}
			}

			/**
			 * 检查输入的合法性
			 * 
			 * @param name
			 * @param pwd
			 * @param pwd1
			 * @return
			 */
			private boolean checkType(String name, String pwd, String pwd1) {
				if (name.contains(" ") || name.contains("	")) {
					JOptionPane.showMessageDialog(null, "用户名含不合法字符！");
					return false;
				} else if (name.length() < 6) {
					JOptionPane.showMessageDialog(null, "用户名小于6位！");
					return false;
				} else if (pwd.length() < 6 || pwd1.length() < 6) {
					JOptionPane.showMessageDialog(null, "密码小于6位！");
					return false;
				} else if (!pwd.equals(pwd1)) {
					JOptionPane.showMessageDialog(null, "两次密码输入不一致！");
					return false;
				}
				return true;
			}
		});
		return jb_reg;
	}

	/**
	 * 返回按钮
	 */
	private JButton jb_return = new JButton("返回");

	public JButton getJb_return() {
		jb_return.setFont(new Font("华文楷体", Font.BOLD, 30));
		jb_return.setBorderPainted(false);
		jb_return.setContentAreaFilled(false);
		jb_return.setBounds(25, 15, 100, 54);
		jb_return.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MainFrame.mf.setContentPane(MainFrame.mf.getMp());
				GamePanelUtil.PlayMusic(GamePanelUtil.CLICK);
			}
		});
		return jb_return;
	}

	/**
	 * 画背景
	 */
	public void paintComponent(Graphics g) {
		int x = 0, y = 0;
		ImageIcon icon = new ImageIcon("img/register.png");
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

	public void actionPerformed(ActionEvent e) {

	}
}
