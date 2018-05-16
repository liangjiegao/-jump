package com.superl.Load;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

public class LoadGoods extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();

        String sellerName = "";
        int goodsId = 0;
        //获取请求参数集合
        Enumeration<String> en = req.getParameterNames();
        //遍历集合获取键
        while (en.hasMoreElements()){
            String key = en.nextElement();
            if (key.equals("username")){
                sellerName = req.getParameter(key);
                System.out.println(sellerName);
            }
            if (key.equals("goodsId")){
                goodsId = Integer.parseInt(req.getParameter(key));
                System.out.println(goodsId);
            }
        }
        //获取连接
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        //编写SQL语句
        String sql = "SELECT commuser.`username`, commuser.`Intr`, segoods.`price`, segoods.`name`, segoods.`goodsIntr` \n" +
                    "FROM commuser JOIN (seldstore JOIN segoods ON seldstore.`seId` = segoods.`seId`) ON commuser.`username` = seldstore.`username`\n" +
                    "WHERE commuser.`username` LIKE ? AND segoods.`goodsId` = ?";

        try {
            //获取PreparedStatement
            ps = conn.prepareStatement(sql);
            ps.setString(1, sellerName);
            ps.setInt(2,goodsId);
            rs = ps.executeQuery();

            JSONArray jsonArray = JSONArray.fromObject(ConverList.converList(rs));

            out.println(jsonArray);
            out.close();
            JDBCUtil.close(conn, ps,rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
