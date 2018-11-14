
import java.sql.*; 
import java.util.*;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if(Config.instance() == null) {
			TLog.instance().info("ÅäÖÃÎÄ¼ş´íÎó£¡\n");
			return;
		}
		
		/*TransData td = new TransData();*/
		TransData td = new trans_once();
		td.trans();
		return;
		
	}

	/* (non-Java-doc)
	 * @see java.lang.Object#Object()
	 */
	public Main() {
		super();
	}

}