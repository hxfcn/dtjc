import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CopyTable_cardinfor extends CopyTable {

	public CopyTable_cardinfor() {
		this.table_name = "cardinfor";
	}
	
	public boolean excute_impl() {
		
		boolean res = true;
		
		try {
			conn_dst.setAutoCommit(false);

			Statement stmt_src = conn_src.createStatement();
	        String sql_src = "SELECT cardno,compno,opetime,cardstatus,nodeno,cardholder,identifyno,pri_card,phone FROM cardinfor";
	        
	        String sql_dst = "INSERT INTO cardinfor "
	        		+ "(cardno,compno,opetime,cardstatus,nodeno,cardholder,identifyno,pri_card,phone) "
	        		+ "VALUES (?,?,?,?,?,?,?,?,?) on DUPLICATE key update "
	        		+ "compno=VALUES(compno),opetime=VALUES(opetime),cardstatus=VALUES(cardstatus),phone=VALUES(phone),"
	        		+ "nodeno=VALUES(nodeno),cardholder=VALUES(cardholder),identifyno=VALUES(identifyno),pri_card=VALUES(pri_card)";
	        
	        PreparedStatement ps_dst = conn_dst.prepareStatement(sql_dst);
	        
	        ResultSet rs = stmt_src.executeQuery(sql_src);
			
	        int insert_size = 0;
	        int batch_size = 0;
	        
	        
	        while (rs.next()) {

	        	copy_str(ps_dst,rs,1);
	        	copy_str(ps_dst,rs,2);

	        	ps_dst.setTimestamp(3, rs.getTimestamp(3));
	        	copy_str(ps_dst,rs,4);
	        	
	        	copy_str(ps_dst,rs,5);
	        	copy_str(ps_dst,rs,6);
	        	copy_str(ps_dst,rs,7);
	        	copy_str(ps_dst,rs,8);
	        	copy_str(ps_dst,rs,9);
	        	ps_dst.addBatch();
	        	batch_size++;
	        	insert_size ++;
	        	        	
	        	if(batch_size >= 1000) {
	        		ps_dst.executeBatch();
	        		conn_dst.commit();
	        		batch_size = 0;

	        		System.out.println(String.format("Insert %d\n", insert_size));
	        	}
	        	
		    }
			if(batch_size > 0) {
        		ps_dst.executeBatch();
        		conn_dst.commit();
        		System.out.println(String.format("Insert %d\n", insert_size));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
