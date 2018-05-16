package com.superl.introduce;

import com.superl.util.ConverList;
import com.superl.util.JDBCUtil;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

public class IntroduceGoods extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        int goodStore = 0;
        String goodsName = "";
        String goodsIntr = "";
        int price = 0;
        //获取请求key集合
        Enumeration<String> en = req.getParameterNames();
        //遍历集合
        while(en.hasMoreElements()){
            //获取键
            String key = en.nextElement();
            if(key.equals("goodsStore")){
                //获取值
                goodStore = Integer.parseInt(req.getParameter(key));
            }else if(key.equals("goodsName")){
                goodsName = req.getParameter(key);
            }else if(key.equals("goodsIntr")){
                goodsIntr = req.getParameter(key);
            }else if(key.equals("price")){
                price = Integer.parseInt(req.getParameter(key));
            }
        }

        Connection conn;
        Statement st;
        ResultSet rs;
        //获取数据库连接
        conn = JDBCUtil.getConnection();
        //编写sql语句
        String sql = "INSERT INTO segoods\n" +
                     "VALUE(NULL,"+goodStore+","+"'"+goodsName+"', '"+goodsIntr+"', NULL,"+ price+")";
        try {

            st = conn.createStatement();
            int state = st.executeUpdate(sql);
            if (state == 1){
                out.print(state);
            }else {
                out.print(0);
            }
            System.out.println(state);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
