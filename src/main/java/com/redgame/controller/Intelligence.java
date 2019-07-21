package com.redgame.controller;

import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

import com.redgame.util.GamePanelUtil;
import com.redgame.view.panel.GamePanel;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 
 * @author Administrator
 */
public class Intelligence {
	public Intelligence() {
		setMyTypeTable();
		setEnemyTypeTable();
	}

	void initTable() {
		for (int r = 0; r < GamePanelUtil.COUNTLINES; r++) {
			for (int c = 0; c < GamePanelUtil.COUNTLINES; c++) {
				computer[r][c] = 0;
				player[r][c] = 0;
			}
		}
	}

	// 搜索算法
	// 构造棋型表，每种棋型对应的分数
	// int[左边空白格][左边己方子数][右边己方子数][右边空白格] MyTable
	int[][][][] MyTable = new int[2][5][5][2];// 己方棋型表
	int[][][][] EnemyTable = new int[2][5][5][2];// 己方棋型表

	// 先手F，后手L，冲C，活H，死S
	// S1=S2=S3=S4=4
	// FC1=LC1<FH1=LH1<FC2=FH2<FC2<FH2<FH2<FC3<FC4<FH3<FH4
	// 己方放子后，即后手

	void setMyTypeTable() {
		for (int m = 0; m < 2; m++) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					for (int n = 0; n < 2; n++) {
						// 死子，两边都无空格且无己方子
						if (m + n == 0 && i + j == 0) {
							MyTable[m][i][j][n] = 4;
						} else if (i + j >= 5) {// 禁手
							MyTable[m][i][j][n] = -1250000;

						}// 成五
						else if (i + j == 4) {
							MyTable[m][i][j][n] = 1250000;
						} // 成四
						else if (i + j == 3) {
							if (m == 1 && n == 1) {// 成活四
								MyTable[m][i][j][n] = 300000;
							} else if (m + n == 0) {// 成死四
								MyTable[m][i][j][n] = 4;
							} else if (m == 0 || n == 0) {
								MyTable[m][i][j][n] = 10000;// 成冲四
							}
						} // 成三
						else if (i + j == 2) {
							if (m == 1 && n == 1) {// 成活三
								MyTable[m][i][j][n] = 50000;
							} else if (m + n == 0) {// 成死三
								MyTable[m][i][j][n] = 4;
							} else if (m == 0 || n == 0) {
								MyTable[m][i][j][n] = 10000;// 成冲三
							}
						} // 成二
						else if (i + j == 1) {
							if (m == 1 && n == 1) {// 成活二
								MyTable[m][i][j][n] = 2500;
							} else if (m + n == 0) {// 成死二
								MyTable[m][i][j][n] = 4;
							} else if (m == 0 || n == 0) {
								MyTable[m][i][j][n] = 500;// 成冲二
							}
						} // 成一
						else if (i + j == 0) {
							if (m == 1 && n == 1) {// 成活一
								MyTable[m][i][j][n] = 100;
							} else if (m + n == 0) {// 成死一
								MyTable[m][i][j][n] = 4;
							} else if (m == 0 || n == 0) {
								MyTable[m][i][j][n] = 20;// 成冲一
							}
						}
					}
				}
			}
		}
	}

	// 敌方放子后，即先手
	void setEnemyTypeTable() {
		for (int m = 0; m < 2; m++) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					for (int n = 0; n < 2; n++) {

						// 死子，两边都无空格且无己方子
						if (m + n == 0 && i + j == 0) {
							EnemyTable[m][i][j][n] = 4;
						} else if (i + j >= 5) {// 禁手
							MyTable[m][i][j][n] = -5000000;

						}// 成五
						else if (i + j == 4) {
							EnemyTable[m][i][j][n] = 1230000;
						} // 成四
						else if (i + j == 3) {
							if (m == 1 && n == 1) {// 成活四
								EnemyTable[m][i][j][n] = 250000;
							} else if (m + n == 0) {// 成死四
								EnemyTable[m][i][j][n] = 4;
							} else if (m == 0 || n == 0) {
								EnemyTable[m][i][j][n] = 40000;// 成冲四
							}
						} // 成三
						else if (i + j == 2) {
							if (m == 1 && n == 1 && i == 1 && j == 1) {
								EnemyTable[m][i][j][n] = 66000;
							} else if (m == 1 && n == 1) {// 成活三
								EnemyTable[m][i][j][n] = 55000;
							} else if (m + n == 0) {// 成死三
								EnemyTable[m][i][j][n] = 4;
							} else if (m == 0 || n == 0) {
								EnemyTable[m][i][j][n] = 10000;// 成冲三
							}
						} // 成二
						else if (i + j == 1) {
							if (m == 1 && n == 1) {// 成活二
								EnemyTable[m][i][j][n] = 2500;
							} else if (m + n == 0) {// 成死二
								EnemyTable[m][i][j][n] = 4;
							} else if (m == 0 || n == 0) {
								EnemyTable[m][i][j][n] = 500;// 成冲二
							}
						} // 成一
						else if (i + j == 0) {
							if (m == 1 && n == 1) {// 成活一
								EnemyTable[m][i][j][n] = 100;
							} else if (m + n == 0) {// 成死一
								EnemyTable[m][i][j][n] = 4;
							} else if (m == 0 || n == 0) {
								EnemyTable[m][i][j][n] = 20;// /成冲一
							}
						}
					}
				}
			}
		}
	}

	int[][] board = GamePanel.allChess;// 1为玩家，2为电脑
	// 棋盘分析表（分值表）
	int computer[][] = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];// 白子
	int player[][] = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];// 黑子

	// 构造电脑和玩家的棋盘分析表
	void analyseComputer(int[][] computer, int[][] board) {

		for (int r = 0; r < GamePanelUtil.COUNTLINES; r++) {
			for (int c = 0; c < GamePanelUtil.COUNTLINES; c++) {

				int left = 0;
				int right = 0;
				int lblank = 0;
				int rblank = 0;
				if (board[r][c] == 0) {
					// 如果有空位，八个方向查找，每个方向查找4步
					computer[r][c] = 0;
					// 下
					for (int i = c + 1; i <= c + 4 && i < GamePanelUtil.COUNTLINES; i++) {
						if (board[r][i] == 0) {
							lblank++;
							break;
						} else if (this.board[r][i] == 2) {
							left++;
						} else if (this.board[r][i] == 1) {
							break;
						}
					}
					// 上
					for (int j = c - 1; j >= c - 4 && j >= 0; j--) {
						if (board[r][j] == 0) {
							rblank++;
							break;
						} else if (this.board[r][j] == 2) {
							right++;
						} else if (this.board[r][j] == 1) {
							break;
						}
					}
					computer[r][c] += this.MyTable[lblank][left][right][rblank];

					rblank = 0;
					lblank = 0;
					left = 0;
					right = 0;
					// 左
					for (int j = r - 1; j >= r - 4 && j >= 0; j--) {
						if (board[j][c] == 0) {
							lblank++;
							break;
						} else if (this.board[j][c] == 2) {
							left++;
						} else if (this.board[j][c] == 1) {
							break;
						}
					}
					// 右
					for (int i = r + 1; i <= r + 4 && i < GamePanelUtil.COUNTLINES; i++) {
						if (board[i][c] == 0) {
							rblank++;
							break;
						} else if (this.board[i][c] == 2) {
							right++;
						} else if (this.board[i][c] == 1) {
							break;
						}
					}
					computer[r][c] += this.MyTable[lblank][left][right][rblank];

					rblank = 0;
					lblank = 0;
					left = 0;
					right = 0;
					// 左上
					int i = r - 1;
					int j = c - 1;
					for (; i >= r - 4 && i >= 0 && j >= c - 4 && j >= 0; i--, j--) {
						if (board[i][j] == 0) {
							lblank++;
							break;
						} else if (this.board[i][j] == 2) {
							left++;
						} else if (this.board[i][j] == 1) {
							break;
						}
					}

					// 右下
					i = r + 1;
					j = c + 1;
					for (; i <= r + 4 && i < GamePanelUtil.COUNTLINES && j <= c + 4 && j < GamePanelUtil.COUNTLINES; i++, j++) {
						if (board[i][j] == 0) {
							rblank++;
							break;
						} else if (this.board[i][j] == 2) {
							right++;
						} else if (this.board[i][j] == 1) {
							break;
						}
					}
					computer[r][c] += this.MyTable[lblank][left][right][rblank];

					rblank = 0;
					lblank = 0;
					left = 0;
					right = 0;
					// 右上
					i = r + 1;
					j = c - 1;
					for (; i <= r + 4 && i < GamePanelUtil.COUNTLINES && j >= c - 4 && j >= 0; i++, j--) {
						if (board[i][j] == 0) {
							rblank++;
							break;
						} else if (this.board[i][j] == 2) {
							right++;
						} else if (this.board[i][j] == 1) {
							break;
						}
					}
					// 左下
					i = r - 1;
					j = c + 1;
					for (; i >= r - 4 && i > 0 && j <= c + 4 && j < GamePanelUtil.COUNTLINES; i--, j++) {
						if (board[i][j] == 0) {
							lblank++;
							break;
						} else if (this.board[i][j] == 2) {
							left++;
						} else if (this.board[i][j] == 1) {
							break;
						}
					}
					computer[r][c] += this.MyTable[lblank][left][right][rblank];

				} else if (board[r][c] != 0) {
					computer[r][c] = 0;
				}
			}
		}

	}

	void analysePlayer(int[][] player, int[][] board) {

		for (int r = 0; r < GamePanelUtil.COUNTLINES; r++) {
			for (int c = 0; c < GamePanelUtil.COUNTLINES; c++) {
				int left = 0;
				int right = 0;
				int lblank = 0;
				int rblank = 0;
				if (board[r][c] == 0) {
					// 如果有空位，八个方向查找，每个方向查找4步
					player[r][c] = 0;
					// 下
					for (int i = c + 1; i <= c + 4 && i < GamePanelUtil.COUNTLINES; i++) {
						if (board[r][i] == 0) {
							lblank++;
							break;
						} else if (this.board[r][i] == 1) {
							left++;
						} else if (this.board[r][i] == 2) {
							break;
						}
					}
					// 上
					for (int j = c - 1; j >= c - 4 && j >= 0; j--) {
						if (board[r][j] == 0) {
							rblank++;
							break;
						} else if (this.board[r][j] == 1) {
							right++;
						} else if (this.board[r][j] == 2) {
							break;
						}
					}

					player[r][c] += EnemyTable[lblank][left][right][rblank];
					rblank = 0;
					lblank = 0;
					left = 0;
					right = 0;
					// 左
					for (int j = r - 1; j >= r - 4 && j >= 0; j--) {
						if (board[j][c] == 0) {
							lblank++;
							break;
						} else if (this.board[j][c] == 1) {
							left++;
						} else if (this.board[j][c] == 2) {
							break;
						}
					}
					// 右
					for (int i = r + 1; i <= r + 4 && i < GamePanelUtil.COUNTLINES; i++) {
						if (board[i][c] == 0) {
							rblank++;
							break;
						} else if (this.board[i][c] == 1) {
							right++;
						} else if (this.board[i][c] == 2) {
							break;
						}
					}
					player[r][c] += EnemyTable[lblank][left][right][rblank];

					rblank = 0;
					lblank = 0;
					left = 0;
					right = 0;
					// 左上
					int i = r - 1;
					int j = c - 1;
					for (; i >= r - 4 && i >= 0 && j >= c - 4 && j >= 0; i--, j--) {
						if (board[i][j] == 0) {
							lblank++;
							break;
						} else if (this.board[i][j] == 1) {
							left++;
						} else if (this.board[i][j] == 2) {
							break;
						}
					}
					// 右下
					i = r + 1;
					j = c + 1;
					for (; i <= r + 4 && i < GamePanelUtil.COUNTLINES && j <= c + 4 && j < GamePanelUtil.COUNTLINES; i++, j++) {
						if (board[i][j] == 0) {
							rblank++;
							break;
						} else if (this.board[i][j] == 1) {
							right++;
						} else if (this.board[i][j] == 2) {
							break;
						}
					}
					player[r][c] += EnemyTable[lblank][left][right][rblank];
					rblank = 0;
					lblank = 0;
					left = 0;
					right = 0;
					// 右上
					i = r + 1;
					j = c - 1;
					for (; i <= r + 4 && i < GamePanelUtil.COUNTLINES && j >= c - 4 && j >= 0; i++, j--) {
						if (board[i][j] == 0) {
							rblank++;
							break;
						} else if (this.board[i][j] == 1) {
							right++;
						} else if (this.board[i][j] == 2) {
							break;
						}
					}
					// 左下
					i = r - 1;
					j = c + 1;
					for (; i >= r - 4 && i >= 0 && j <= c + 4 && j < GamePanelUtil.COUNTLINES; i--, j++) {
						if (board[i][j] == 0) {
							lblank++;
							break;
						} else if (this.board[i][j] == 1) {
							left++;
						} else if (this.board[i][j] == 2) {
							break;
						}
					}
					player[r][c] += EnemyTable[lblank][left][right][rblank];
				} else if (board[r][c] != 0) {
					player[r][c] = 0;
				}
			}
		}

	}

	Stack<Point> getMax(int[][] computer, int[][] player) {
		int cmax = 0;
		int cx = 0;
		int cy = 0;
		int pmax = 0;
		int px = 0;
		int py = 0;
		Stack<Point> ps = new Stack<Point>();
		for (int r = 0; r < GamePanelUtil.COUNTLINES; r++) {
			for (int c = 0; c < GamePanelUtil.COUNTLINES; c++) {
				if (computer[r][c] > cmax) {
					cmax = computer[r][c];
					cx = r;
					cy = c;
				}
				if (player[r][c] > pmax) {
					pmax = player[r][c];
					px = r;
					py = c;
				}
			}
		}
		if (cmax >= pmax) {
			for (int r = 0; r < GamePanelUtil.COUNTLINES; r++) {
				for (int c = 0; c < GamePanelUtil.COUNTLINES; c++) {
					if (computer[r][c] == cmax) {
						ps.push(new Point(r, c));
					}
				}
			}
			return ps;
		} else {
			for (int r = 0; r < GamePanelUtil.COUNTLINES; r++) {
				for (int c = 0; c < GamePanelUtil.COUNTLINES; c++) {
					if (player[r][c] == pmax) {
						ps.push(new Point(r, c));
					}
				}
			}
			return ps;
		}

	}

	Point onFirst() {
		board[7][7] = 1;// 电脑先走
		return new Point(7, 7);
	}

	int turn = 1;

	public Point complay() {
		analyseComputer(computer, board);
		analysePlayer(player, board);
		int[][] tboard = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
		for (int r = 0; r < GamePanelUtil.COUNTLINES; r++) {
			for (int c = 0; c < GamePanelUtil.COUNTLINES; c++) {
				tboard[r][c] = board[r][c];
			}
		}
		Stack<Point> ps = getMax(computer, player);
		// 测试每一个分值的最大的点
		int step = 0;
		Point p1 = null;
		while (!ps.empty()) {
			p1 = ps.pop();
			compThink(p1, p1, tboard, 2 * step + 1);
		}
		// 获取经过n步预测后分值最大的点
		Enumeration em = ht.keys();
		int key = 0;
		int max = 0;
		Point p = null;
		while (em.hasMoreElements()) {
			key = (Integer) em.nextElement();
			Point value = (Point) ht.get(key);
			if (key >= max) {
				max = key;
				p = value;
			}
		}
		ht.clear();
		board[p.x][p.y] = 2;
		return p;
	}

	Hashtable<Integer, Point> ht = new Hashtable();

	private void compThink(Point p, Point temp, int[][] board, int step) {
		int[][] tboard = new int[GamePanelUtil.COUNTLINES][GamePanelUtil.COUNTLINES];
		for (int r = 0; r < GamePanelUtil.COUNTLINES; r++) {
			for (int c = 0; c < GamePanelUtil.COUNTLINES; c++) {
				tboard[r][c] = board[r][c];
			}
		}
		// 在分值最高点下子
		tboard[temp.x][temp.y] = turn;

		// 换位思考
		analyseComputer(computer, tboard);
		analysePlayer(player, tboard);
		Stack<Point> ps = getMax(computer, player);
		if (step == 0) {
			while (!ps.empty()) {
				temp = ps.pop();
				if (computer[temp.x][temp.y] >= player[temp.x][temp.y]) {
					ht.put(computer[temp.x][temp.y], p);
				} else {
					ht.put(player[temp.x][temp.y], p);
				}
			}
			return;
		}
		while (!ps.empty()) {
			compThink(p, ps.pop(), tboard, step - 1);
		}
	}
}
