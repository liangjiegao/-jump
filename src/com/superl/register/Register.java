package com.superl.register;

import com.superl.util.JDBCUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

public class Register extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String userName = "";
        String password = "";

        //获取key集合
        Enumeration<String> en = req.getParameterNames();
        //遍历集合
        while(en.hasMoreElements()){
            String key = en.nextElement();
            if (key.equals("userName")){
                userName = req.getParameter(key);
            }
            if (key.equals("password")){
                password = req.getParameter(key);
            }
        }

        //获取连接
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        //编写SQL语句
        String sql = "INSERT INTO commuser" +
                      " VALUE(?,?,'这个家伙很懒， 设什么都没留下',NULL,NULL,NULL,NULL,NULL,NULL,NULL)";
        //创建PreparedStatement
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,userName);
            ps.setInt(2, Integer.parseInt(password));
            int isSuccess = ps.executeUpdate();
            if (isSuccess == 1){
                out.println(isSuccess);
            }else {
                out.println("注册失败");
            }


            JDBCUtil.close(conn, ps, null);
        } catch (SQLException e) {
            JDBCUtil.close(conn, ps, null);
            e.printStackTrace();
        }
    }
}
