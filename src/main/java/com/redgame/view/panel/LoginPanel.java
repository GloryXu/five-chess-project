package com.redgame.view.panel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.redgame.entity.User;
import com.redgame.util.GamePanelUtil;
import com.redgame.util.JXLUtil;
import com.redgame.view.frame.MainFrame;



public class LoginPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private User user;
	private String text;

	private static final long serialVersionUID = 1L;

	private JButton jb_return = new JButton("返回");
	private JButton jb_start = new JButton("开始游戏");
	private JRadioButton rb_rj = new JRadioButton("人机对战");
	private JRadioButton rb_rr = new JRadioButton("人人对战");
	private JLabel lb_info;
	private JLabel lb_set;
	private JLabel lb_bottom;

	public LoginPanel() {
	}

	public LoginPanel(User user) {
		this.user = user;
		lb_info = new JLabel("欢迎" + this.user.getName() + "，您当前分数："
				+ JXLUtil.getScore(user.getName()) + "分，排名："
				+ JXLUtil.getRanking(this.user.getName()));
		init();
	}

	private void init() {
		this.setLayout(null);
		this.setSize(640, 600);
		this.add(getJb_return());
		this.add(getJb_start());
		this.add(getLb_info());
		this.add(getLb_bottom());
		ButtonGroup bg = new ButtonGroup();
		bg.add(getRb_rj());
		bg.add(getRb_rr());
		rb_rj.setSelected(true);
		this.add(getRb_rj());
		this.add(getRb_rr());
		this.add(getLb_set());
		jb_start.requestFocus();
	}

	/**
	 * 人机对战&人人对战
	 * 
	 * @return
	 */
	public JRadioButton getRb_rj() {
		rb_rj.setFont(new Font("华文楷体", Font.BOLD, 25));
		rb_rj.setBounds(124, 284, 169, 40);
		rb_rj.setContentAreaFilled(false);
		rb_rj.setBorderPainted(false);
		rb_rj.addActionListener(this);
		return rb_rj;
	}

	public JRadioButton getRb_rr() {
		rb_rr.setFont(new Font("华文楷体", Font.BOLD, 25));
		rb_rr.setBounds(364, 284, 169, 40);
		rb_rr.setContentAreaFilled(false);
		rb_rr.setBorderPainted(false);
		rb_rr.addActionListener(this);
		return rb_rr;
	}

	/**
	 * 返回键
	 * 
	 * @return
	 */
	public JButton getJb_return() {
		jb_return.setBounds(35, 45, 100, 45);
		jb_return.setFont(new Font("华文楷体", Font.BOLD, 30));
		jb_return.setContentAreaFilled(false);
		jb_return.setBorderPainted(false);
		jb_return.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MainFrame.mf.setContentPane(MainFrame.mf.getMp());
				GamePanelUtil.PlayMusic(GamePanelUtil.CLICK);
			}
		});
		jb_return.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					jb_return.doClick();
				}
			}
		});
		return jb_return;
	}

	/**
	 * 开始游戏按钮
	 * 
	 * @return
	 */
	public JButton getJb_start() {
		jb_start.setBounds(200, 464, 235, 50);
		jb_start.setFont(new Font("华文行楷", Font.BOLD, 40));
		jb_start.setContentAreaFilled(false);
		jb_start.setBorderPainted(false);
		jb_start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (rb_rj.isSelected()) {
					text = rb_rj.getText();
				} else {
					text = rb_rr.getText();
				}
				GamePanelUtil.PlayMusic(GamePanelUtil.CLICK);
				MainFrame.mf.setContentPane(MainFrame.mf.getGp(text, user));
			}
		});
		jb_start.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					jb_start.doClick();
				}
			}
		});
		return jb_start;
	}

	/**
	 * 信息栏
	 * 
	 * @return
	 */
	public JLabel getLb_info() {
		lb_info.setFont(new Font("华文楷体", Font.BOLD, 20));
		lb_info.setBounds(111, 101, 450, 53);
		return lb_info;
	}

	/**
	 * 标签游戏选择
	 * 
	 * @return
	 */
	public JLabel getLb_set() {
		lb_set = new JLabel("游戏选择");
		lb_set.setFont(new Font("华文楷体", Font.BOLD, 40));
		lb_set.setBounds(225, 180, 311, 59);
		return lb_set;
	}

	/**
	 * 标签版权
	 * 
	 * @return
	 */
	public JLabel getLb_bottom() {
		lb_bottom = new JLabel("版权所有  违者必究");
		lb_bottom.setFont(new Font("黑体", Font.BOLD, 13));
		lb_bottom.setBounds(252, 574, 235, 20);
		return lb_bottom;
	}

	/**
	 * 画背景
	 */
	public void paintComponent(Graphics g) {
		int x = 0, y = 0;
		ImageIcon icon = new ImageIcon("img/login.png");
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void actionPerformed(ActionEvent e) {

	}
}
