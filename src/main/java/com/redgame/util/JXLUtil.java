package com.redgame.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import com.redgame.entity.User;
import com.redgame.view.frame.MainFrame;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class JXLUtil {

	/**
	 * 创建文件.xls
	 * 
	 * @param filePath
	 */
	public static void createExcel(String filePath) {
		try {
			WritableWorkbook wwb;// 创建Excel工作簿
			/**
			 * 在当前目录下生成user文件
			 */
			OutputStream os = new FileOutputStream(filePath);
			wwb = Workbook.createWorkbook(os);

			/**
			 * 添加第一个工作表并设置第一个sheet的名字，0是工作表的索引
			 */
			WritableSheet sheet = wwb.createSheet("用户信息", 0);
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 20);
			sheet.setColumnView(2, 20);
			sheet.setColumnView(3, 20);
			Label lb_name = new Label(0, 0, "姓名");
			Label lb_pwd = new Label(1, 0, "密码");
			Label lb_score = new Label(2, 0, "分数");
			sheet.addCell(lb_score);
			sheet.addCell(lb_pwd);
			sheet.addCell(lb_name);
			wwb.write();
			wwb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查.xls文件是否存在
	 * 
	 * @return
	 */
	public static boolean checkFileExist(String filePath) {
		if (new File(filePath).exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 检查用户名是否存在
	 * 
	 * @param filePath
	 * @param name
	 * @return
	 */
	public static boolean checkName(String filePath, String name) {

		try {
			// 打开文件
			Workbook wb = Workbook.getWorkbook(new File(filePath));
			// 获得名为第一页的sheet
			Sheet sheet = wb.getSheet(0);
			for (int i = 1; i < sheet.getRows(); i++) {
				if (sheet.getCell(0, i).getContents().equals(name)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取所有用户
	 * 
	 * @param filePath
	 * @return
	 */
	public static ArrayList<User> getUsers(String filePath) {
		List<User> users = new ArrayList<User>();
		// 打开文件
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(new File(filePath));
			// 获得名为第一页的sheet
			Sheet sheet = wb.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
				users.add(new User(sheet.getCell(0, i).getContents(), sheet
						.getCell(1, i).getContents()));
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (ArrayList<User>) users;
	}

	/**
	 * 返回文件的行数
	 * 
	 * @param filePath
	 * @return
	 */
	public static int getRows(String filePath) {
		File file = new File(filePath);
		Workbook wb;
		int rows = 0;
		try {
			wb = Workbook.getWorkbook(file);
			Sheet st = wb.getSheet(0);
			rows = st.getRows();
			wb.close();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}

	/**
	 * 返回对应姓名的排名
	 * 
	 * @param name
	 * @return
	 */
	public static String getRanking(String name) {
		int count_user = JXLUtil.getRows("user.xls") - 1;
		int[] scores = new int[count_user];
		Workbook wb = null;
		String temp = null;
		try {
			wb = Workbook.getWorkbook(new File("user.xls"));
			// 获得名为第一页的sheet
			Sheet sheet = wb.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
				scores[i - 1] = Integer.parseInt(sheet.getCell(2, i)
						.getContents());
			}
			Arrays.sort(scores);
			for (int i = 0; i < scores.length; i++) {
				if (JXLUtil.getScore(name) == scores[i]) {
					temp = Integer.toString(scores.length - i);
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * 获得分数
	 * 
	 * @param name
	 * @return
	 */
	public static int getScore(String name) {
		Workbook wb = null;
		int score = 0;
		try {
			wb = Workbook.getWorkbook(new File(GamePanelUtil.FILEPATH));
			// 获得名为第一页的sheet
			Sheet sheet = wb.getSheet(0);
			int rows = sheet.getRows();
			int temp = 0;
			for (int i = 1; i < rows; i++) {
				if (sheet.getCell(0, i).getContents().equals(name))
					temp = i;
			}
			score = Integer.parseInt(sheet.getCell(2, temp).getContents());
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return score;
	}

	/**
	 * 游戏输了
	 * 
	 * @param name
	 * @param score
	 * @return
	 */
	public static void updateFail(String name, int score) {
		score = score - GamePanelUtil.FAIL;
		try {
			File file = new File(GamePanelUtil.FILEPATH);
			Workbook wb = Workbook.getWorkbook(file);
			int row = JXLUtil.getRow(name);
			File tmpFile = new File(System.getProperty("user.dir")
					+ "\\tempfile.xls");
			WritableWorkbook wwb = Workbook.createWorkbook(tmpFile, wb);
			WritableSheet ws = wwb.getSheet(0);
			Label lb_score = new Label(2, row, Integer.toString(score));
			ws.addCell(lb_score);
			wwb.write();
			wwb.close();
			file.delete();
			tmpFile.renameTo(file);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 用户逃跑扣分
	 * 
	 * @param name
	 * @param score
	 */
	public static void updateEscape(String name, int score) {
		score = score - GamePanelUtil.ESCAPE;
		try {
			File file = new File(GamePanelUtil.FILEPATH);
			Workbook wb = Workbook.getWorkbook(file);
			int row = JXLUtil.getRow(name);
			File tmpFile = new File(System.getProperty("user.dir")
					+ "\\tempfile.xls");
			WritableWorkbook wwb = Workbook.createWorkbook(tmpFile, wb);
			WritableSheet ws = wwb.getSheet(0);
			Label lb_score = new Label(2, row, Integer.toString(score));
			ws.addCell(lb_score);
			wwb.write();
			wwb.close();
			file.delete();
			tmpFile.renameTo(file);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 游戏赢了
	 * 
	 * @param name
	 * @param score
	 * @return
	 */
	public static void updateSuccess(String name, int score) {
		score = score + GamePanelUtil.SUCCESS;
		try {
			File file = new File(GamePanelUtil.FILEPATH);
			Workbook wb = Workbook.getWorkbook(file);
			int row = JXLUtil.getRow(name);
			File tmpFile = new File(System.getProperty("user.dir")
					+ "\\tempfile.xls");
			WritableWorkbook wwb = Workbook.createWorkbook(tmpFile, wb);
			WritableSheet ws = wwb.getSheet(0);
			Label lb_score = new Label(2, row, Integer.toString(score));
			ws.addCell(lb_score);
			wwb.write();
			wwb.close();
			file.delete();
			tmpFile.renameTo(file);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 获取对应名字的行号
	 * 
	 * @param name
	 * @return
	 */
	private static int getRow(String name) {
		Workbook wb = null;
		int temp = 0;
		try {
			wb = Workbook.getWorkbook(new File(GamePanelUtil.FILEPATH));
			// 获得名为第一页的sheet
			Sheet sheet = wb.getSheet(0);
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
				if (sheet.getCell(0, i).getContents().equals(name))
					temp = i;
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * 文件新注册用户
	 * 
	 * @param filePath
	 * @param name
	 * @param pwd
	 */
	public static void register(String filePath, String name, String pwd) {
		try {
			File file = new File(filePath);
			Workbook wb = Workbook.getWorkbook(file);
			int rows = JXLUtil.getRows(filePath);
			if (!isUserExist(name)) {
				File tmpFile = new File(System.getProperty("user.dir")
						+ "\\tempfile.xls");
				WritableWorkbook wwb = Workbook.createWorkbook(tmpFile, wb);
				WritableSheet ws = wwb.getSheet(0);
				Label lb_name = new Label(0, rows, name);
				Label lb_pwd = new Label(1, rows, pwd);
				Label lb_score = new Label(2, rows, "0");
				ws.addCell(lb_pwd);
				ws.addCell(lb_name);
				ws.addCell(lb_score);
				wwb.write();
				wwb.close();
				file.delete();
				tmpFile.renameTo(file);
				JOptionPane.showMessageDialog(null, "注册成功!");
				GamePanelUtil.PlayMusic(GamePanelUtil.CLICK);
				MainFrame.mf.setContentPane(MainFrame.mf.getMp());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 判断用户是否存在
	 * 
	 * @param name
	 * @return
	 */
	private static boolean isUserExist(String name) {
		String path = "user.xls";
		File file = new File(path);
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(file);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Sheet st = wb.getSheet(0);
		int rows = st.getRows();
		String name_sheet;
		for (int i = 1; i < rows; i++) {
			name_sheet = st.getCell(0, i).getContents();
			if (name_sheet.equals(name)) {
				GamePanelUtil.PlayMusic(GamePanelUtil.WARN);
				JOptionPane.showMessageDialog(null, "用户名已存在！");
				return true;
			}
		}
		return false;
	}
}
