package com.superl.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.superl.util.ConverList;
import com.superl.util.JDBCUtil;
import net.sf.json.JSONArray;


public class Login extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		String userName = null;
		String password = null;
		String intr = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs=null; 
		PrintWriter out = resp.getWriter();
		
		//获取请求的key集合
		Enumeration<String> en = req.getParameterNames();
		//遍历集合获取请求的key
		while(en.hasMoreElements()){
			String key  = en.nextElement();
			if(key.equals("userName")){
				userName = req.getParameter(key);
			}else if(key.equals("password")){
				password = req.getParameter(key);
			}
		}
		System.out.println(userName+" "+password);
		if(userName != null && password != null){
			conn = JDBCUtil.getConnection();
			System.out.println("--------------------"+conn);
			try {
				String sql1 = "select * from CommUser where username = ?";
				String sql2 = "select * from CommUser where username = ? AND password = ?";
				ps = conn.prepareStatement(sql1);
				ps.setString(1, userName);
				rs = ps.executeQuery();
				if(rs.next()){
					System.out.println(password);
					rs = null;
//					JDBCUtil.close(conn, ps, rs);
//					conn = JDBCUtil.getConnection();
					ps = conn.prepareStatement(sql2);
					ps.setString(1, userName);
					ps.setInt(2, Integer.parseInt(password));
					rs = ps.executeQuery();
					if(rs.next()){
						System.out.println(password);
						JSONArray  jsonArray = JSONArray.fromObject(ConverList.converList(rs));	//获取jsonArray对象
					
					
						out.println(jsonArray);
						out.close();
//					String un = rs.getString("name");
//					int pw = rs.getInt("password");
//					String selfIntr = rs.getString("Intr");
//					System.out.println(selfIntr);
//					resp.getOutputStream().write(("1/"+un+"/"+pw+"/"+selfIntr).getBytes("utf-8"));
					//resp.getWriter().write("1");
							//System.out.println(1);
					}else{
						out.println("密码错误！");
						//resp.getWriter().write("0");
						//System.out.println(0);
					}
				}else{
					//System.out.println(2);
					out.println("账号不存在！");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JDBCUtil.close(conn, ps, rs);
		}
	}

	
	
	
}
