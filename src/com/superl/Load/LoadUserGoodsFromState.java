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

public class LoadUserGoodsFromState extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String userName = "";
        String goodsState = "";
        //获取请求参数集合
        Enumeration<String> en = req.getParameterNames();
        //遍历集合获取键
        while (en.hasMoreElements()){
            String key = en.nextElement();
            if (key.equals("userName")){
                userName = req.getParameter(key);
            }
            if (key.equals("goodsState")){
                goodsState = req.getParameter(key);
            }
        }
        //获取连接
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        //编写多条Sql语句， 用于匹配不同的查询状态
        String mySeGoods = "SELECT segoods.`name`, segoods.`price` \n" +
                          "FROM segoods JOIN (commuser JOIN seldstore ON commuser.`username` = seldstore.`username`) ON segoods.`seId` = seldstore.`seId`\n" +
                          "WHERE commuser.`username` LIKE ?";
        String mySoGoods = "SELECT sogoods.`name`, sogoods.`price` \n" +
                           "FROM sogoods JOIN (commuser JOIN soldstore ON commuser.`username` = soldstore.`username`) ON sogoods.`soId` = soldstore.`soId`\n" +
                          "WHERE commuser.`username` LIKE ?";
        String myCollGoods = "SELECT cogoods.`name`, cogoods.`price` \n" +
                             "FROM cogoods JOIN (commuser JOIN collection ON commuser.`username` = collection.`username`) ON cogoods.`collId` = collection.`collId`\n" +
                             "WHERE commuser.`username` LIKE ?";
        String myBuyGoods = "SELECT buygoods.`name`, buygoods.`price` \n" +
                            "FROM buygoods JOIN (commuser JOIN buyStore ON commuser.`username` = buyStore.`username`) ON buygoods.`buyId` = buyStore.`buyId`\n" +
                            "WHERE commuser.`username` LIKE ?";
        String executeSQL = "";
        if (goodsState.equals("mySeGoods")){
            executeSQL = mySeGoods;
        }else if (goodsState.equals("mySoGoods")){
            executeSQL = mySoGoods;
        }else if (goodsState.equals("myCollGoods")){
            executeSQL = myCollGoods;
        }else if (goodsState.equals("myBuyGoods")){
            executeSQL = myBuyGoods;
        }
        try {
            ps = conn.prepareStatement(executeSQL);
            ps.setString(1, userName);
            rs = ps.executeQuery();

            JSONArray jsonArray = JSONArray.fromObject(ConverList.converList(rs));

            out.println(jsonArray);
            out.close();
            JDBCUtil.close(conn, ps, rs);
        } catch (SQLException e) {
            JDBCUtil.close(conn, ps, rs);
            e.printStackTrace();
        }
    }
}
