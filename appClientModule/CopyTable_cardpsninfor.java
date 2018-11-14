import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CopyTable_cardpsninfor extends CopyTable {
	public CopyTable_cardpsninfor() {
		this.table_name = "cardpsninfor";
		
		this.columns = new String[] {
				"compno",
				"compname",
				"comptag",
				"compaddrs",
				"postno",
				"areano",
				"telphno1",
				"telphno2",
				"e_mail",
				"faxno",
				"birthday",
				"identitytype",
				"identifyno",
				"compattribute",
				"bgdate",
				"nodeno",
				"compstatus",
				"confidence",
				"working",
				"duty",
				"job",
				"knowledge",
				"cartype",
				"marriage",
				"country",
				"province",
				"city",
				"cardnum",
				"invoicetype",
				"checktype",
				"comptype",
				"invoicehead",
				"onlineflag",
				"postflag",
				"smsflag",
				"mmsflag",
				"mailflag",
				"limitcarnoflag",
				"qq",
				"mmsno",
				"dealtype",
				"certphotoflag",
				"billtype",
				"merch_flag",
				"taxno",
				"taxapprovedtime",
				"usercardtype"
		};
	}
	String[] columns;
	
	@Override
	public boolean excute_impl() {
		boolean res = true;
		int ix = 1;
		try {
			conn_dst.setAutoCommit(false);
			String colstr = String.join(",", this.columns);
			Statement stmt_src = conn_src.createStatement();
	        String sql_src = "SELECT "+colstr+" FROM cardpsninfor";
	        
	        String[] qmarr = new String[this.columns.length];
	        for(int i = 0; i< this.columns.length;i++) {
	        	qmarr[i] = "?";
	        }
	        
	        String[] duparr = new String[this.columns.length-1];
	        for(int i = 0; i< duparr.length;i++) {
	        	String ccc=this.columns[i+1];
	        	duparr[i] = ccc + "=VALUES(" + ccc + ")";
	        }
	        
	        String sql_dst = "INSERT INTO cardpsninfor ("
	        		+ colstr
	        		+ ") VALUES ("
	        		+ String.join(",", qmarr) + ")  on DUPLICATE key update "
	        		+ String.join(",", duparr);
	        
	        TLog.instance().info("sql_dst:" + sql_dst);
	        
	        PreparedStatement ps_dst = conn_dst.prepareStatement(sql_dst);
	        ResultSet rs = stmt_src.executeQuery(sql_src);
	        int insert_size = 0;
	        int batch_size = 0;
	        while (rs.next()) {
	        	ix = 1;
	        	copy_strcn(ps_dst,rs,ix++);
	        	copy_strcn(ps_dst,rs,ix++);
	        	copy_strcn(ps_dst,rs,ix++);
	        	copy_strcn(ps_dst,rs,ix++);
	        	copy_strcn(ps_dst,rs,ix++);
	        	copy_strcn(ps_dst,rs,ix++);
	        	copy_strcn(ps_dst,rs,ix++);
	        	copy_strcn(ps_dst,rs,ix++);
	        	copy_strcn(ps_dst,rs,ix++);
	        	copy_strcn(ps_dst,rs,ix++);

		        copy_time(ps_dst,rs,ix++);

		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        
		        copy_time(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        
		        copy_int(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        
		        copy_time(ps_dst,rs,ix++);
		        copy_strcn(ps_dst,rs,ix++);
		        
		        
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

			TLog.instance().info("提交完成！");
			
		} catch (SQLException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TLog.instance().info("当前列:" + this.columns[ix -1]);
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
