
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.File;


public class Config {
	
	private static Config _instance = null;
	public static Config instance() {
		if(_instance == null) {
	        try {
	        	
				_instance = new Config();
		        //reader.setEncoding("gb2312");
		        //String rt = "D:/zsh";
		        String rt =System.getProperty("user.dir");
		        _instance.root = rt;
	        	SAXReader reader = new SAXReader();
	        	TLog.instance().info("¶ÁÈ¡ÅäÖÃÎÄ¼þ£º" + rt + "/config.xml");
				Document document = reader.read(rt + "/config.xml");
				
		        Element re = document.getRootElement();
		        
		        Element o = re.element("src_db");
		        _instance.src_db_url = o.element("url").getText();
		        _instance.src_db_port = o.element("port").getText();
		        _instance.src_db_user = o.element("user").getText();
		        _instance.src_db_pwd = o.element("pwd").getText();

		        Element o2 = re.element("dst_db");
		        _instance.dst_db_url = o2.element("url").getText();
		        _instance.dst_db_port = o2.element("port").getText();
		        _instance.dst_db_user = o2.element("user").getText();
		        _instance.dst_db_pwd = o2.element("pwd").getText();
		        
		        Element o3 = re.element("clear_before_days");
		        int v = Integer.valueOf(o3.getText());
		        if(v >= 1) {
		        	_instance.clear_before_days = v * -1;
		        }
				
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_instance = null;
			}
		}
		return _instance;
	}
	
	public String src_db_url;
	public String src_db_port;
	public String src_db_user;
	public String src_db_pwd;
	
	public String dst_db_url;
	public String dst_db_port;
	public String dst_db_user;
	public String dst_db_pwd;
	
	public int clear_before_days = 3;
	
	public String root = "";

}
