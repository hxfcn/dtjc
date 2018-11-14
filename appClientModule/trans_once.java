

public class trans_once extends TransData {

	@Override
	public void tanrs_imp() {
		// TODO Auto-generated method stub
		
    	//CopyTable ct = new CopyTable_cardaccount();
		//CopyTable ct = new CopyTable_nodeinfor();
		//CopyTable ct = new CopyTable_cardinfor();
		//CopyTable ct = new CopyTable_cardpsninfor();
		CopyTable ct = new CopyTable_cardcompinfor();
		ct.conn_dst = _conn_dst;
    	ct.conn_src = _conn_src;
    	ct.excute();
		
	}
}
