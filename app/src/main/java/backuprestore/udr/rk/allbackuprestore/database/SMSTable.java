package backuprestore.udr.rk.allbackuprestore.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import backuprestore.udr.rk.allbackuprestore.BaseApp;
import backuprestore.udr.rk.allbackuprestore.Model.AppModel;
import backuprestore.udr.rk.allbackuprestore.Model.Sms;
import backuprestore.udr.rk.allbackuprestore.util.CM;

import static backuprestore.udr.rk.allbackuprestore.BaseApp.sqLiteDatabase;

public class SMSTable {

    public static final String TableName = "SMSTable";

    public static final String SMSADDRESS = "smsAddress";
    public static final String SMSTIME = "smstime";
    public static final String SMSBODY = "smsbody";
    public static final String ISBACKUP = "isBackup";
    public static final String ISSELECTED = "isSelected";
    public static final String SMSID = "smsId";
    public static final String SMSREADSTATE = "smsreadState";
    public static final String SMSAPPID = "smsAppId";


    //private static String headerTitle1;


//    public static ArrayList<DiscloserPojo> getAllData(String headerTitle) {
//
//        SQLiteDatabase sqldb = Ayala.sqLiteDatabase;
//        ArrayList<DiscloserPojo> arrModelList = null;
//        Cursor cursor = null;
//
//        headerTitle1 = "\"" + headerTitle + "\"";
//        String Query = "SELECT * FROM " + TableName + " where HeaderTitle =" + headerTitle1;
//        cursor = sqldb.rawQuery(Query, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            arrModelList = new ArrayList<DiscloserPojo>();
//            do {
//                DiscloserPojo model = new DiscloserPojo();
//                model.setPdfId(cursor.getString(cursor.getColumnIndex(PDFID)));
//                model.setTitle(cursor.getString(cursor.getColumnIndex(PDFNAME)));
//                model.setDownload(cursor.getString(cursor.getColumnIndex(PDFPATH)));
//                model.setIsDownload(cursor.getString(cursor.getColumnIndex(ISDOWNLOAD)));
//                model.setIsViewed(cursor.getString(cursor.getColumnIndex(ISVIEWED)));
//                model.setHeaderTitle(cursor.getString(cursor.getColumnIndex(HEADERTITLE)));
//                model.setDownloadStatus(cursor.getString(cursor.getColumnIndex(DOWNLOADSTATUS)));
//                arrModelList.add(model);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }//end if(cursor!=null)
//        return arrModelList;
//    }


    public static ArrayList<AppModel> getAllisChecked() {

        SQLiteDatabase sqldb = BaseApp.sqLiteDatabase;
        ArrayList<AppModel> arrModelList = null;
        Cursor cursor = null;
        String isbackup = "0";

        String Query = "SELECT * FROM " + TableName + " where isSelected =" + isbackup;
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<AppModel>();
            do {
                AppModel model = new AppModel();
                model.setIsSelected(cursor.getString(cursor.getColumnIndex(ISBACKUP)));
                arrModelList.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return arrModelList;
    }

    public static String checkIsChecked(String id) {
        SQLiteDatabase sqldb = BaseApp.sqLiteDatabase;
        ArrayList<AppModel> arrModelList = null;
        Cursor cursor = null;
        String isbackup = "";

        String Query = "SELECT isSelected FROM " + TableName + " where " + SMSID + " ='" + id + "'";
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            isbackup = cursor.getString(cursor.getColumnIndex(ISSELECTED));
            cursor.close();
        }
        return isbackup;
    }

    public static void setAllChecke(String value) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        String selectQuery = "UPDATE " + TableName + " SET " + ISSELECTED
                + " = '" + value + "'";
        try {
            sqldb.execSQL(selectQuery);
        } catch (Exception e) {
            e.getStackTrace();
        }


    }

    public static void updateKey(String id, String value) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        String selectQuery = "UPDATE " + TableName + " SET " + ISSELECTED
                + " = '" + value + "' WHERE "
                + SMSID + " ='" + id + "'";
        try {
            sqldb.execSQL(selectQuery);
        } catch (Exception e) {
            e.getStackTrace();
        }


    }

//
//    public static ArrayList<PojoItems> getAllDownloadData() {
//        String download = "1";
//        SQLiteDatabase sqldb = FoodOrdringApplication.sqLiteDatabase;
//        ArrayList<PojoItems> arrModelList = null;
//        Cursor cursor = null;
//        String Query = "SELECT * FROM " + TableName + " where " + ISDOWNLOAD + " = " + download;
//        cursor = sqldb.rawQuery(Query, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            arrModelList = new ArrayList<DiscloserPojo>();
//            do {
//                DiscloserPojo model = new DiscloserPojo();
//                model.setPdfId(cursor.getString(cursor.getColumnIndex(PDFID)));
//                model.setTitle(cursor.getString(cursor.getColumnIndex(PDFNAME)));
//                model.setDownload(cursor.getString(cursor.getColumnIndex(PDFPATH)));
//                model.setIsDownload(cursor.getString(cursor.getColumnIndex(ISDOWNLOAD)));
//                model.setIsViewed(cursor.getString(cursor.getColumnIndex(ISVIEWED)));
//                model.setHeaderTitle(cursor.getString(cursor.getColumnIndex(HEADERTITLE)));
//                model.setDownloadStatus(cursor.getString(cursor.getColumnIndex(DOWNLOADSTATUS)));
//                arrModelList.add(model);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }//end if(cursor!=null)
//        return arrModelList;
//    }


    /*public static ArrayList<PojoItems> getAlldata() {
        SQLiteDatabase sqldb = FoodOrdringApplication.sqLiteDatabase;
        ArrayList<PojoItems> arrModelList = null;
        String bool = "\"" + "true" + "\"";
        Cursor cursor = null;
        String Query = "Select * from " + TableName + " where " + ISREQUIRED + " =" + bool;
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<PojoItems>();
            do {
                PojoItems model = new PojoItems();
                model.setnMapperID(cursor.getString(cursor.getColumnIndex(NMAPPERID)));
                model.setnAttributeID(cursor.getString(cursor.getColumnIndex(NATTRIBUTEID)));
                model.setcAttributeLabel(cursor.getString(cursor.getColumnIndex(CATTRIBUTELABEL)));
                model.setIsMultiple(cursor.getString(cursor.getColumnIndex(ISMULTIPLE)));
                model.setIsRequired(cursor.getString(cursor.getColumnIndex(ISREQUIRED)));
                //model.setHedaer(cursor.getString(cursor.getColumnIndex(HEADER)));
                arrModelList.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }//end if(cursor!=null)
        return arrModelList;
    }
*/


    public static ArrayList<Sms> getAppDataWithBackUp() {
        SQLiteDatabase sqldb = sqLiteDatabase;
        ArrayList<Sms> arrModelList = null;
        Cursor cursor = null;
        String Query = "Select * from " + TableName + " where " + ISBACKUP + " =" + "true";
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<Sms>();
            do {
                Sms model = new Sms();
                model.setId(cursor.getString(cursor.getColumnIndex(SMSID)));
                model.setSmsAppId(cursor.getString(cursor.getColumnIndex(SMSAPPID)));
                model.setAddress(cursor.getString(cursor.getColumnIndex(SMSADDRESS)));
                model.setMsg(cursor.getString(cursor.getColumnIndex(SMSBODY)));
                model.setTime(cursor.getString(cursor.getColumnIndex(SMSTIME)));
                model.setSmsreadState(cursor.getString(cursor.getColumnIndex(SMSREADSTATE)));
                model.setIsSelected(cursor.getString(cursor.getColumnIndex(ISSELECTED)));
                model.setIsBackup(cursor.getString(cursor.getColumnIndex(ISBACKUP)));
                arrModelList.add(model);


            } while (cursor.moveToNext());
            cursor.close();
        }//end if(cursor!=null)
        return arrModelList;
    }

    public static ArrayList<Sms> getAppDataWithChecked() {
        SQLiteDatabase sqldb = sqLiteDatabase;
        ArrayList<Sms> arrModelList = null;
        Cursor cursor = null;
        String bool = "\"" + true + "\"";
        String Query = "Select * from " + TableName + " where " + ISSELECTED + " =" + bool;
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<Sms>();
            do {
                Sms model = new Sms();
                model.setId(cursor.getString(cursor.getColumnIndex(SMSID)));
                model.setSmsAppId(cursor.getString(cursor.getColumnIndex(SMSAPPID)));
                model.setAddress(cursor.getString(cursor.getColumnIndex(SMSADDRESS)));
                model.setMsg(cursor.getString(cursor.getColumnIndex(SMSBODY)));
                model.setTime(cursor.getString(cursor.getColumnIndex(SMSTIME)));
                model.setSmsreadState(cursor.getString(cursor.getColumnIndex(SMSREADSTATE)));
                model.setIsSelected(cursor.getString(cursor.getColumnIndex(ISSELECTED)));
                model.setIsBackup(cursor.getString(cursor.getColumnIndex(ISBACKUP)));
                arrModelList.add(model);

                //model.setHedaer(cursor.getString(cursor.getColumnIndex(HEADER)));

            } while (cursor.moveToNext());
            cursor.close();
        }//end if(cursor!=null)
        return arrModelList;
    }

    public static ArrayList<Sms> getAppDataById(String txt) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        ArrayList<Sms> arrModelList = null;
        Cursor cursor = null;
        // String Query = "SELECT DISTINCT(" + SMSADDRESS + ") FROM " + TableName;
        String txta = "\"" + txt + "\"";
        String Query = "select * from " + TableName + " where " + SMSADDRESS + " = " + txta;

        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<Sms>();
            do {
                Sms model = new Sms();
                model.setId(cursor.getString(cursor.getColumnIndex(SMSID)));
                model.setSmsAppId(cursor.getString(cursor.getColumnIndex(SMSAPPID)));
                model.setAddress(cursor.getString(cursor.getColumnIndex(SMSADDRESS)));
                model.setMsg(cursor.getString(cursor.getColumnIndex(SMSBODY)));
                model.setTime(cursor.getString(cursor.getColumnIndex(SMSTIME)));
                model.setSmsreadState(cursor.getString(cursor.getColumnIndex(SMSREADSTATE)));
                model.setIsSelected(cursor.getString(cursor.getColumnIndex(ISSELECTED)));
                model.setIsBackup(cursor.getString(cursor.getColumnIndex(ISBACKUP)));
                arrModelList.add(model);
            } while (cursor.moveToNext());
            //cursor.close();
        }//end if(cursor!=null)
        return arrModelList;
    }

    public static ArrayList<Sms> getAppData() {
        SQLiteDatabase sqldb = sqLiteDatabase;
        ArrayList<Sms> arrModelList = null;
        Cursor cursor = null;

        // String Query = "SELECT DISTINCT(" + SMSADDRESS + ") FROM " + TableName;

        String Query = "select * from " + TableName + " group by " + SMSADDRESS + " ORDER BY smsId ASC ";

        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<Sms>();
            do {
                Sms model = new Sms();
                model.setId(cursor.getString(cursor.getColumnIndex(SMSID)));
                model.setSmsAppId(cursor.getString(cursor.getColumnIndex(SMSAPPID)));
                model.setAddress(cursor.getString(cursor.getColumnIndex(SMSADDRESS)));
                model.setMsg(cursor.getString(cursor.getColumnIndex(SMSBODY)));
                model.setTime(cursor.getString(cursor.getColumnIndex(SMSTIME)));
                model.setSmsreadState(cursor.getString(cursor.getColumnIndex(SMSREADSTATE)));
                model.setIsSelected(cursor.getString(cursor.getColumnIndex(ISSELECTED)));
                model.setIsBackup(cursor.getString(cursor.getColumnIndex(ISBACKUP)));
                arrModelList.add(model);
            } while (cursor.moveToNext());
            //cursor.close();
        }//end if(cursor!=null)
        return arrModelList;
    }

 /*   public static ArrayList<PojoItems> getAlldataNotReruird() {
        SQLiteDatabase sqldb = FoodOrdringApplication.sqLiteDatabase;
        ArrayList<PojoItems> arrModelList = null;
        Cursor cursor = null;
        String bool = "\"" + "false" + "\"";
        boolean b = false;
        String Query = "Select * from " + TableName + " where " + ISREQUIRED + " =" + bool;
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<PojoItems>();
            do {
                PojoItems model = new PojoItems();
                model.setnMapperID(cursor.getString(cursor.getColumnIndex(NMAPPERID)));
                model.setnAttributeID(cursor.getString(cursor.getColumnIndex(NATTRIBUTEID)));
                model.setcAttributeLabel(cursor.getString(cursor.getColumnIndex(CATTRIBUTELABEL)));
                model.setIsMultiple(cursor.getString(cursor.getColumnIndex(ISMULTIPLE)));
                model.setIsRequired(cursor.getString(cursor.getColumnIndex(ISREQUIRED)));
                //model.setHedaer(cursor.getString(cursor.getColumnIndex(HEADER)));
                arrModelList.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }//end if(cursor!=null)
        return arrModelList;
    }*/


//    public static ArrayList<PojoItems> getSelectedIdRecord(String pdfId) {
//        SQLiteDatabase sqldb = FoodOrdringApplication.sqLiteDatabase;
//        ArrayList<PojoItems> arrModelList = null;
//        Cursor cursor = null;
//        //String Query = "Select * from " + TableName;
//        String Query = "Select * from " + TableName + " where " + NMAPPERID + " = " + pdfId;
//        cursor = sqldb.rawQuery(Query, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            arrModelList = new ArrayList<PojoItems>();
//            do {
//                PojoItems model = new PojoItems();
//                model.setnMapperID(cursor.getString(cursor.getColumnIndex(NMAPPERID)));
//                model.setnAttributeID(cursor.getString(cursor.getColumnIndex(NATTRIBUTEID)));
//                model.setcAttributeLabel(cursor.getString(cursor.getColumnIndex(CATTRIBUTELABEL)));
//                model.setIsMultiple(cursor.getString(cursor.getColumnIndex(ISMULTIPLE)));
//                arrModelList.add(model);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }//end if(cursor!=null)
//        return arrModelList;
//    }
    /*
    * get single record
    * */


//    public static Model_ClubDetails SelectSingleRecord() {
//        SQLiteDatabase sqldb = KarnavatiApp.sqLiteDatabase;
//        Model_ClubDetails model = null;
//        Cursor cursor = null;
//        String Query = "Select * from " + TableName;
//        cursor = sqldb.rawQuery(Query, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            model = new Model_ClubDetails();
//            model.Id = (cursor.getInt(cursor.getColumnIndex(ID)));
//            model.ClubName = (cursor.getString(cursor.getColumnIndex(CLUBNAME)));
//            model.ClubAddress = (cursor.getString(cursor.getColumnIndex(CLUBADDRESS)));
//            model.ClubPhone = (cursor.getString(cursor.getColumnIndex(CLUBPHONE)));
//            model.WebSite = (cursor.getString(cursor.getColumnIndex(WEBSITE)));
//            model.LatLong = (cursor.getString(cursor.getColumnIndex(LATLONG)));
//            model.EmailId = (cursor.getString(cursor.getColumnIndex(EMAILID)));
//            model.CreatedDate = (cursor.getString(cursor.getColumnIndex(CREATEDDATE)));
//            model.UpdatedDate = (cursor.getString(cursor.getColumnIndex(UPDATEDDATE)));
//            cursor.close();
//        }//end if(cursor!=null)
//        return model;
//    }


    public static void Insert(ArrayList<Sms> arrListModel) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        sqldb.beginTransaction();


        for (Sms model : arrListModel) {
            ContentValues values = new ContentValues();
            values.put(SMSADDRESS, model.getAddress());
            values.put(SMSBODY, model.getMsg());
            values.put(SMSTIME, model.getTime());
            values.put(SMSREADSTATE, model.getReadState());
            values.put(SMSAPPID, model.getSmsAppId());
            values.put(ISSELECTED, model.getIsSelected());
            values.put(ISBACKUP, model.getIsBackup());
            if (CM.CheckIsDataAlreadyInDBorNot(TableName, SMSAPPID, model.getSmsAppId())) {
                try {
                    sqldb.update(TableName, values, SMSAPPID + "=" + model.getSmsAppId(), null);
                } catch (Exception e) {
                    e.getMessage();
                }
            } else {
                try {
                    sqldb.insert(TableName, null, values);
                } catch (Exception e) {
                    e.getMessage();
                }

            }


        }
        sqldb.setTransactionSuccessful();
        sqldb.endTransaction();
    }//End insert method

    public static int deleteData() {
        SQLiteDatabase sqldb = sqLiteDatabase;
        int row = 0;
        try {
            row = sqldb.delete(TableName, null, null);
        } catch (Exception e) {
            e.getMessage();
        }
        return row;
    }


    public static void setAllChecked(String key) {
        //  String isView = "1";
        SQLiteDatabase sqldb = BaseApp.sqLiteDatabase;
        String selectQuery = "UPDATE " + TableName + " SET " + ISSELECTED
                + " = '" + key + "'";
        try {
            sqldb.execSQL(selectQuery);
        } catch (Exception e) {

            e.getStackTrace();
        }


    }

    //
//    public static void updateKey(String packageName, String value) {
//        SQLiteDatabase sqldb = sqLiteDatabase;
//        String selectQuery = "UPDATE " + TableName + " SET " + ISSELECTED
//                + " = '" + value + "' WHERE "
//                + APPPACKAGE + " ='" + packageName + "'";
//        try {
//            sqldb.execSQL(selectQuery);
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//
//
//    }

    //    public static void updateKeyForBackup(String packageName, String value) {
//        SQLiteDatabase sqldb = sqLiteDatabase;
//        String selectQuery = "UPDATE " + TableName + " SET " + ISBACKUP
//                + " = '" + value + "' WHERE "
//                + APPPACKAGE + " ='" + packageName + "'";
//        try {
//            sqldb.execSQL(selectQuery);
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//
//
//    }
    public static String getCount(String text) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        String selectQuery = "SELECT smsAddress FROM SMSTable WHERE smsAddress=" + text;
        String count = "";
        try {
            Cursor cursor = sqldb.rawQuery(selectQuery, null);
            int total_count = cursor.getCount();
            count = String.valueOf(total_count);

        } catch (Exception e) {
            e.getStackTrace();
            count = "0";
        }
        return count;
    }


    public static void deleteItem(String packageName) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        String selectQuery = "delete from " + TableName + " where appPackage = '" + packageName + "' ";
        try {
            sqldb.execSQL(selectQuery);
        } catch (Exception e) {
            e.getStackTrace();
        }


    }
//
//    public static void updateDonloadStatus(String id, String status) {
//        SQLiteDatabase sqldb = Ayala.sqLiteDatabase;
//        String selectQuery = "UPDATE " + TableName + " SET " + DOWNLOADSTATUS
//                + " = '" + status + "' WHERE "
//                + PDFID + " = " + id;
//        try {
//            sqldb.execSQL(selectQuery);
//
//        } catch (Exception e) {
//
//            e.getStackTrace();
//        }
//
//
//    }

}