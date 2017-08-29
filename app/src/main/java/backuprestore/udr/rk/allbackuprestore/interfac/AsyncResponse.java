package backuprestore.udr.rk.allbackuprestore.interfac;

import java.util.ArrayList;

import backuprestore.udr.rk.allbackuprestore.Model.Sms;

public interface AsyncResponse {
    void processFinish(ArrayList<Sms> output);
}