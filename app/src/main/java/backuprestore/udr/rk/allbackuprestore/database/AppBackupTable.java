package backuprestore.udr.rk.allbackuprestore.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import backuprestore.udr.rk.allbackuprestore.BaseApp;
import backuprestore.udr.rk.allbackuprestore.Model.AppModel;
import backuprestore.udr.rk.allbackuprestore.util.CM;

import static backuprestore.udr.rk.allbackuprestore.BaseApp.sqLiteDatabase;

public class AppBackupTable {

    public static final String TableName = "AppsBTable";
    public static final String APPNAME = "appName";
    public static final String APPIMAGE = "appImage";
    public static final String APPDATETIME = "appDateTime";
    public static final String ISBACKUP = "isBackup";
    public static final String ISSELECTED = "isSelected";


    public static final String APPSIZE = "appSize";
    public static final String APPID = "appId";
    public static final String APPVERSION = "appVersion";
    public static final String APPPACKAGE = "appPackage";
    public static final String APPPATH = "appPath";
    public static final String APPISDELETE = "appisDelete";


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

    public static void updateKeyForDelete(String packageName, String value) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        String selectQuery = "UPDATE " + TableName + " SET " + APPISDELETE
                + " = '" + value + "' WHERE "
                + APPPACKAGE + " ='" + packageName + "'";
        try {
            sqldb.execSQL(selectQuery);
        } catch (Exception e) {
            e.getStackTrace();
        }


    }

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

    public static String checkIsChecked(String apppackage) {
        SQLiteDatabase sqldb = BaseApp.sqLiteDatabase;
        ArrayList<AppModel> arrModelList = null;
        Cursor cursor = null;
        String isbackup = "";

        String Query = "SELECT isSelected FROM " + TableName + " where appPackage ='" + apppackage + "'";
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            isbackup = cursor.getString(cursor.getColumnIndex(ISSELECTED));
            cursor.close();
        }
        return isbackup;
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


    public static ArrayList<AppModel> getAppDataWithBackUp() {
        SQLiteDatabase sqldb = sqLiteDatabase;
        ArrayList<AppModel> arrModelList = null;
        Cursor cursor = null;
        String Query = "Select * from " + TableName + " where " + ISBACKUP + " =" + "1";
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<AppModel>();
            do {
                AppModel model = new AppModel();
                model.setAppName(cursor.getString(cursor.getColumnIndex(APPNAME)));
                model.setAppVerName(cursor.getString(cursor.getColumnIndex(APPVERSION)));
                model.setAppDateTime(cursor.getString(cursor.getColumnIndex(APPDATETIME)));
                model.setAppSize(cursor.getString(cursor.getColumnIndex(APPSIZE)));
                model.setAppImage(cursor.getString(cursor.getColumnIndex(APPIMAGE)));
                model.setAppPackage(cursor.getString(cursor.getColumnIndex(APPPACKAGE)));
                model.setIsSelected(cursor.getString(cursor.getColumnIndex(ISSELECTED)));
                model.setAppIsBackup(cursor.getString(cursor.getColumnIndex(ISBACKUP)));
                model.setAppId(cursor.getString(cursor.getColumnIndex(APPID)));
                model.setAppPath(cursor.getString(cursor.getColumnIndex(APPPATH)));
                model.setAppIsDelete(cursor.getString(cursor.getColumnIndex(APPISDELETE)));

                //model.setHedaer(cursor.getString(cursor.getColumnIndex(HEADER)));
                arrModelList.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }//end if(cursor!=null)
        return arrModelList;
    }

    public static ArrayList<AppModel> getAppDataWithChecked() {
        SQLiteDatabase sqldb = sqLiteDatabase;
        ArrayList<AppModel> arrModelList = null;
        Cursor cursor = null;
        String Query = "Select * from " + TableName + " where " + ISSELECTED + " =" + "1";
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<AppModel>();
            do {
                AppModel model = new AppModel();
                model.setAppName(cursor.getString(cursor.getColumnIndex(APPNAME)));
                model.setAppVerName(cursor.getString(cursor.getColumnIndex(APPVERSION)));
                model.setAppDateTime(cursor.getString(cursor.getColumnIndex(APPDATETIME)));
                model.setAppSize(cursor.getString(cursor.getColumnIndex(APPSIZE)));
                model.setAppImage(cursor.getString(cursor.getColumnIndex(APPIMAGE)));
                model.setAppPackage(cursor.getString(cursor.getColumnIndex(APPPACKAGE)));
                model.setIsSelected(cursor.getString(cursor.getColumnIndex(ISSELECTED)));
                model.setAppIsBackup(cursor.getString(cursor.getColumnIndex(ISBACKUP)));
                model.setAppId(cursor.getString(cursor.getColumnIndex(APPID)));
                model.setAppPath(cursor.getString(cursor.getColumnIndex(APPPATH)));

                model.setAppIsDelete(cursor.getString(cursor.getColumnIndex(APPISDELETE)));
                //model.setHedaer(cursor.getString(cursor.getColumnIndex(HEADER)));
                arrModelList.add(model);
            } while (cursor.moveToNext());
            cursor.close();
        }//end if(cursor!=null)
        return arrModelList;
    }

    public static ArrayList<AppModel> getAppData() {
        SQLiteDatabase sqldb = sqLiteDatabase;
        ArrayList<AppModel> arrModelList = null;
        Cursor cursor = null;
        String Query = "Select * from " + TableName + " where " + APPISDELETE + " = " + "0";
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            arrModelList = new ArrayList<AppModel>();
            do {
                AppModel model = new AppModel();
                model.setAppName(cursor.getString(cursor.getColumnIndex(APPNAME)));
                model.setAppVerName(cursor.getString(cursor.getColumnIndex(APPVERSION)));
                model.setAppDateTime(cursor.getString(cursor.getColumnIndex(APPDATETIME)));
                model.setAppSize(cursor.getString(cursor.getColumnIndex(APPSIZE)));
                model.setAppImage(cursor.getString(cursor.getColumnIndex(APPIMAGE)));
                model.setAppPackage(cursor.getString(cursor.getColumnIndex(APPPACKAGE)));
                model.setIsSelected(cursor.getString(cursor.getColumnIndex(ISSELECTED)));
                model.setAppIsBackup(cursor.getString(cursor.getColumnIndex(ISBACKUP)));
                model.setAppId(cursor.getString(cursor.getColumnIndex(APPID)));
                model.setAppPath(cursor.getString(cursor.getColumnIndex(APPPATH)));
                model.setAppIsDelete(cursor.getString(cursor.getColumnIndex(APPISDELETE)));


                //model.setHedaer(cursor.getString(cursor.getColumnIndex(HEADER)));
                arrModelList.add(model);
            } while (cursor.moveToNext());
            cursor.close();
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


    public static void Insert(ArrayList<AppModel> arrListModel) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        sqldb.beginTransaction();


        for (AppModel model : arrListModel) {
            ContentValues values = new ContentValues();
            String pack = "\"" + model.getAppPackage() + "\"";
            //  values.put(HEADER, model.getHedaer());
            if (CM.CheckIsDataAlreadyInDBorNot(TableName, APPPACKAGE, pack)) {
                try {
                    values.put(APPNAME, model.getAppName());
                    values.put(APPIMAGE, model.getAppImage());
                    values.put(APPDATETIME, model.getAppDateTime());
                    values.put(APPSIZE, model.getAppSize());
                    values.put(APPVERSION, model.getAppVerName());
                    values.put(APPPACKAGE, model.getAppPackage());
                    values.put(APPPATH, model.getAppPath());
                    values.put(ISSELECTED, model.getIsSelected());
                    values.put(ISBACKUP, model.getAppIsBackup());
                    values.put(APPISDELETE, getDeleteed(pack));

                    //String pack = "\"" + model.getAppPackage() + "\"";
                    sqldb.update(TableName, values, APPPACKAGE + " = " + pack, null);
                } catch (Exception e) {
                    e.getMessage();
                }
            } else {
                try {
                    values.put(APPNAME, model.getAppName());
                    values.put(APPIMAGE, model.getAppImage());
                    values.put(APPDATETIME, model.getAppDateTime());
                    values.put(APPSIZE, model.getAppSize());
                    values.put(APPVERSION, model.getAppVerName());
                    values.put(APPPACKAGE, pack);
                    values.put(APPPATH, model.getAppPath());
                    values.put(ISSELECTED, model.getIsSelected());
                    values.put(ISBACKUP, model.getAppIsBackup());
                    values.put(APPISDELETE, model.getAppIsDelete());
                    sqldb.insert(TableName, null, values);
                } catch (Exception e) {
                    e.getMessage();
                }

            }

        }
        sqldb.setTransactionSuccessful();
        sqldb.endTransaction();
    }//End insert method

    public static String getSelectedData(String apppackage) {
        SQLiteDatabase sqldb = BaseApp.sqLiteDatabase;
        ArrayList<AppModel> arrModelList = null;
        Cursor cursor = null;
        String isbackup = "";

        String Query = "SELECT isSelected FROM " + TableName + " where appPackage ='" + apppackage + "'";
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            isbackup = cursor.getString(cursor.getColumnIndex(ISSELECTED));
            cursor.close();
        }
        return isbackup;
    }

    public static String getDeleteed(String apppackage) {
        SQLiteDatabase sqldb = BaseApp.sqLiteDatabase;
        ArrayList<AppModel> arrModelList = null;
        Cursor cursor = null;
        String isbackup = "";
        String Query = "SELECT appisDelete FROM " + TableName + " where appPackage ='" + apppackage + "'";
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            isbackup = cursor.getString(cursor.getColumnIndex(ISBACKUP));
            cursor.close();
        }
        return isbackup;
    }

    public static String getBackupData(String apppackage) {
        SQLiteDatabase sqldb = BaseApp.sqLiteDatabase;
        ArrayList<AppModel> arrModelList = null;
        Cursor cursor = null;
        String isbackup = "";

        String Query = "SELECT isBackup FROM " + TableName + " where appPackage ='" + apppackage + "'";
        cursor = sqldb.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            isbackup = cursor.getString(cursor.getColumnIndex(ISBACKUP));
            cursor.close();
        }
        return isbackup;
    }

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
    public static void updateKey(String packageName, String value) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        String selectQuery = "UPDATE " + TableName + " SET " + ISSELECTED
                + " = '" + value + "' WHERE "
                + APPPACKAGE + " ='" + packageName + "'";
        try {
            sqldb.execSQL(selectQuery);
        } catch (Exception e) {
            e.getStackTrace();
        }


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


    public static void updateKeyForBackup(String packageName, String value) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        String selectQuery = "UPDATE " + TableName + " SET " + ISBACKUP
                + " = '" + value + "' WHERE "
                + APPPACKAGE + " ='" + packageName + "'";
        try {
            sqldb.execSQL(selectQuery);
        } catch (Exception e) {
            e.getStackTrace();
        }


    }


}