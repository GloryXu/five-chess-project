package com.redgame.view.panel;

import com.redgame.controller.Intelligence;
import com.redgame.entity.User;
import com.redgame.util.GamePanelUtil;
import com.redgame.util.JXLUtil;
import com.redgame.view.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.net.URL;


public class GamePanel extends JPanel implements ActionListener, MouseListener,
		Runnable {

	private static final long serialVersionUID = 1L;

	// 鼠标点击
	boolean canClick = false;

	// 最新的一个棋子
	private Point latestPoint = null;

	// 悔棋数组
	public static Point[] white_chess = new Point[GamePanelUtil.REGRET_COUNT];
	public static Point[] black_chess = new Point[GamePanelUtil.REGRET_COUNT];

	// 记录开始、结束
	int isStart = 1;

	// 玩家分数
	int score = 0;

	// 悔棋实际次数
	int ACTUALLY = -1;
	int chessCount = 0;

	// 登录玩家线程
	private Thread blackThread;

	// 其中内容 0：表示这个点没有棋子 1：表示这个点是黑子 2：表示这个点是白子
	public static int[][] allChess = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];

	// 保存棋子的坐标
	public int X = 0;
	public int Y = 0;

	// 人人对战人机对战
	private String text;

	// 保存当前模式是人机还是人人，true表示人机（默认），false表示人人
	boolean isPeoWithCom = false;

	// 当前下棋标志：黑白
	private ImageIcon BLACK = new ImageIcon("img/black.png");
	private ImageIcon WHITE = new ImageIcon("img/white.png");

	// 荷花
	private ImageIcon flower = new ImageIcon("img/flower.png");

	// 用于开始秒表的变量
	boolean running = false;
	private int[] timeblack = new int[3];
	private int[] timewhite = new int[3];

	// 标识当前应该是白棋还是黑棋
	boolean isBlack = true;

	// 记录每个可放白棋的点的优先大小，数字越大，越优先在这个点放白棋
	int[][] youxian = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
	// 最优先
	int[][] zyouxian = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];

	// 标识当前游戏是否可以继续
	boolean canPlay = true;

	// 当前登录用户
	private User user = null;
	private String name = null;

	private JLabel jlbTimeblack = new JLabel(GamePanelUtil.GAME_TIME);
	private JLabel jlbTimewhite = new JLabel(GamePanelUtil.GAME_TIME);
	private JRadioButton player = new JRadioButton("玩家先走");
	private JRadioButton compu = new JRadioButton("电脑先走");

	public GamePanel(String text, User user) {
		allChess = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
		this.text = text;
		isPeoWithCom = text.equals("人机对战") ? true : false;
		this.user = user;
		this.name = user.getName();
		canClick = false;
		init();
	}

	/**
	 *
	 */
	private void init() {
		this.setLayout(null);
		this.setSize(640, 600);
		//this.add(getCb_degree());
		this.add(getJb_return());
		this.add(getJtfTimeblack());
		this.add(getJtfTimewhite());
		ButtonGroup bg = new ButtonGroup();
		bg.add(player);
		bg.add(compu);
		compu.setSelected(true);
		if (text.equals("人人对战")) {
			//cb_degree.setEnabled(false);
			player.setEnabled(false);
			compu.setEnabled(false);
		}

		this.add(getPlayer());
		this.add(getCompu());
		this.add(getJb_draw());
		this.add(getJb_fail());
		this.add(getJb_help());
		this.add(getJb_regret());
		this.add(getJb_start());
		this.add(getLb_chess(BLACK));
		this.add(getLb_flower(flower));
		this.add(getLb_timeblack());
		this.add(getLb_timewhite());
		setVisible(true);
		this.addMouseListener(this);
	}

	/*********************************** 各种组件 *********************************/
	/**
	 * 先走设置
	 *
	 * @return
	 */
	public JRadioButton getPlayer() {
		player.setFont(new Font("华文楷体", Font.BOLD, 27));
		//player.setBounds(349, 58, 144, 30);
		player.setBounds(315, 37, 144, 30);
		player.setContentAreaFilled(false);
		return player;
	}

	public JRadioButton getCompu() {
		compu.setFont(new Font("华文楷体", Font.BOLD, 27));
		compu.setContentAreaFilled(false);
		//compu.setBounds(349, 10, 144, 30);
		compu.setBounds(165, 37, 144, 30);
		return compu;
	}

	/**
	 * 返回按钮
	 */
	private JButton jb_return = new JButton("返回");

	public JButton getJb_return() {
		jb_return.setBounds(15, 20, 110, 55);
		jb_return.setBorderPainted(false);
		jb_return.setFont(new Font("华文楷体", Font.BOLD, 30));
		jb_return.setContentAreaFilled(false);
		jb_return.addActionListener(this);
		return jb_return;
	}

	/**
	 * 难度等级设置
	 */
	/*private JComboBox cb_degree;

	public JComboBox getCb_degree() {
		String[] str = new String[3];
		str[0] = "初级";
		str[1] = "中级";
		str[2] = "高级";
		cb_degree = new JComboBox(str);
		cb_degree.setBounds(175, 25, 110, 50);
		cb_degree.setFont(new Font("华文楷体", Font.BOLD, 30));
		cb_degree.setOpaque(false);
		if (text.equals("人人对战")) {
			cb_degree.setEnabled(false);
		}
		return cb_degree;
	}*/

	/**
	 * 显示当前棋色
	 */
	private JLabel lb_chess = new JLabel();

	public JLabel getLb_chess(ImageIcon color) {
		lb_chess.setIcon(color);
		lb_chess.setBounds(523, 6, 100, 100);
		lb_chess.setVisible(true);
		return lb_chess;
	}

	/**
	 * 荷花
	 */
	private JLabel lb_flower = new JLabel();

	public JLabel getLb_flower(ImageIcon flower) {
		lb_flower.setIcon(flower);
		lb_flower.setBounds(503, 435, 150, 200);
		if (text.equals("人人对战")) {
			lb_flower.setVisible(false);
		} else {
			lb_flower.setVisible(true);
		}
		return lb_flower;
	}

	/**
	 * 开始按钮
	 */
	private JButton jb_start = new JButton("开始");

	public JButton getJb_start() {
		jb_start.addActionListener(this);
		jb_start.setBounds(522, 115, 100, 54);
		jb_start.setFont(new Font("华文楷体", Font.BOLD, 30));
		jb_start.setBorderPainted(false);
		jb_start.setContentAreaFilled(false);
		return jb_start;
	}

	/**
	 * 平局按钮
	 */
	private JButton jb_draw = new JButton("平局");

	public JButton getJb_draw() {
		jb_draw.setBounds(522, 185, 100, 54);
		jb_draw.setFont(new Font("华文楷体", Font.BOLD, 30));
		jb_draw.setBorderPainted(false);
		jb_draw.setContentAreaFilled(false);
		jb_draw.addActionListener(this);
		return jb_draw;
	}

	/**
	 * 悔棋按钮
	 */
	private JButton jb_regret = new JButton("悔棋");

	public JButton getJb_regret() {
		jb_regret.setBounds(522, 255, 100, 54);
		jb_regret.setFont(new Font("华文楷体", Font.BOLD, 30));
		jb_regret.setBorderPainted(false);
		jb_regret.setContentAreaFilled(false);
		jb_regret.addActionListener(this);
		return jb_regret;
	}

	/**
	 * 认输按钮
	 */
	private JButton jb_fail = new JButton("认输");

	public JButton getJb_fail() {
		jb_fail.setBounds(522, 325, 100, 54);
		jb_fail.setFont(new Font("华文楷体", Font.BOLD, 30));
		jb_fail.setBorderPainted(false);
		jb_fail.setContentAreaFilled(false);
		jb_fail.addActionListener(this);
		return jb_fail;
	}

	/**
	 * 帮助按钮
	 */
	private JButton jb_help = new JButton("帮助");

	public JButton getJb_help() {
		jb_help.setBounds(522, 395, 100, 54);
		jb_help.setFont(new Font("华文楷体", Font.BOLD, 30));
		jb_help.setBorderPainted(false);
		jb_help.setContentAreaFilled(false);
		jb_help.addActionListener(this);
		return jb_help;
	}

	/**
	 * "黑棋时间"显示标签
	 */
	private JLabel lb_timeblack = new JLabel("黑方时间");

	public JLabel getLb_timeblack() {
		lb_timeblack.setBounds(528, 455, 100, 50);
		lb_timeblack.setFont(new Font("华文楷体", Font.BOLD, 20));
		if (text.equals("人机对战"))
			lb_timeblack.setVisible(false);
		return lb_timeblack;
	}

	/**
	 * 黑棋时间显示文本框
	 */
	public JLabel getJtfTimeblack() {

		jlbTimeblack.setBounds(535, 492, 75, 30);
		jlbTimeblack.setFont(new Font("华文楷体", Font.BOLD, 20));
		if (text.equals("人机对战"))
			jlbTimeblack.setVisible(false);
		return jlbTimeblack;
	}

	/**
	 * "白方时间"显示标签
	 */
	private JLabel lb_timewhite = new JLabel("白方时间");

	public JLabel getLb_timewhite() {
		lb_timewhite.setBounds(528, 525, 100, 50);
		lb_timewhite.setFont(new Font("华文楷体", Font.BOLD, 20));
		if (text.equals("人机对战"))
			lb_timewhite.setVisible(false);
		return lb_timewhite;
	}

	/**
	 * 白棋时间显示文本框
	 */
	public JLabel getJtfTimewhite() {
		jlbTimewhite.setBounds(535, 562, 75, 30);
		jlbTimewhite.setFont(new Font("华文楷体", Font.BOLD, 20));
		if (text.equals("人机对战"))
			jlbTimewhite.setVisible(false);
		return jlbTimewhite;
	}

	/***************************************** 背景 ***********************************/

	public void paintComponent(Graphics g) {
		if (text.equals("人人对战")) {
			if (isStart == 1) {
				jb_start.setText("开始");
				this.getLb_chess(BLACK);
				latestPoint = null;
				jlbTimeblack.setText(GamePanelUtil.GAME_TIME);
				jlbTimewhite.setText(GamePanelUtil.GAME_TIME);
				allChess = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
			} else {
				jb_start.setText("结束");
			}
			if (isBlack) {
				this.getLb_chess(BLACK);
			} else {
				this.getLb_chess(WHITE);
			}
		} else {
			if (isStart == 1) {
				jb_start.setText("开始");
				allChess = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
				//this.cb_degree.setEnabled(true);
				this.player.setEnabled(true);
				this.compu.setEnabled(true);
			} else {
				jb_start.setText("结束");
				if (isPCfirstGo() && chessCount == 0) {
					chessCount++;
					int px = (GamePanelUtil.COUNTLINES - 1) / 2;
					allChess[px][px] = 2;
					Point p = new Point(px, px);
					latestPoint = p;
					GamePanelUtil.whiteAddChess(p);
					GamePanelUtil.PlayMusic(GamePanelUtil.CHESS);
				}
				//this.cb_degree.setEnabled(false);
				this.player.setEnabled(false);
				this.compu.setEnabled(false);
			}
		}
		int x = 0, y = 0;
		ImageIcon icon = new ImageIcon("img/chess.png");
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
		// 绘制棋子
		for (int i = 0; i < GamePanelUtil.COUNTLINES; i++) {
			for (int j = 0; j < GamePanelUtil.COUNTLINES; j++) {
				// 黑子
				if (allChess[i][j] == 1) {
					int tempX = i * GamePanelUtil.LENGTH
							+ GamePanelUtil.FIRST_POSITION_X;
					int tmpY = j * GamePanelUtil.LENGTH
							+ GamePanelUtil.FIRST_POSITION_Y;
					g.setColor(Color.BLACK);
					Graphics2D g2d = (Graphics2D) g;
					RadialGradientPaint paint = new RadialGradientPaint(tempX
							- GamePanelUtil.DIAMETER / 2, tmpY
							- GamePanelUtil.DIAMETER / 2, 20, new float[] { 0f,
							1f }, new Color[] { Color.WHITE, Color.BLACK });
					g2d.setPaint(paint);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setRenderingHint(
							RenderingHints.KEY_ALPHA_INTERPOLATION,
							RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
					Ellipse2D e = new Ellipse2D.Float(tempX
							- GamePanelUtil.DIAMETER / 2, tmpY
							- GamePanelUtil.DIAMETER / 2,
							GamePanelUtil.DIAMETER, GamePanelUtil.DIAMETER);
					g2d.fill(e);

				}
				// 白子
				if (allChess[i][j] == 2) {
					int tempX = i * GamePanelUtil.LENGTH
							+ GamePanelUtil.FIRST_POSITION_X;
					int tmpY = j * GamePanelUtil.LENGTH
							+ GamePanelUtil.FIRST_POSITION_Y;
					g.setColor(Color.WHITE);
					Graphics2D g2d = (Graphics2D) g;
					RadialGradientPaint paint = new RadialGradientPaint(tempX
							- GamePanelUtil.DIAMETER / 2, tmpY
							- GamePanelUtil.DIAMETER / 2, 100, new float[] {
							0f, 1f }, new Color[] { Color.WHITE, Color.BLACK });
					g2d.setPaint(paint);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setRenderingHint(
							RenderingHints.KEY_ALPHA_INTERPOLATION,
							RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
					Ellipse2D e = new Ellipse2D.Float(tempX
							- GamePanelUtil.DIAMETER / 2, tmpY
							- GamePanelUtil.DIAMETER / 2,
							GamePanelUtil.DIAMETER, GamePanelUtil.DIAMETER);
					g2d.fill(e);
					// GamePanelUtil.PlayChessMusic();
				}
			}
		}

		// 画红框
		if (null != latestPoint) {
			g.setColor(Color.red);
			int i = (int) latestPoint.getX();
			int j = (int) latestPoint.getY();
			int redPoint_X = i * GamePanelUtil.LENGTH
					+ GamePanelUtil.FIRST_POSITION_X - GamePanelUtil.DIAMETER
					/ 2;
			int redPoint_Y = j * GamePanelUtil.LENGTH
					+ GamePanelUtil.FIRST_POSITION_Y - GamePanelUtil.DIAMETER
					/ 2;
			g.drawRect(redPoint_X, redPoint_Y, GamePanelUtil.DIAMETER,
					GamePanelUtil.DIAMETER);
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 是否电脑先走
	 *
	 * @return
	 */
	public boolean isPCfirstGo() {
		return compu.isSelected() ? true : false;
	}

	/****************************************** 按钮监听 ****************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		// 返回按钮
		if (obj == jb_return) {
			if (text.equals("人机对战")) {
				if (isStart == -1) {
					GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
					int i = JOptionPane.showConfirmDialog(null,
							"游戏还在继续，确定强制返回？");
					if (i == 0) {
						score = JXLUtil.getScore(name);
						JXLUtil.updateEscape(name, score);
						MainFrame.mf.setContentPane(MainFrame.mf.getLp(user));
					} else {
						// GamePanelUtil.PlayWarnMusic();
						return;
					}
				} else {
					GamePanelUtil.PlayMusic(GamePanelUtil.CLICK);
					MainFrame.mf.setContentPane(MainFrame.mf.getLp(user));
				}
			} else {
				GamePanelUtil.PlayMusic(GamePanelUtil.CLICK);
				MainFrame.mf.setContentPane(MainFrame.mf.getLp(user));
			}

		}
		// 开始按钮
		if (obj == jb_start) {
			if (isStart == -1) {
				if (text.equals("人机对战")) {
					GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
					int i = JOptionPane.showConfirmDialog(null, "确定强制结束？");
					if (i == 0) {
						ACTUALLY = -1;
						isStart = -1 * isStart;
						score = JXLUtil.getScore(name);
						JXLUtil.updateEscape(name, score);
						chessCount = 0;
						latestPoint = null;
						this.repaint();
						return;
					} else {
						return;
					}
				} else {
					canPlay = false;
					try {
						blackThread.interrupt();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					ACTUALLY = -1;
					isStart = -1 * isStart;
					isBlack = true;
					this.repaint();
					return;
				}
			} else {
				if (text.equals("人人对战")) {
					running = true;
					canPlay = true;
					blackThread = new Thread(this);
					blackThread.start();
					isStart = -1 * isStart;
					this.repaint();
					return;
				} else {
					running = true;
					canPlay = true;
					blackThread = new Thread(this);
					blackThread.start();
					isStart = -1 * isStart;
					this.repaint();
					return;
				}
			}
		}
		// 平局按钮
		if (obj == jb_draw) {
			if (isStart == 1) {
				GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
				JOptionPane.showMessageDialog(null, "对不起，请先开始！");
				return;
			}
			if (text.equals("人机对战")) {
				GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
				JOptionPane.showMessageDialog(null, "对不起，机器不同意平局！");
			} else {
				int result_Draw = JOptionPane.showConfirmDialog(null,
						"您的对手同意吗？");
				if (result_Draw == 0) {
					isStart = -1 * isStart;
					running = false;
					isBlack = true;
					this.repaint();
				}
			}
		}
		// 悔棋按钮
		if (obj == jb_regret) {
			ACTUALLY++;
			if (ACTUALLY >= GamePanelUtil.REGRET_COUNT) {
				GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
				JOptionPane.showMessageDialog(null, "对不起每局只能悔5次！");
				return;
			}
			Point p;
			if (isStart == 1) {
				GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
				JOptionPane.showMessageDialog(null, "对不起，请先开始！");
				return;
			} else {
				if (text.equals("人机对战")) {
					GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
					int result_regret = JOptionPane.showConfirmDialog(null,
							"确认悔棋?");
					if (result_regret == 0) {
						p = GamePanelUtil.getBlackPoint();
						if (null != p) {
							allChess[p.x][p.y] = 0;
						}
						p = GamePanelUtil.getWhitePoint();
						if (null != p) {
							allChess[p.x][p.y] = 0;
						}
						latestPoint = GamePanelUtil.getJustWhitePoint();
						this.repaint();
					}
				} else {
					GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
					int result_regret = JOptionPane.showConfirmDialog(null,
							"确认悔棋?");
					if (result_regret == 0) {
						if (isBlack) {
							p = GamePanelUtil.getWhitePoint();
							if (null != p)
								allChess[p.x][p.y] = 0;
							isBlack = false;
							latestPoint = GamePanelUtil.getJustBlackPoint();
							this.repaint();
						} else {
							p = GamePanelUtil.getBlackPoint();
							if (null != p)
								allChess[p.x][p.y] = 0;
							isBlack = true;
							latestPoint = GamePanelUtil.getJustWhitePoint();
							this.repaint();
						}
					}
				}
			}
		}
		// 认输按钮
		if (obj == jb_fail) {
			if (isStart == 1) {
				GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
				JOptionPane.showMessageDialog(null, "对不起，请先开始！");
				return;
			}
			GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
			int result_Fail = JOptionPane.showConfirmDialog(null,
					"还有再战之力，确定认输？");
			if (result_Fail == 0) {
				if (text.equals("人机对战")) {
					score = JXLUtil.getScore(name);
					JXLUtil.updateFail(name, score);
					isStart = -1 * isStart;
					latestPoint = null;
					chessCount = 0;
					this.repaint();
					return;
				} else {
					isStart = -1 * isStart;
					this.repaint();
					return;
				}
			}
		}
		// 帮助按钮
		if (obj == jb_help) {
			JOptionPane.showMessageDialog(null, GamePanelUtil.HELP);
		}
	}

	private String formatTime(int[] times2) {
		String strm = "", strs = "", strss = "";
		strm = String.format("%02d", times2[0]);
		strs = String.format("%02d", times2[1]);
		strss = String.format("%02d", times2[2]);
		return strm + ":" + strs + ":" + strss;
	}

	/**
	 * 时间的变化
	 *
	 * @param a
	 * @return
	 */
	public void setTimes(int[] a) {
		if (a[2] == 0) {
			if (a[1] == 0) {
				if (a[0] == 0) {
					return;
				}
				a[0]--;
				a[1] = 60;
			}
			a[1]--;
			a[2] = 10;
		}
		a[2]--;
	}

	/****************************************** 鼠标监听 ******************************************/

	public void mousePressed(MouseEvent e) {
		X = e.getX();
		Y = e.getY();
		if (isStart == 1 && X >= GamePanelUtil.CHESS_LEFT_X
				&& X <= GamePanelUtil.CHESS_RIGHT_X
				&& Y >= GamePanelUtil.CHESS_UP_Y
				&& Y <= GamePanelUtil.CHESS_DOWN_Y) {
			GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
			JOptionPane.showMessageDialog(null, "请先开始！");
			return;
		} else {
			if (canPlay == true) {
				if (X >= GamePanelUtil.CHESS_LEFT_X
						&& X <= GamePanelUtil.CHESS_RIGHT_X
						&& Y >= GamePanelUtil.CHESS_UP_Y
						&& Y <= GamePanelUtil.CHESS_DOWN_Y) {
					// 判断鼠标点击的位置离哪个交叉点最近。
					double dx = (double) (X - GamePanelUtil.FIRST_POSITION_X)
							/ GamePanelUtil.LENGTH;
					double dy = (double) (Y - GamePanelUtil.FIRST_POSITION_Y)
							/ GamePanelUtil.LENGTH;
					X = (int) Math.round(dx);
					Y = (int) Math.round(dy);
					// 判断当前要下的是什么棋子
					if (isPeoWithCom) {
						if (allChess[X][Y] == 0) {
							allChess[X][Y] = 1;
							Point p1 = new Point(X, Y);
							latestPoint = p1;
							GamePanelUtil.blackAddChess(p1);
							GamePanelUtil.PlayMusic(GamePanelUtil.CHESS);
							this.getLb_chess(WHITE);
							boolean winFlag = this.checkWin1(p1);
							if (winFlag) {
								canPlay = false;
								isStart = -1 * isStart;
								ACTUALLY = -1;
								JOptionPane.showMessageDialog(this, "游戏结束"
										+ (allChess[X][Y] == 2 ? "电脑" : name)
										+ "获胜！");
								score = JXLUtil.getScore(name);
								if ((allChess[X][Y] == 2 ? "电脑" : name)
										.equals(name)) {
									JXLUtil.updateSuccess(name, score);
								} else {
									JXLUtil.updateFail(name, score);
								}
								chessCount = 0;
								latestPoint = null;
								allChess = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
							}
							if (canPlay) {
								allChess[X][Y] = 1;
								Point p = new Point();
								Intelligence intel = new Intelligence();
								p = intel.complay();
								latestPoint = p;
								GamePanelUtil.whiteAddChess(p);
								GamePanelUtil.PlayMusic(GamePanelUtil.CHESS);
								winFlag = checkWin1(p);
								if (winFlag) {
									canPlay = false;
									isStart = -1 * isStart;
									ACTUALLY = -1;
									score = JXLUtil.getScore(name);
									if ((allChess[X][Y] == 2 ? "电脑" : name)
											.equals(name)) {
										JXLUtil.updateSuccess(name, score);
									} else {
										JXLUtil.updateFail(name, score);
									}
									JOptionPane.showMessageDialog(this, "游戏结束"
											+ (allChess[X][Y] == 2 ? "电脑"
											: name) + "获胜！");
									allChess = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
									chessCount = 0;
									latestPoint = null;
								}
							}
							this.getLb_chess(BLACK);
							this.repaint();
							canPlay = true;
						} else {
							GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
							JOptionPane.showMessageDialog(this, "已经有棋子，请重新落子！");
						}
					} else {

						/**************************** 人人对战 *************************************/

						if (allChess[X][Y] == 0) {
							if (isBlack) {
								allChess[X][Y] = 1;
								Point p = new Point(X, Y);
								GamePanelUtil.blackAddChess(p);
								GamePanelUtil.PlayMusic(GamePanelUtil.CHESS);
								latestPoint = p;
								isBlack = false;
							} else {
								allChess[X][Y] = 2;
								Point p = new Point(X, Y);
								GamePanelUtil.whiteAddChess(p);
								GamePanelUtil.PlayMusic(GamePanelUtil.CHESS);
								latestPoint = p;
								isBlack = true;
							}
							// 判断这个棋子是否和其他的棋子连城5个，即判断游戏是否结束
							boolean winFlag = this.checkWin();
							if (winFlag) {
								isStart = -1 * isStart;
								running = false;
								canPlay = false;
								isBlack = true;
								this.getLb_chess(BLACK);
								ACTUALLY = -1;
								JOptionPane.showMessageDialog(this, "游戏结束"
										+ (allChess[X][Y] == 1 ? "黑方" : "白方")
										+ "获胜!");
								latestPoint = null;
								allChess = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
								this.repaint();
							}
							this.repaint();
						} else {
							GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
							JOptionPane.showMessageDialog(this,
									"当前位置已经有棋子，请重新下");
						}
					}
				}
			}
		}
	}

	private boolean checkWin1(Point p) {
		boolean flag = false;
		// 判断横向的是否有5个棋子相连。特点：纵坐标是相同的。即allChess[x][y]中y的值相同
		X = p.x;
		Y = p.y;
		int color = allChess[X][Y];

		// 判断横向
		int count = 1;
		count = this.checkCount(1, 0, color);
		if (count >= 5) {
			flag = true;
		} else {// 判断纵向
			count = this.checkCount(0, 1, color);
			if (count >= 5) {
				flag = true;
			} else {// 判断右上、左下
				count = this.checkCount(1, -1, color);
				if (count >= 5) {
					flag = true;
				} else {// 判断右下、左下
					count = this.checkCount(1, 1, color);
					if (count >= 5) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	// 判断棋子连接的数量
	private int checkCount(int xChange, int yChange, int color) {
		int count = 1;
		int tempX = xChange;
		int tempY = yChange;
		while (X + xChange >= 0 && X + xChange <= GamePanelUtil.COUNTLINES - 1
				&& Y + yChange >= 0
				&& Y + yChange <= GamePanelUtil.COUNTLINES - 1
				&& color == allChess[X + xChange][Y + yChange]) {
			count++;

			if (xChange != 0) {
				xChange++;
			}
			if (yChange != 0) {
				if (yChange > 0)
					yChange++;
				else {
					yChange--;
				}
			}
		}
		xChange = tempX;
		yChange = tempY;
		while (X - xChange >= 0 && X - xChange <= GamePanelUtil.COUNTLINES - 1
				&& Y - yChange >= 0
				&& Y - yChange <= GamePanelUtil.COUNTLINES - 1
				&& color == allChess[X - xChange][Y - yChange]) {
			count++;

			if (xChange != 0) {
				xChange++;
			}
			if (yChange != 0) {
				if (yChange > 0)
					yChange++;
				else {
					yChange--;
				}
			}
		}
		return count;
	}

	private boolean checkWin() {
		boolean flag = false;
		// 判断横向的是否有5个棋子相连。特点：纵坐标是相同的。即allChess[x][y]中y的值相同
		int color = allChess[X][Y];
		// 判断横向
		int count = 1;
		count = this.checkCount(1, 0, color);
		if (count >= 5) {
			flag = true;
		} else {// 判断纵向
			count = this.checkCount(0, 1, color);
			if (count >= 5) {
				flag = true;
			} else {// 判断右上、左下
				count = this.checkCount(1, -1, color);
				if (count >= 5) {
					flag = true;
				} else {// 判断右下、左下
					count = this.checkCount(1, 1, color);
					if (count >= 5) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void run() {
		if (text.equals("人人对战")) {
			String[] strTime = GamePanelUtil.GAME_TIME.split(":");
			timeblack[0] = Integer.parseInt(strTime[0]);
			timeblack[1] = Integer.parseInt(strTime[1]);
			timeblack[2] = Integer.parseInt(strTime[2]);
			timewhite[0] = Integer.parseInt(strTime[0]);
			timewhite[1] = Integer.parseInt(strTime[1]);
			timewhite[2] = Integer.parseInt(strTime[2]);
			while (running) {
				try {
					Thread.sleep(100);
					if (isBlack) {
						setTimes(timeblack);
						jlbTimeblack.setText(formatTime(timeblack));
						if (timeblack[0] == 0 && timeblack[1] == 0
								&& timeblack[2] == 0) {
							JOptionPane.showMessageDialog(null, "黑方超时判负！");
							isBlack = true;
							isStart = -1 * isStart;
							allChess = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
							this.repaint();
							break;
						}
					} else {
						setTimes(timewhite);
						jlbTimewhite.setText(formatTime(timewhite));
						if (timewhite[0] == 0 && timewhite[1] == 0
								&& timewhite[2] == 0) {
							JOptionPane.showMessageDialog(null, "白方超时判负！");
							isBlack = true;
							isStart = -1 * isStart;
							allChess = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
							this.repaint();
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
