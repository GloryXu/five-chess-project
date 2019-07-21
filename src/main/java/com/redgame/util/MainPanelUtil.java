package com.redgame.util;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.redgame.entity.User;
import com.redgame.view.frame.MainFrame;


public class MainPanelUtil {
	/**
	 * 检查用户输入是否合法
	 * 
	 * @param name
	 * @param pwd
	 * @return
	 */
	public static boolean checkUserPwd(String name, String pwd) {
		if (name == null || name.length() == 0) {
			JOptionPane.showMessageDialog(new JPanel(), "用户名为空！");
			return false;
		} else if (name.length() < 6) {
			JOptionPane.showMessageDialog(new JPanel(), "用户名少于6位！");
			return false;
		} else if (pwd == null || pwd.length() == 0) {
			JOptionPane.showMessageDialog(new JPanel(), "密码为空！");
			return false;
		} else if (pwd.length() < 6) {
			JOptionPane.showMessageDialog(new JPanel(), "密码少于6位!");
			return false;
		}
		return true;
	}

	/**
	 * 检查用户的信息是否存在
	 * 
	 * @param name
	 * @param pwd
	 * @return
	 */
	public static boolean checkUserInfo(String name, String pwd) {
		String filePath = "user.xls";
		if (!JXLUtil.checkFileExist(filePath)) {
			JXLUtil.createExcel(filePath);
			JOptionPane.showMessageDialog(new JPanel(), "用户不存在，请先注册!");
			MainFrame.mf.setContentPane(MainFrame.mf.getRp());
			return false;
		} else {
			try {
				List<User> users = JXLUtil.getUsers(filePath);
				User nowUser = new User(name, pwd);
				// 判断用户密码是否匹配
				if (users != null) {
					for (User user : users) {
						if (user.getName().equals(nowUser.getName())
								&& user.getPwd().equals(nowUser.getPwd())) {
							return true;
						}
					}
				}
				if (JXLUtil.checkName(filePath, nowUser.getName())) {
					JOptionPane.showMessageDialog(null, "密码错误！");
					return false;
				} else {
					JOptionPane.showMessageDialog(null, "用户名不存在！");
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
