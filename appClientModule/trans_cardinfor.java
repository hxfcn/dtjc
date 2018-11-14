

public class trans_cardinfor extends TransData {

	@Override
	public void tanrs_imp() {
		// TODO Auto-generated method stub
		
    	CopyTable ct_crardinfor = new CopyTable_cardinfor();
    	ct_crardinfor.conn_dst = _conn_dst;
    	ct_crardinfor.conn_src = _conn_src;
    	ct_crardinfor.excute();
		
	}

}
