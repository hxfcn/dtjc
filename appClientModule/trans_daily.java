
public class trans_daily extends TransData{

	@Override
	public void tanrs_imp() {
		// TODO Auto-generated method stub
    	CopyTable ct_oilvouch = new CopyTable_oilvouch();
    	ct_oilvouch.conn_dst = _conn_dst;
    	ct_oilvouch.conn_src = _conn_src;
    	ct_oilvouch.excute();
    	
    	CopyTable ct_oildetail = new CopyTable_oildetail();
    	ct_oildetail.conn_dst = _conn_dst;
    	ct_oildetail.conn_src = _conn_src;
    	ct_oildetail.excute();
    	
    	CopyTable ct_oilvouch_bycash = new CopyTable_oilvouch_bycash();
    	ct_oilvouch_bycash.conn_dst = _conn_dst;
    	ct_oilvouch_bycash.conn_src = _conn_src;
    	ct_oilvouch_bycash.excute();
	}

}
