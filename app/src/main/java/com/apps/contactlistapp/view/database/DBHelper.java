package com.apps.contactlistapp.view.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.apps.contactlistapp.BuildConfig;
import com.apps.contactlistapp.model.ContactModel;
import com.apps.contactlistapp.view.classes.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "contact_user_list.db";
    private SQLiteDatabase db;
    private final Context context;
    private String DB_PATH;
    String outFileName = "";
    SharedPreferences.Editor spEdit;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";
    }


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        //------------------------------------------------------------
        PackageInfo pinfo = null;
        if (!dbExist) {
            getReadableDatabase();
            copyDataBase();
        }

    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public Cursor getData(String Query) {
        String myPath = DB_PATH + DB_NAME;
        Cursor c = null;
        try {
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            c = db.rawQuery(Query, null);
        } catch (Exception e) {
            Log.e("Err", e.toString());
        }
        return c;
    }

    public void dml(String Query) {
        String myPath = DB_PATH + DB_NAME;
        if (db == null)
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            db.execSQL(Query);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }


    private static final String contactTables = "create table " + Constant.TABLE_CONTACT +
            "(" + Constant.CONTACT_NAME + " text," +
            Constant.CONTACT_NUMBER + " text primary key," +
            Constant.CONTACT_EMAIL + " text," +
            Constant.CONTACT_IMAGE + " text);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(contactTables);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }


    public boolean saveToContactList(String value, String name, String number, String email, String img) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.CONTACT_NAME, name);
        contentValues.put(Constant.CONTACT_NUMBER, number);
        contentValues.put(Constant.CONTACT_EMAIL, email);
        contentValues.put(Constant.CONTACT_IMAGE, img);


        long result = db.insert(Constant.TABLE_CONTACT, null, contentValues);
        if(result==-1)
        {
            return false;
        }else{
            return true;
        }
    }


    public ArrayList<ContactModel> getContactDetail(String tableContact) {

        ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();

        //Cursor cursor = getData("select * from '" + table + "'" + " ORDER BY RANDOM()");
        Cursor cursor = getData("select * from '" + tableContact + "'");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String c_name = cursor.getString(cursor.getColumnIndex("contact_name"));
                String c_number = cursor.getString(cursor.getColumnIndex("contact_number"));
                String c_mail = cursor.getString(cursor.getColumnIndex("contact_mail"));
                String c_img = cursor.getString(cursor.getColumnIndex("contact_img"));

                ContactModel wallpaper = new ContactModel( c_name, c_number, c_mail, c_img);
                arrayList.add(wallpaper);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return arrayList;
    }

    public void deleteRecord(String tableContact, String number) {
        db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + tableContact + " where " + Constant.CONTACT_NUMBER + "='" + number + "'");

    }

    public boolean upDatedData(String tableContact, String name, String number, String email, String img) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.CONTACT_NAME, name);
        values.put(Constant.CONTACT_EMAIL, email);
        values.put(Constant.CONTACT_IMAGE, img);

        Cursor cursor = database.rawQuery("Select * from tbl_contact where contact_number = ?",new String[]{number});
        if(cursor.getCount()>0)
        {
            long result = database.update(tableContact, values, "contact_number = ?",new String[]{number});
            if(result==-1)
            {
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }






    }
}
