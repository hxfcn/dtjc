import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CopyTable_oilvouch_bycash extends CopyTable{
	public CopyTable_oilvouch_bycash() {
		this.table_name = "hh_oilvouch_bycash";
	}
	
	public boolean clear_old_data() {
		
		try {
			
			TLog.instance().info("���������...");
			
			String timestr = this.getClearTimeString();

			Statement stmt_src = conn_src.createStatement();
			String sql_clear = "delete from hh_oilvouch_bycash where oilvouch_bycashtime < '" + timestr + "'";

			int res = stmt_src.executeUpdate(sql_clear);
			stmt_src.close();
			TLog.instance().info("���" + timestr + "��֮ǰ����" + String.valueOf(res) + " ������");
			return true;
			
		} catch (SQLException e) {
			TLog.instance().info("���������ʧ�ܣ�" + e.getLocalizedMessage());
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

    			String sql_get_src_max_id = "select MAX(__id) from hh_oilvouch_bycash";
    			ResultSet rs_get_src_max_id = stmt_src.executeQuery(sql_get_src_max_id);
    			if(!rs_get_src_max_id.next()) {
    				TLog.instance().info("��ȡԭ�����IDʧ��!");
    				stmt_src.close();
    				break;
    			}
    			src_max_id =  rs_get_src_max_id.getInt(1);

    			
    			Statement stmt_dst = conn_dst.createStatement();
    			String sql_get_dst_max_id = "select MAX(__id) from oilvouch_bycash";
    			ResultSet rs_get_dst_max_id = stmt_dst.executeQuery(sql_get_dst_max_id);
    			if(!rs_get_dst_max_id.next()) {
    				TLog.instance().info("��ȡĿ������IDʧ��!");
    				stmt_dst.close();
    				break;
    			}

    			dst_max_id =  rs_get_dst_max_id.getInt(1);
    			
    			if(src_max_id <= dst_max_id) {
    				TLog.instance().info("���ID�쳣!");
    				stmt_dst.close();
    				stmt_src.close();
    				break;
    			}
    			
    			
//    	        String sql_src = "SELECT cardno,oilno,opetime,ctc,ttc,nodeno,macno,openo,litter,price,amount,balance,suctag,checkcode,pumpno,pos_p,sign,tac,gmac,psam_tac,psam_asn,terminalno,psam_ttc,moneysou,paymode,payunit,t_mac,accountdate,tracode,gettime,m_ctc,m_ttc,hw,subno1,wrongflag,key_version,key_index"
    			String sql_src = "SELECT cardno,oilno,opetime,ctc,nodeno,litter,price,amount,macno,suctag,terminalno,moneysou,openo,balance,ttc ,pumpno,psam_ttc,paymode,oilvouch_bycashtime,__id,accountdate "
    	        		+ "FROM hh_oilvouch_bycash where __id between " + String.valueOf(dst_max_id + 1) + " and " + String.valueOf(src_max_id) + " order by __id";
    	        
    	        String sql_dst = "INSERT INTO oilvouch_bycash "
    	        		+ "(cardno,oilno,opetime,ctc,nodeno,litter,price,amount,macno,suctag,terminalno,moneysou,openo,balance,ttc ,pumpno,psam_ttc,paymode,oilvouchtime,__id,accountdate) "
    	        		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    	        
    	        PreparedStatement ps_dst = conn_dst.prepareStatement(sql_dst);
    	        ResultSet rs = stmt_src.executeQuery(sql_src);
    	        int batch_size = 0;
    	        
    	        while (rs.next()) {
    	        	int ix = 1;
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_time(ps_dst,rs,ix++);
    	        	copy_int(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_float(ps_dst,rs,ix++);
    	        	copy_float(ps_dst,rs,ix++);
    	        	copy_float(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_float(ps_dst,rs,ix++);
    	        	copy_int(ps_dst,rs,ix++);
    	        	copy_float(ps_dst,rs,ix++);
    	        	copy_int(ps_dst,rs,ix++);
    	        	copy_str(ps_dst,rs,ix++);
    	        	copy_time(ps_dst,rs,ix++);
    	        	copy_int(ps_dst,rs,ix++);
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
    			
    			TLog.instance().info("�����ύ...");
    			conn_dst.commit();
    			TLog.instance().info("�ύ���");
    			
				stmt_dst.close();
				stmt_src.close();
    			
				res = true;
    			String fmt = "%s �������,������ %d ������";
    			TLog.instance().info(String.format(fmt, this.table_name,insert_size));
    			
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			
    			String fmt = "%s ����ʧ��,��ǰ�� %d ������: %s";
    			
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
