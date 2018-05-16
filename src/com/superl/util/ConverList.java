package com.superl.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverList {

	//根据ResultSet，把数据封装成List<Map<String, Object>>的形式
		public static List<Map<String, Object>> converList(ResultSet rs) throws SQLException{
			rs.previous();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();

			while(rs.next()){
				Map<String, Object> rowMap = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					System.out.println(rs.getObject(i));
					rowMap.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowMap);
			}
			return list;
			
		}
	//根据ResultSet[] ，把数据封装成List<Map<String, Object>>的形式
	public static List<Map<String, Object>> converArrayToList(ResultSet rs) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while(rs.next()){
				Map<String, Object> rowMap = new HashMap<String, Object>();
				for (int j = 1; j <= columnCount; j++) {
					rowMap.put(md.getColumnName(j),rs.getObject(j));
				}
				list.add(rowMap);
			}

		return list;

	}
}
