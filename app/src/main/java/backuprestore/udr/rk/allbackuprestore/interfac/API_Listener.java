package backuprestore.udr.rk.allbackuprestore.interfac;

/**
 * Created by Ravi kumawat on 2017-02-06.
 */

public interface API_Listener {
    public void onSuccess(int requestnumber, Object obj);
    public void onFail(String errormessage);
}
