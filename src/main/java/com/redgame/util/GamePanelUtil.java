package com.redgame.util;

import java.awt.Point;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import com.redgame.view.panel.GamePanel;


public class GamePanelUtil {
	/**
	 * 五子棋棋盘大小
	 */
	public static final int COUNTLINES = 15;

	/**
	 * 第一个位置的X坐标
	 */
	public static final int FIRST_POSITION_X = 20;

	/**
	 * 棋盘宽度
	 */
	public static final int LENGTH = 33;

	/**
	 * 第一个位置的Y坐标
	 */
	public static final int FIRST_POSITION_Y = 118;

	/**
	 * 棋子直径
	 */
	public static final int DIAMETER = 24;

	/**
	 * 一局时间
	 */
	public static final String GAME_TIME = "15:00:00";

	/**
	 * 棋盘左侧X值
	 */
	public static final int CHESS_LEFT_X = 1;

	/**
	 * 棋盘右侧X值
	 */
	public static final int CHESS_RIGHT_X = 499;

	/**
	 * 棋盘上侧Y值
	 */
	public static final int CHESS_UP_Y = 99;

	/**
	 * 棋盘下侧Y值
	 */
	public static final int CHESS_DOWN_Y = 594;

	/**
	 * 输所扣除的分数
	 */
	public static final int FAIL = 1;

	/**
	 * 赢所加上的分数
	 */
	public static final int SUCCESS = 2;

	/**
	 * 逃跑扣分
	 */
	public static final int ESCAPE = 5;

	/**
	 * 路径
	 */
	public static final String FILEPATH = "user.xls";

	/**
	 * 悔棋次数
	 */
	public static final int REGRET_COUNT = 5;

	public static final String HELP = "一、人机游戏：负一局减一分，胜一局加两分，逃跑减五分;\n"+
			"二、人人游戏：胜负、逃跑、平局等不计分。";

	/**
	 * 记录黑棋
	 * 
	 * @param p
	 */
	public static void blackAddChess(Point p) {
		int i, j = 0;
		for (i = 0; i < REGRET_COUNT; i++) {
			if (null == GamePanel.black_chess[i]) {
				GamePanel.black_chess[i] = p;
				break;
			}
		}
		if (i == REGRET_COUNT) {
			for (j = 1; j < REGRET_COUNT; j++) {
				GamePanel.black_chess[j - 1] = GamePanel.black_chess[j];
			}
			GamePanel.black_chess[REGRET_COUNT - 1] = p;
		}
	}

	/**
	 * 记录白棋
	 * 
	 * @param p
	 */
	public static void whiteAddChess(Point p) {
		int i, j = 0;
		for (i = 0; i < REGRET_COUNT; i++) {
			if (null == GamePanel.white_chess[i]) {
				GamePanel.white_chess[i] = p;
				break;
			}
		}
		if (i == REGRET_COUNT) {
			for (j = 1; j < REGRET_COUNT; j++) {
				GamePanel.white_chess[j - 1] = GamePanel.white_chess[j];
			}
			GamePanel.white_chess[REGRET_COUNT - 1] = p;
		}
	}

	/**
	 * 获取黑棋最新下棋点
	 * 
	 * @return
	 */
	public static Point getBlackPoint() {
		Point p = null;
		int i = 0;
		for (i = REGRET_COUNT - 1; i >= 0; i--) {
			if (null != GamePanel.black_chess[i]) {
				p = GamePanel.black_chess[i];
				GamePanel.black_chess[i] = null;
				break;
			}
		}
		if (i == -1) {
			JOptionPane.showMessageDialog(null, "对不起，您还未下棋！");
			System.out.println("black");
		}
		return p;
	}

	/**
	 * 获取白棋最新下棋点
	 * 
	 * @return
	 */
	public static Point getWhitePoint() {
		Point p = null;
		int i = 0;
		for (i = REGRET_COUNT - 1; i >= 0; i--) {
			if (null != GamePanel.white_chess[i]) {
				p = GamePanel.white_chess[i];
				GamePanel.white_chess[i] = null;
				return p;
			}
		}
		if (i == -1) {
			JOptionPane.showMessageDialog(null, "对不起，您还未下棋！");
			System.out.println("white");
		}
		return null;
	}

	/**
	 * 获取最新的白点，不置空
	 * 
	 * @return
	 */
	public static Point getJustWhitePoint() {
		Point p = null;
		int i = 0;
		for (i = REGRET_COUNT - 1; i >= 0; i--) {
			if (null != GamePanel.white_chess[i]) {
				p = GamePanel.white_chess[i];
				break;
			}
		}
		/*
		 * if(i==-1){ JOptionPane.showMessageDialog(null, "对不起，您还未下棋！"); }
		 */
		return p;
	}

	/**
	 * 获得最新的黑棋，不置空
	 * 
	 * @return
	 */
	public static Point getJustBlackPoint() {
		Point p = null;
		int i = 0;
		for (i = REGRET_COUNT - 1; i >= 0; i--) {
			if (null != GamePanel.black_chess[i]) {
				p = GamePanel.black_chess[i];
				break;
			}
		}
		/*
		 * if(i==-1){ JOptionPane.showMessageDialog(null, "对不起，您还未下棋！"); }
		 */
		return p;
	}

	/**
	 * 播放下棋音乐
	 */
	public static String CLICK = "music/click.wav";
	public static String CHESS = "music/chess.wav";
	public static String WARN = "music/warn.wav";

	public static void PlayMusic(String music) {
		try {
			File wavFile = new File(music);// 可以使用文件
			// InputStream in = new FileInputStream(wavFile);//也可以使用流
			// URL url = wavFile.toURI().toURL();//还可以使用URL
			AudioInputStream ais = AudioSystem.getAudioInputStream(wavFile);// 这里使用上面的三种，那种都可以
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("失败咯！");
		}
	}
}
