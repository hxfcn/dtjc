import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CopyTable {

	public Connection conn_src = null;
	public Connection conn_dst = null;
	
	public String table_name = null;
	
	
	public CopyTable() {
		
	}
	
//	public CopyTable(Connection src,Connection dst,String tname) {
//		conn_src = src;
//		conn_dst = dst;
//		table_name = tname;
//	}
	
	public boolean excute() {
		
		if(conn_src == null || table_name == null || conn_dst == null) {
			
			return false;
		}
		
		TLog.instance().info("开始复制表:" + this.table_name);
		
//		if(!this.lock_src_table()) {
//			//System.out.println(String.format("原表锁失败！"));
//			TLog.instance().info("原表锁失败！");
//			return false;
//		}
		
		boolean res = this.excute_impl();
		return res;
		//this.unlock_src_table();
		
		//return true;
	}
	
	public boolean excute_impl() {
		return false;
	}
	
	public boolean lock_src_table() {
	
		try {
			Statement stmt_src = conn_src.createStatement();
	        String sql_src = "begin tran\n lock table " + table_name + " in share mode";
	        stmt_src.execute(sql_src);
		}catch(SQLException e) {
			return false;
		}
		return true;
	}
	
	public boolean unlock_src_table() {
		if(conn_src == null ) {
			return false;
		}
		
		try {
			conn_src.commit();
			
		}catch(SQLException e) {
			return false;
		}
		return true;
	}
	
	public void copy_str(PreparedStatement ps,ResultSet rs,int i) throws SQLException {
		String s = rs.getString(i);
		if(s != null) {
			ps.setString(i, s.trim());
		}else {
			ps.setString(i, null);
		}
	}
	
	public void copy_strcn(PreparedStatement ps,ResultSet rs,int i) throws SQLException, UnsupportedEncodingException {
		String s = rs.getString(i);
		if(s != null) {
			String cn = new String(s.getBytes("ISO-8859-1"));
			ps.setString(i, cn.trim());
		}else {
			ps.setString(i, null);
		}
	}
	
	public void copy_time(PreparedStatement ps,ResultSet rs,int i) throws SQLException {
		ps.setTimestamp(i, rs.getTimestamp(i));
	}
	
	public void copy_int(PreparedStatement ps,ResultSet rs,int i) throws SQLException {
		ps.setInt(i, rs.getInt(i));
	}
	
	public void copy_float(PreparedStatement ps,ResultSet rs,int i) throws SQLException {
		ps.setFloat(i, rs.getFloat(i));
	}
	
	public String getClearTimeString() {
		java.util.Calendar cal = java.util.Calendar.getInstance();//使用默认时区和语言环境获得一个日历。    
		cal.add(Calendar.DATE, Config.instance().clear_before_days);//取当前日期的前3天.     
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String timestr = format.format(cal.getTime());
		return timestr;
	}
}
