package com.superl.Load;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.superl.util.ConverList;
import com.superl.util.JDBCUtil;
import net.sf.json.JSONArray;

public class LoadSeller extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		String goodsName = "";
		Connection conn;
		Statement ps;
		ResultSet rs;
		//获取请求key集合
		Enumeration<String> en = req.getParameterNames();
		//遍历集合
		while(en.hasMoreElements()){
			//获取键
			String key = en.nextElement();
			if(key.equals("goodsName")){
				//获取值
				goodsName = req.getParameter(key);
			}
		}
		//获取数据库连接
		conn = JDBCUtil.getConnection();
		//编写sql语句

		String sql = "SELECT commuser.`username`, segoods.`name` , segoods.goodsId, segoods.`price` , segoods.`goodsIntr` FROM"
					+" segoods JOIN (seldStore JOIN commuser ON seldStore.`username` = commuser.`username` ) ON segoods.`seId` = seldStore.`seId`"
					+" WHERE segoods.name like '%"+goodsName+"%'";
		try {
			
			ps = conn.createStatement();
			System.out.println(goodsName);
			System.out.println(sql);
			rs =  ps.executeQuery(sql);
			
			JSONArray jsonArray = JSONArray.fromObject(ConverList.converList(rs));
			
			out.print(jsonArray);
			System.out.println(jsonArray);
			JDBCUtil.close(conn, ps, rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}












