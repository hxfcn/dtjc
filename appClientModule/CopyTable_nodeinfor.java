import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class CopyTable_nodeinfor extends CopyTable {

	public CopyTable_nodeinfor() {
		this.table_name = "nodeinfor";
	}
	
	public boolean excute_impl() {
		
		boolean res = true;
        int insert_size = 0;   
		try {
			this.conn_dst.setAutoCommit(false);

			Statement stmt_src = conn_src.createStatement();
	        String sql_src = "SELECT nodeno,sinopec_nodeno,nodename,nodetag,telphno,faxno,nodetree,nodetype,useful_statu FROM nodeinfor";
	        
	        String sql_dst = "INSERT INTO nodeinfor "
	        		+ "(nodeno,sinopec_nodeno,nodename,nodetag,telphno,faxno,nodetree,nodetype,useful_statu) "
	        		+ "VALUES (?,?,?,?,?,?,?,?,?)  on DUPLICATE key update "
	        		+ "nodename=VALUES(nodename),nodetag=VALUES(nodetag),telphno=VALUES(telphno),faxno=VALUES(faxno),nodetype=VALUES(nodetype),useful_statu=VALUES(useful_statu)";
	        
	        PreparedStatement ps_dst = conn_dst.prepareStatement(sql_dst);
	        
	        ResultSet rs = stmt_src.executeQuery(sql_src);
			
   
			
	        int batch_size = 0;
	        
	        
	        while (rs.next()) {
	        	copy_str(ps_dst,rs,1);
	        	copy_str(ps_dst,rs,2);
	        	
	        	String src = rs.getString(3);
	        	String nodename = null;
	        	if(src != null) {
		        	nodename = new String(src.getBytes("ISO-8859-1")).trim();
	        	}
	        	//nodename.to
	        	ps_dst.setString(3, nodename);
	        	
	        	src = rs.getString(4);
	        	String nodetag = null;
	        	if(src != null) {
	        		nodetag = new String(src.getBytes("ISO-8859-1")).trim();
	        	}
	        	ps_dst.setString(4, nodetag);
	        	
	        	copy_str(ps_dst,rs,5);
	        	copy_str(ps_dst,rs,6);
	        	copy_str(ps_dst,rs,7);
	        	copy_str(ps_dst,rs,8);
	        	ps_dst.setInt(9, rs.getInt(9));
	        	
	        	ps_dst.addBatch();

	        	batch_size++;
	        	insert_size ++;
	        	
	        	if(batch_size >= 1000) {
	        		ps_dst.executeBatch();
	        		batch_size = 0;  		
	        		System.out.println(String.format("Insert %d\n", insert_size));
	        	}
	        	
	        	
		    }
			if(batch_size > 0) {
        		ps_dst.executeBatch();
        		System.out.println(String.format("Insert %d\n", insert_size));
			}
			
			TLog.instance().info("正在提交...");
			conn_dst.commit();
			TLog.instance().info("提交完成");
			
			String fmt = "%s 表复制完成,共复制 %d 条数据";
			TLog.instance().info(String.format(fmt, this.table_name,insert_size));
			
		} catch (SQLException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			String fmt = "%s 表复制失败,当前第 %d 条数据: %s";
			
			TLog.instance().info(String.format(fmt, this.table_name,insert_size,e.toString()));

			
			try {
				conn_dst.rollback();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res = false;
		}

		return res;
	}
	
}
