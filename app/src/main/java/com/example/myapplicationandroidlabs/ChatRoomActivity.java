package com.example.myapplicationandroidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        ListView myList = findViewById(R.id.ChatList);
        myList.setAdapter(myAdapter = new MyListAdapter());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);



        Button send = findViewById(R.id.ButtonSend);
        Button receive = findViewById(R.id.ButtonReceive);
        EditText messageBox = findViewById(R.id.MessageBox);

        send.setOnClickListener(click -> {
            messages.add(new Message(true,messageBox.getText().toString()));
            messageBox.setText("");
            myAdapter.notifyDataSetChanged();
        });
        receive.setOnClickListener(click -> {
            messages.add(new Message(false ,messageBox.getText().toString()));
            messageBox.setText("");
            myAdapter.notifyDataSetChanged();
        });

        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            alertDialogBuilder.setTitle(R.string.alertTitle);
            alertDialogBuilder.setMessage(getString(R.string.alertRow )+ position + "\n"+getString(R.string.alertID)+ id);
            alertDialogBuilder.setPositiveButton(R.string.Yes, (click, arg) -> {
                messages.remove(position);
                myAdapter.notifyDataSetChanged();
            });
            alertDialogBuilder.setNegativeButton(R.string.No, (click, arg) -> {  });
            alertDialogBuilder.create().show();
            return true;
        });
    }


    private class Message extends Object {
        private boolean sent;
        private String text;

        public Message(boolean sent, String text){ setSent(sent); setText(text);}
        public void setText(String text) { this.text = text; }

        public String getText() { return text; }

        public void setSent(boolean sent) { this.sent = sent; }

        public boolean isSent() { return sent; }
    }


    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return messages.size();
        }

        public Message getItem(int position) {return messages.get(position); }

        public long getItemId(int position) {
            return (long) position;
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
}