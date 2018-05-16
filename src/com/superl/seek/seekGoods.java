package com.superl.seek;

import com.superl.util.JDBCUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Enumeration;

public class seekGoods extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String goodsName = "";
        //获取请求参数集合
        Enumeration<String> en = req.getParameterNames();
        //遍历集合获取键
        while(en.hasMoreElements()){
            String key = en.nextElement();
            if (key.equals("goodsName")){
                goodsName = req.getParameter(key);
            }
        }
        //请求转移
        req.getRequestDispatcher("/LoadSeller?seek="+goodsName).forward(req, resp);

    }
}
