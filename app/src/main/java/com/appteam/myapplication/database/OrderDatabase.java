package com.appteam.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.appteam.myapplication.model.Order;

import java.util.ArrayList;

public class OrderDatabase extends SQLiteOpenHelper {
    private static OrderDatabase instance;
    public static OrderDatabase getInstance(Context context){
        if(instance == null){
            instance = new OrderDatabase(context);
        }
        return instance;
    }
    public static final String DATABASE_NAME = "order.db";
    public static final String ORDER_TABLE_NAME = "orders";
    public static final String ORDER_COLUMN_ID = "id";
    public static final String ORDER_COLUMN_ITEM_NAME = "item_name";
    public static final String ORDER_COLUMN_DATE = "date";
    public static final String ORDER_COLUMN_PRICE = "price";
    public static final String ORDER_COLUMN_RATING = "rating";

    private OrderDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+ORDER_TABLE_NAME+" (id integer  primary key AUTOINCREMENT, item_name text," +
                        "date text,price double, rating integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +ORDER_TABLE_NAME);
        onCreate(db);
    }
    public boolean insertOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDER_COLUMN_ITEM_NAME,order.getItemName());
        contentValues.put(ORDER_COLUMN_DATE,order.getDatePicker());
        contentValues.put(ORDER_COLUMN_PRICE,order.getPrice());
        contentValues.put(ORDER_COLUMN_RATING,order.getRating());
        if(db.insert(ORDER_TABLE_NAME, null, contentValues) == -1){
            db.close();
            return false;
        }
        db.close();
        return true;
    }
    public boolean updateOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDER_COLUMN_ITEM_NAME,order.getItemName());
        contentValues.put(ORDER_COLUMN_DATE,order.getDatePicker());
        contentValues.put(ORDER_COLUMN_PRICE,order.getPrice());
        contentValues.put(ORDER_COLUMN_RATING,order.getRating());
        if(db.update(ORDER_TABLE_NAME, contentValues, "id = ?", new String[]{Integer.toString(order.getId())}) != -1){
            db.close();
            return true;
        }
        db.close();
        return false;
    }
    public Integer deleteOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ORDER_TABLE_NAME,
                "id = ?",
                new String[]{Integer.toString(order.getId())});
    }
    public ArrayList<Order> getAllOrder(){
        ArrayList<Order> listOrder = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+ORDER_TABLE_NAME,null);
        res.moveToFirst();
        while (!res.isAfterLast()){
            listOrder.add(new Order(
                    res.getInt(res.getColumnIndex(ORDER_COLUMN_ID)),
                    res.getString(res.getColumnIndex(ORDER_COLUMN_ITEM_NAME)),
                    res.getString(res.getColumnIndex(ORDER_COLUMN_DATE)),
                    res.getDouble(res.getColumnIndex(ORDER_COLUMN_PRICE)),
                    res.getInt(res.getColumnIndex(ORDER_COLUMN_RATING))
            ));
            res.moveToNext();
        }
        db.close();
        return listOrder;
    }
    public ArrayList<Order> searchOrder(String s){
        ArrayList<Order> listOrder = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+ORDER_TABLE_NAME +" where "+ORDER_COLUMN_ITEM_NAME+" like '%"+s+"%'",null);
        res.moveToFirst();
        while (!res.isAfterLast()){
            listOrder.add(new Order(
                    res.getInt(res.getColumnIndex(ORDER_COLUMN_ID)),
                    res.getString(res.getColumnIndex(ORDER_COLUMN_ITEM_NAME)),
                    res.getString(res.getColumnIndex(ORDER_COLUMN_DATE)),
                    res.getDouble(res.getColumnIndex(ORDER_COLUMN_PRICE)),
                    res.getInt(res.getColumnIndex(ORDER_COLUMN_RATING))
            ));
            res.moveToNext();
        }
        db.close();
        return listOrder;
    }
}
