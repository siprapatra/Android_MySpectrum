package com.sipra.myspectrum.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sipra.myspectrum.CustomerModel;

import java.util.ArrayList;

import static com.sipra.myspectrum.database.DBConstants.CUS_CREATED;
import static com.sipra.myspectrum.database.DBConstants.CUS_ID;
import static com.sipra.myspectrum.database.DBConstants.CUS_NAME;
import static com.sipra.myspectrum.database.DBConstants.CUS_PASS;
import static com.sipra.myspectrum.database.DBConstants.DATABASE_NAME;
import static com.sipra.myspectrum.database.DBConstants.TABLE_CUSTOMER;


public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String TAG = DatabaseHandler.class.getName();
    private static final int DATABASE_VERSION = 1;

    Context mContext;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        //3rd argument to be passed is CursorFactory instance
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_PRODUCT_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMER + "("
                + CUS_ID + " INTEGER PRIMARY KEY," + CUS_NAME + " TEXT,"
                + CUS_PASS + " TEXT," + CUS_CREATED + " TEXT" + ")";

        sqLiteDatabase.execSQL(CREATE_PRODUCT_CUSTOMER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.e(TAG, "onUpgrade");
        Log.e(TAG, "i :- " + i + " i1 :- " + i1);
    }

    public long addCustomer(CustomerModel customerModel) {
        long res = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMER, null, CUS_NAME + "=? COLLATE NOCASE",
                new String[]{customerModel.getName()}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            res = -2;
        } else {
            ContentValues values = new ContentValues();

            values.put(CUS_NAME, customerModel.getName());
            values.put(CUS_PASS, customerModel.getPass());
            values.put(CUS_CREATED, customerModel.getCreated());

            // Inserting Row
            res = db.insert(TABLE_CUSTOMER, null, values);
        }
        cursor.close();
        db.close();
        return res;
    }

    public long updateCustomer(CustomerModel customerModel) {

        long rowUpdated = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CUS_ID, customerModel.getId());
        values.put(CUS_NAME, customerModel.getName());
        values.put(CUS_PASS, customerModel.getPass());
        values.put(CUS_CREATED, customerModel.getCreated());

        rowUpdated = db.update(TABLE_CUSTOMER, values, CUS_ID + "=?", new String[]{String.valueOf(customerModel.getId())});
        db.close();

        return rowUpdated;
    }

    // code to get All Customer
    public ArrayList<CustomerModel> getAllCustomer() {

        ArrayList<CustomerModel> customerModelArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " ORDER BY " + CUS_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomerModel customerModel = new CustomerModel();
                customerModel.setId(Integer.parseInt(cursor.getString(0)));
                customerModel.setName(cursor.getString(1));
                customerModel.setPass(cursor.getString(2));
                customerModel.setCreated(cursor.getString(3));

                customerModelArrayList.add(customerModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customerModelArrayList;
    }

}
