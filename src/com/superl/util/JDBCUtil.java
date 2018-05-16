package com.superl.util;

import java.sql.*;

public class JDBCUtil {

    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
           System.out.println("注册失败！");
        }
    }
    private static Connection conn;
    public static Connection getConnection(){

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jump?serverTimezone=UTC", "root", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void close(Connection conn, Statement st, ResultSet rs){
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                if (st != null){
                    try {
                        st.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally {
                        if (conn != null){
                            try {
                                conn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }
}
