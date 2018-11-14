import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CopyTable_oildetail extends CopyTable {

	public CopyTable_oildetail() {
		this.table_name = "hh_oildetail";
	}
	
	public boolean clear_old_data() {
		
		try {
			
			TLog.instance().info("清除旧数据...");
			
			String timestr = this.getClearTimeString();

			Statement stmt_src = conn_src.createStatement();
			String sql_clear = "delete from hh_oildetail where oildetailtime < '" + timestr + "'";

			int res = stmt_src.executeUpdate(sql_clear);
			stmt_src.close();
			TLog.instance().info("清除" + timestr + "日之前，共" + String.valueOf(res) + " 条数据");
			return true;
			
		} catch (SQLException e) {
			TLog.instance().info("清除旧数据失败！" + e.getLocalizedMessage());
		}
		return false;
		
	}
	
	
	public boolean excute_impl() {
		
		boolean res = false;
        int insert_size = 0;   
        int src_max_id = 0;
        int dst_max_id = 0;
        do {
        	
    		try {
    			this.conn_dst.setAutoCommit(false);

    			Statement stmt_src = conn_src.createStatement();

    			String sql_get_src_max_id = "select MAX(__id) from hh_oildetail";
    			ResultSet rs_get_src_max_id = stmt_src.executeQuery(sql_get_src_max_id);
    			if(!rs_get_src_max_id.next()) {
    				TLog.instance().info("获取原表最大ID失败!");
    				stmt_src.close();
    				break;
    			}
    			src_max_id =  rs_get_src_max_id.getInt(1);
    			
    			Statement stmt_dst = conn_dst.createStatement();
    			String sql_get_dst_max_id = "select MAX(__id) from oildetail";
    			ResultSet rs_get_dst_max_id = stmt_dst.executeQuery(sql_get_dst_max_id);
    			if(!rs_get_dst_max_id.next()) {
    				TLog.instance().info("获取目标表最大ID失败!");
    				stmt_dst.close();
    				break;
    			}
    			dst_max_id =  rs_get_dst_max_id.getInt(1);
    			
    			if(src_max_id <= dst_max_id) {
    				TLog.instance().info("最大ID异常!");
    				stmt_dst.close();
    				stmt_src.close();
    				break;
    			}
    			
    			
    	        String sql_src = "SELECT moneysou,terminalno,cardno,paymode,suctag,macno,openo,nodeno,oilno,oildetailtime,opetime,ctc,psam_ttc,ttc,amount,balance,pumpno,__id,price,litter,accountdate "
    	        		+ "FROM hh_oildetail where __id between " + String.valueOf(dst_max_id + 1) + " and " + String.valueOf(src_max_id) + " order by __id";
    	        
    	        String sql_dst = "INSERT INTO oildetail "
    	        		+ "(moneysou,terminalno,cardno,paymode,suctag,macno,openo,nodeno,oilno,oildetailtime,opetime,ctc,psam_ttc,ttc,amount,balance,pumpno,__id,price,litter,accountdate) "
    	        		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    	        
    	        PreparedStatement ps_dst = conn_dst.prepareStatement(sql_dst);
    	        ResultSet rs = stmt_src.executeQuery(sql_src);
    	        int batch_size = 0;
    	        
    	        while (rs.next()) {
    	        	int ix = 1;
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	
    	        	copy_time(ps_dst,rs,ix++);
    	        	copy_time(ps_dst,rs,ix++);
    	        	
    	        	copy_int(ps_dst,rs,ix++);
    	        	copy_int(ps_dst,rs,ix++);
    	        	copy_int(ps_dst,rs,ix++);
      	        	copy_float(ps_dst,rs,ix++);
    	        	copy_float(ps_dst,rs,ix++);
    	        	copy_float(ps_dst,rs,ix++);
    	        	copy_int(ps_dst,rs,ix++);
    	        	copy_float(ps_dst,rs,ix++);
    	        	copy_float(ps_dst,rs,ix++);
    	        	copy_time(ps_dst,rs,ix++);
    	        	
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
    			
				stmt_dst.close();
				stmt_src.close();
    			
				res = true;
    			String fmt = "%s 表复制完成,共复制 %d 条数据";
    			TLog.instance().info(String.format(fmt, this.table_name,insert_size));
    			
    		} catch (SQLException e) {
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
        }while(false);
        
        if(res) {
        	res = this.clear_old_data();
        }
        
		return res;
	}
	
}
