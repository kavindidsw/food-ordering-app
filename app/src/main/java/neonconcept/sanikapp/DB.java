package neonconcept.sanikapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DB(Context context){
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void insertData(String[] column_list, Object[] value_list, String table_name) throws Exception {
        String column_names = "";
        String values_names = "";
        if (column_list.length == value_list.length & table_name != null) {
            for (int index = 0; index < column_list.length; index++) {
                boolean isLast = index == column_list.length -1;
                column_names = column_names +(column_list[index] + (isLast ? "" : ","));
            }

            for (int index = 0; index < value_list.length; index++) {
                boolean isLast = index == value_list.length -1;
                values_names = values_names + ( "'"+ value_list[index] + (isLast ? "'" : "',"));
            }
        }
        if (column_names != null & values_names != null) {
            database.execSQL("INSERT INTO `" + table_name + "` ("+column_names+") VALUES ("+values_names+")");
        }
    }

    public Cursor search(String query) throws Exception{
        Cursor resultSet = database.rawQuery(query,null);
        return resultSet;
    }

}
