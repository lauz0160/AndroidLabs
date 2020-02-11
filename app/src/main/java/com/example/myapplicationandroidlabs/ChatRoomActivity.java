package com.example.myapplicationandroidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> messages = new ArrayList<>(Arrays.asList());
    private MyListAdapter myAdapter;
    SQLiteDatabase db;

    final static String DATABASE_NAME = "MessagesDB";
    final static String TABLE_NAME = "Messages";
    final static String COL_TEXT = "Text";
    final static String COL_BOOL = "isSent";
    final static String COL_ID = "_id";
    final static int VERSION_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        loadDataFromDatabase();

        ListView myList = findViewById(R.id.ChatList);
        myList.setAdapter(myAdapter = new MyListAdapter());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);



        Button send = findViewById(R.id.ButtonSend);
        Button receive = findViewById(R.id.ButtonReceive);
        EditText messageBox = findViewById(R.id.MessageBox);

        send.setOnClickListener(click -> {
            messages.add(buttonAction(messageBox.getText().toString(),true));
            messageBox.setText("");
            myAdapter.notifyDataSetChanged();
        });
        receive.setOnClickListener(click -> {
            messages.add(buttonAction(messageBox.getText().toString(),false));
            messageBox.setText("");
            myAdapter.notifyDataSetChanged();
        });

        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            alertDialogBuilder.setTitle(R.string.alertTitle);
            alertDialogBuilder.setMessage(getString(R.string.alertRow )+ position + "\n"+getString(R.string.alertID)+ id);
            alertDialogBuilder.setPositiveButton(R.string.Yes, (click, arg) -> {
                db.delete(TABLE_NAME, COL_ID + "= ?", new String[] {Long.toString(messages.get(position).getID())});
                messages.remove(position);
                myAdapter.notifyDataSetChanged();
            });
            alertDialogBuilder.setNegativeButton(R.string.No, (click, arg) -> {  });
            alertDialogBuilder.create().show();
            return true;
        });
    }



    private Message buttonAction(String text, boolean bool){

        Message message = new Message(text, bool);

        ContentValues newRow = new ContentValues();
        newRow.put(COL_TEXT,text);
        newRow.put(COL_BOOL,bool);
        long newId = db.insert(TABLE_NAME, null, newRow);
        message.setID(newId);

        return message;
    }




    private class Message extends Object {
        private boolean sent;
        private String text;
        private long id;

        public Message(String text, boolean sent){ this(text ,sent ,0L ); }
        public Message(String text, boolean sent, Long id){ setText(text); setSent(sent); setID(id);}
        public void setText(String text) { this.text = text; }

        public String getText() { return text; }

        public void setSent(boolean sent) { this.sent = sent; }

        public boolean isSent() { return sent; }

        public void setID(Long id){ this.id = id; }

        public long getID(){ return id; }
    }






    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return messages.size();
        }

        public Message getItem(int position) {return messages.get(position); }

        public long getItemId(int position) {
            return messages.get(position).getID();
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            Message message = this.getItem(position);
            TextView tView;

            if (message.isSent()) {
                old = inflater.inflate(R.layout.row_layout_send, parent, false);
                tView = old.findViewById(R.id.textGoesHere);
            } else {
                old = inflater.inflate(R.layout.row_layout_receive, parent, false);
                tView = old.findViewById(R.id.textGoesHere);
            }

            tView.setText(message.getText());
            return old;
        }
    }






    private class MyOpener extends SQLiteOpenHelper {


        public MyOpener(Context ctx){ super(ctx, DATABASE_NAME, null, VERSION_NUM); }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_TEXT + " text,"
                    + COL_BOOL  + " integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }





    private void loadDataFromDatabase()
    {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        String [] columns = {COL_ID, COL_TEXT, COL_BOOL};
        Cursor results = db.query(false,TABLE_NAME, columns, null, null, null, null, null, null);

        int textColumnIndex = results.getColumnIndex(COL_TEXT);
        int boolColIndex = results.getColumnIndex(COL_BOOL);
        int idColIndex = results.getColumnIndex(COL_ID);

        while(results.moveToNext()) {
            String text = results.getString(textColumnIndex);
            boolean bool = (1==results.getInt(boolColIndex));
            long id = results.getLong(idColIndex);
            messages.add(new Message(text,bool,id));
        }

        printCursor(results,VERSION_NUM);
    }




    private void printCursor (Cursor c, int version){
        Log.i("printCursor", "The database version number is: "+db.getVersion());
        Log.i("printCursor", "The number of columns is: "+c.getColumnCount());
        String ColumnName = "";
        for(int i =0; i<c.getColumnCount();i++){
            ColumnName=ColumnName+ c.getColumnName(i)+", ";
        }
        Log.i("printCursor", "The name of the columns: "+ ColumnName);
        Log.i("printCursor", "The number of results is: "+c.getCount());
        String results ="";
        if(c.moveToFirst()){
            do {
                results=results + "{";
                for (int i = 0; i < c.getColumnCount(); i++) {
                    if(i == (c.getColumnCount()-1)) {
                        results = results + c.getString(i)+"";
                    }
                    else{
                        results = results + c.getString(i)+", ";
                    }
                }
                if(!c.isLast()){
                    results = results + "}, ";
                }
                else{
                    results =results +"}";
                }
            }while (c.moveToNext());
        }
        Log.i("printCursor", "The rows of results: " + results);
    }
}