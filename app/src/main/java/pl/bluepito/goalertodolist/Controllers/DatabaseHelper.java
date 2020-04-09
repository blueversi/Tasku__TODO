package pl.bluepito.goalertodolist.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pl.bluepito.goalertodolist.Models.Category;
import pl.bluepito.goalertodolist.Models.Task;

public class DatabaseHelper extends SQLiteOpenHelper {

    //---- LOG G TAG----
    private static final String TAG = "DatabaseHelper";

    //----  DATABASE NAME AND VERSION ----
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sTodo_db";

    //---- TABLE NAMES ----
    private static final String TABLE_TASK = "task_table";
    private static final String TABLE_CAT = "cat_table";
    private static final String TABLE_TASK_TAG = "task_cat_table";

    //---- COLUMN NAMES ----

    // -general-
    private static final String COL_ID = "ID";
    private static final String COL_INSERT_DATE = "insertData";

    // -task-
    private static final String COL_TASK = "task";
    private static final String COL_STATUS = "status";

    // -categories-
    private static final String COL_CAT_NAME = "cat_name";

    // -task_categories-
    private static final String COL_TASK_ID = "task_id";
    private static final String COL_CAT_ID = "cat_id";


    //---- FINAL CREATE TABLE QUERIES ----
    private static final String CREATE_TABLE_TASK = "CREATE TABLE " + TABLE_TASK + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TASK + " TEXT, "
            + COL_STATUS + " INTEGER, " + COL_INSERT_DATE + " DATATIME" + ")";

    private static final String CREATE_TABLE_CAT = "CREATE TABLE " + TABLE_CAT + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_CAT_NAME + " TEXT, "
            + COL_INSERT_DATE + " DATATIME" + ")";

    private static final String CREATE_TABLE_TASK_CAT = "CREATE TABLE " + TABLE_TASK_TAG + "(" + COL_TASK_ID + " INTEGER, "
            + COL_CAT_ID + " INTEGER" + " )";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_CAT);
        db.execSQL(CREATE_TABLE_TASK_CAT);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CAT_NAME, "default");
        contentValues.put(COL_INSERT_DATE, getDateTime());
        long cat_id = db.insert(TABLE_CAT, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_TAG);

    }

    //---- TASK ADD ----

    public long addTask(Task task, long cat_ids[]) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TASK, task.getTaskName());
        contentValues.put(COL_STATUS, task.getStatus());
        contentValues.put(COL_INSERT_DATE, getDateTime());

        Log.d(TAG, "addTask: Adding task to " + TABLE_TASK);

        long task_id = db.insert(TABLE_TASK, null, contentValues);

        for (long cat_id : cat_ids) {
            createTaskCategory(task_id, cat_id);
        }

        return task_id;
    }

    public long addTask(Task task, long cat_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TASK, task.getTaskName());
        contentValues.put(COL_STATUS, task.getStatus());
        contentValues.put(COL_INSERT_DATE, getDateTime());

        Log.d(TAG, "addTask: Adding task to " + TABLE_TASK);

        long task_id = db.insert(TABLE_TASK, null, contentValues);
        createTaskCategory(task_id, cat_id);

        return task_id;
    }

    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TASK, task.getTaskName());
        contentValues.put(COL_STATUS, task.getStatus());
        contentValues.put(COL_INSERT_DATE, getDateTime());

        Log.d(TAG, "addTask: Adding task to " + TABLE_TASK);

        long task_id = db.insert(TABLE_TASK, null, contentValues);

        return task_id;
    }

    public long addCategory(Category cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CAT_NAME, cat.getName());
        contentValues.put(COL_INSERT_DATE, getDateTime());

        long cat_id = db.insert(TABLE_CAT, null, contentValues);

        return cat_id;
    }


    //---- TASK GETTERS ----

    public Task getTask(long task_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_TASK + " WHERE "
                + COL_ID + " = " + task_id;

        Cursor data = db.rawQuery(query, null);

        if (data != null) {
            data.moveToFirst();
        }

        Task task = new Task();
        task.setId(data.getInt(data.getColumnIndex(COL_ID)));
        task.setTaskName(data.getString(data.getColumnIndex(COL_TASK)));
        task.setInsertDate(data.getString(data.getColumnIndex(COL_INSERT_DATE)));

        return task;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASK;


        Log.e(TAG, query);

        Cursor data = db.rawQuery(query, null);

        if (data.moveToFirst()) {

            do {
                Task task = new Task();
                task.setId(data.getInt(data.getColumnIndex(COL_ID)));
                task.setTaskName(data.getString(data.getColumnIndex(COL_TASK)));
                task.setInsertDate(data.getString(data.getColumnIndex(COL_INSERT_DATE)));

                tasks.add(task);
            }
            while (data.moveToNext());

        }

        return tasks;
    }

    public ArrayList<Task> getAllTasksByCategory(int catId) {
        ArrayList<Task> tasks = new ArrayList<Task>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASK + " tt, " + TABLE_CAT + " tc, " + TABLE_TASK_TAG + " ttc "
                + " WHERE " + "tc." + COL_ID + " = " + catId + " AND " + "tc." + COL_ID + " = " + "ttc." + COL_CAT_ID
                + " AND " + "tt." + COL_ID + " = " + "ttc." + COL_TASK_ID;

        Log.e(TAG, query);
        // db.execSQL(query);

        Cursor data = db.rawQuery(query, null);

        if (data.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(data.getInt(data.getColumnIndex(COL_TASK_ID)));
                task.setTaskName(data.getString(data.getColumnIndex(COL_TASK)));
                task.setInsertDate(data.getString(data.getColumnIndex(COL_INSERT_DATE)));

                tasks.add(task);
            }
            while (data.moveToNext());
        }

        return tasks;
    }

    //---- CATEGORIES GETTERS ----

    public Category getCategory(long cat_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_CAT + " WHERE "
                + COL_ID + " = " + cat_id;

        Cursor data = db.rawQuery(query, null);

        if (data != null) {
            data.moveToFirst();
        }

        Category cat = new Category();
        cat.setId(data.getInt(data.getColumnIndex(COL_ID)));
        cat.setName(data.getString(data.getColumnIndex(COL_CAT_NAME)));
        cat.setInsertDate(data.getString(data.getColumnIndex(COL_INSERT_DATE)));

        return cat;
    }

    public Category getTaskCategory(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryCatId = "SELECT * FROM " + TABLE_TASK_TAG + " WHERE "
                + COL_TASK_ID + " = " + task.getId();

        try {
            Cursor dataCatId = db.rawQuery(queryCatId, null);
            if (dataCatId != null) {
                dataCatId.moveToFirst();
            }
            int idOfCat = dataCatId.getInt(dataCatId.getColumnIndex(COL_CAT_ID));
            return getCategory(idOfCat);
        } catch ( IndexOutOfBoundsException e){

            return new Category(-1, "no category");
        }

    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CAT;


        Log.e(TAG, query);

        Cursor data = db.rawQuery(query, null);

        if (data.moveToFirst()) {

            do {
                Category cat = new Category();
                cat.setId(data.getInt(data.getColumnIndex(COL_ID)));
                cat.setName(data.getString(data.getColumnIndex(COL_CAT_NAME)));
                cat.setInsertDate(data.getString(data.getColumnIndex(COL_INSERT_DATE)));

                categories.add(cat);
            }
            while (data.moveToNext());

        }
        if (categories.isEmpty()) {
            categories.add(new Category(-1, "No Categories", "00/00/0000"));
            return categories;
        } else {
            return categories;
        }

    }


    public ArrayList<String> getAllCategoriesNames() {
        ArrayList<String> categories = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CAT;


        Log.e(TAG, query);

        Cursor data = db.rawQuery(query, null);

        if (data.moveToFirst()) {

            do {

                categories.add(data.getString(data.getColumnIndex(COL_CAT_NAME)));
            }
            while (data.moveToNext());

        }
        if (categories.isEmpty()) {
            categories.add("No Categories");
            return categories;
        } else {
            return categories;
        }

    }
    //---- UPDATE TASK ----

    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TASK, task.getTaskName());
        contentValues.put(COL_STATUS, task.getStatus());

        long result = db.update(TABLE_TASK, contentValues, COL_ID + " = ?", new String[]{String.valueOf(task.getId())});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateTaskWithCategory(Task task, long longId) {
        //TODO
        return false;
    }

    //---- UPDATE CATEGORY ----

    public boolean updateCat(Category cat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CAT_NAME, cat.getName());

        long result = db.update(TABLE_CAT, contentValues, COL_ID + " = ?", new String[]{String.valueOf(cat.getId())});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //---- DELETE TASK ----

    public void deleteTask(long task_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK, COL_ID + " = ?", new String[]{String.valueOf(task_id)});


    }


    //---- DELETE CATEGORY AND ALL TASK REALATED WITH IT ----

    public void deleteCat(Category cat, boolean delete_all_cat_tasks) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (delete_all_cat_tasks) {
            ArrayList<Task> allCatTasks = getAllTasksByCategory(cat.getId());

            for (Task task : allCatTasks) {
                deleteTask(task.getId());
            }
        }

        db.delete(TABLE_CAT, COL_ID + " = ?", new String[]{String.valueOf(cat.getId())});
    }

    //---- CREATE CATEGORY FOR TASKS ----
    public long createTaskCategory(long task_id, long cat_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TASK_ID, task_id);
        contentValues.put(COL_CAT_ID, cat_id);

        long id = db.insert(TABLE_TASK_TAG, null, contentValues);

        return id;
    }

    //---- CATEGORY UPDATE FOR TASK ----
    public int updateTaskTag(long id, long cat_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CAT_ID, cat_id);

        return db.update(TABLE_TASK, contentValues, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
