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
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class LoadUserMsg extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("连接");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String username="";
        //获取请求key集合
        Enumeration<String> en = req.getParameterNames();
        //遍历集合
        while(en.hasMoreElements()){
            //获取键
            String key = en.nextElement();
            if(key.equals("userName")){
                //获取值
                username = req.getParameter(key);
            }
        }
        //获取数据库连接
        Connection conn = JDBCUtil.getConnection();
        //编写sql语句
        String sqlUserN = "SELECT username, Intr, count(MyAtten) as myAtten, count(attenMe) as attenMe, myBuy ,saleGoodsStore FROM commuser "
                         + "WHERE username= '" + username+"'";
        String sqlColl = "SELECT COUNT(cogoods.name) AS collection FROM cogoods" +
                          " WHERE cogoods.collId IN (" +
                          " SELECT collection.collId FROM collection WHERE collection.username = '"+username+"')";
        String sqlSale =  "SELECT COUNT(segoods.name) AS saleNum FROM segoods" +
                          " WHERE segoods.seId IN (" +
                          " SELECT seldstore.seId FROM seldstore WHERE seldstore.username = '" +username+ "')";
        String sqlSold = "SELECT COUNT(sogoods.name) AS soldNum FROM sogoods" +
                         " WHERE sogoods.soId IN (" +
                         " SELECT soldstore.soId FROM soldstore WHERE soldstore.username = '" +username+ "')";
        String[] sqls = {sqlUserN, sqlColl, sqlSale, sqlSold};
        List<Map<String, Object>> sList =new ArrayList<>();
        //获取PreparedStatement
        try {
            Statement ps = conn.createStatement();
            for (int i = 0; i < sqls.length; i++){
                ResultSet rs = ps.executeQuery(sqls[i]);
                List<Map<String, Object>> list = ConverList.converArrayToList(rs);
                for ( Map<String, Object> map : list) {
                    sList.add(map);
                }
                rs = null;
            }
            JSONArray jsonArray = JSONArray.fromObject(sList);
            out.println(jsonArray);
            out.close();
            JDBCUtil.close(conn, ps, null);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
