import java.io.UnsupportedEncodingException;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;

public class TransData {

	Connection _conn_src = null;
	Connection _conn_dst = null;
	
	public void connectdb() {
		boolean flag = true;
        try {
	        	{
					Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance();
					String url = Config.instance().src_db_url;// 数据库名
					Properties sysProps = System.getProperties();
					sysProps.put("user", Config.instance().src_db_user); 
					sysProps.put("password", Config.instance().src_db_pwd);
					_conn_src = DriverManager.getConnection(url, sysProps);
	        	}
	        	{
	    			Class.forName("com.mysql.jdbc.Driver");
	    	        String url =  Config.instance().dst_db_url;// 数据库名
	    	        String user =  Config.instance().dst_db_user;
	    	        String pwd =  Config.instance().dst_db_pwd;  
	    	        _conn_dst = DriverManager.getConnection(url, user, pwd);//获取连接  
	        	}
        	
        }catch (ClassNotFoundException | SQLException  | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TLog.instance().info(e.getLocalizedMessage());
			flag = false;
		} 
        
        if(flag == true) return;
        try {
        	if(_conn_src != null ) {
        		_conn_src.close();
        	}
        	if(_conn_dst != null ) {
        		_conn_dst.close();
        	}
        }catch ( SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} 
        _conn_src = null;
        _conn_dst = null;
	}
	
	public void tanrs_imp() {
		

    	
	}
	
	public void trans() {
		
		this.connectdb();
		
    	if(_conn_src == null || _conn_dst == null ) {
    		TLog.instance().info("数据库连接错误\n");
    		return;
    	}
    	
    	this.tanrs_imp();
	}
	
	
}
