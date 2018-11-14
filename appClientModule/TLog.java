import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TLog {
	
	private static Logger log = null;  
	public static Logger instance() {
		if(log == null) {

			try {
				String root = Config.instance().root;
				root += "/log.properties";
				System.setProperty("java.util.logging.config.file", root);  
				
				log = Logger.getLogger("TLog");
		        log.setLevel(Level.ALL);  
		        
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log = null;
			}
		}

		return log;
	}
	
}
