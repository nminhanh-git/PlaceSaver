package corp.nminhanh.placesaver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Minh Anh on 1/11/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "Item_Database";

    //Table name
    private static final String TABLE_NAME = "item";

    //Table's column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_IMAGE_PATH = "image_source";
    private static final String KEY_DESCRIPTION = "description";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_TYPE + " TEXT, "
                + KEY_IMAGE_PATH + " TEXT, "
                + KEY_DESCRIPTION + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    long AddItem(Item anItem) {
        SQLiteDatabase db = null;
        long id = 0;
        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, anItem.getName());
            values.put(KEY_TYPE, anItem.getType());
            values.put(KEY_IMAGE_PATH, anItem.getPath());
            values.put(KEY_DESCRIPTION, anItem.getDescription());

            id = db.insert(TABLE_NAME, null, values);
        } catch (Exception ex) {
            Log.i("error", "add item: " + ex.getMessage());
        }
        return id;
    }

    public ArrayList<Item> getAllItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Item> itemsList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String type = cursor.getString(2);
                String imagePath = cursor.getString(3);
                String description = cursor.getString(4);
                Item anItem = new Item(id, name, type, description, imagePath);
                itemsList.add(anItem);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return itemsList;
    }

    public ArrayList<Item> getAllQueryItem(String itemType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Item> itemsList = new ArrayList<>();
        String[] selectedColumns = {KEY_ID, KEY_NAME, KEY_TYPE, KEY_DESCRIPTION, KEY_IMAGE_PATH};
        String[] arguments = {itemType};
        Cursor cursor = db.query(TABLE_NAME, selectedColumns, KEY_TYPE + " = ?", arguments, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String type = cursor.getString(2);
                String description = cursor.getString(3);
                String imagePath = cursor.getString(4);
                Item anItem = new Item(id, name, type, description, imagePath);
                itemsList.add(anItem);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return itemsList;
    }

    public int updateItem(Item anItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, anItem.getName());
        contentValues.put(KEY_TYPE, anItem.getType());
        contentValues.put(KEY_DESCRIPTION, anItem.getDescription());
        contentValues.put(KEY_IMAGE_PATH, anItem.getPath());
        return db.update(TABLE_NAME, contentValues, KEY_ID + " = ?", new String[]{String.valueOf(anItem.getId())});
    }

    public int deleteItem(Item anItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(anItem.getId())});
    }
}