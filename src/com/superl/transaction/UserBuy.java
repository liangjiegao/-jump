package com.superl.transaction;

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

public class UserBuy extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();

        int seGoodsId =0;
        String seGoodsName = "";
        int buyStoreId = 0;

        String seGoodsIntr = "";
        double price = 0;
        //获取请求参数集合
        Enumeration<String> en = req.getParameterNames();
        //遍历集合获取键
        while (en.hasMoreElements()){
            String key = en.nextElement();
            if (key.equals("seGoodsId")){
                seGoodsId = Integer.parseInt(req.getParameter(key));
            }
            if (key.equals("buyStoreId")){
                buyStoreId = Integer.parseInt(req.getParameter(key));
            }
        }
        //获取连接
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        //查询出售卖商品的全部信息的 sql语句
        String querySeGoodsMsgSQL =  "SELECT NAME, goodsIntr, price\n" +
                                     "FROM segoods \n" +
                                      "WHERE goodsId LIKE ?";
        try {
            ps = conn.prepareStatement(querySeGoodsMsgSQL);
            ps.setInt(1,seGoodsId);

            rs = ps.executeQuery();
            if (rs.next()){
                seGoodsName = rs.getString("name");
                seGoodsIntr = rs.getString("goodsIntr");
                price = rs.getDouble("price");
            }
            ps = null;
            rs = null;
            String insertIntoBuyGoodsSQL =  "INSERT INTO buygoods\n" +
                    "VALUE (NULL,?,?,?,null,?)";
            ps = conn.prepareStatement(insertIntoBuyGoodsSQL);
            ps.setInt(1, buyStoreId);
            ps.setString(2, seGoodsName);
            ps.setString(3,seGoodsIntr);
            ps.setDouble(4,price);

            int row = ps.executeUpdate();
            if (row == 1){
                String insertIntoSoGoodsSQL =  "INSERT INTO sogoods\n" +
                                                "VALUE (NULL,?,?,?,null,?)";
                ps = conn.prepareStatement(insertIntoSoGoodsSQL);
                ps.setInt(1, buyStoreId);
                ps.setString(2, seGoodsName);
                ps.setString(3,seGoodsIntr);
                ps.setDouble(4,price);

                int row2 = ps.executeUpdate();

                ps = null;
                String deleteSeGoodsSQL = "DELETE FROM segoods\n" +
                                          "WHERE goodsId = ?";
                ps = conn.prepareStatement(deleteSeGoodsSQL);
                ps.setInt(1, seGoodsId);
                int row3 = ps.executeUpdate();

                if (row2 == 1 && row3 == 1){
                    System.out.println("购买成功");
                    out.println(1);
                }else {
                    out.println(0);
                }
            }else{
                out.println(0);
            }
            out.close();
            JDBCUtil.close(conn, ps, rs);
        } catch (SQLException e) {
            JDBCUtil.close(conn, ps, rs);
            e.printStackTrace();
        }
    }
}
