package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
        SQLiteHelper sqliteHelper;

        EditText phoneEditText,messageEditText;
        Button insert_btn,retrieve_btn;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            sqliteHelper = new SQLiteHelper(this);

            phoneEditText = findViewById(R.id.phone_ed);
            messageEditText = findViewById(R.id.message_ed);
            insert_btn= findViewById(R.id.Save);
            retrieve_btn= findViewById(R.id.Retrieve);

            // to insert data at dataBase
            insert_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    SQLiteDatabase db = sqliteHelper.getWritableDatabase();

                    db.beginTransaction();

                    try {
                        String phone = phoneEditText.getText().toString();
                        String message = messageEditText.getText().toString();

                        ContentValues values = new ContentValues();
                        values.put("phone", phone);
                        values.put("message", message);

                        db.insert("MyTable", null, values);

                        db.setTransactionSuccessful();

                    } finally {
                        db.endTransaction();
                    }

                    phoneEditText.setText("");
                    messageEditText.setText("");
                }
            });


             // to retrieve data from dataBase
            retrieve_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    SQLiteDatabase db = sqliteHelper.getReadableDatabase();
                    // Query to get the last inserted row
                    String query = "SELECT * FROM MyTable ORDER BY rowid DESC LIMIT 1";

                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor.moveToFirst()) {
                        int phoneIdx = cursor.getColumnIndex("phone");
                        int messageIdx = cursor.getColumnIndex("message");

                        if (phoneIdx != -1 && messageIdx != -1) {
                            String phone = cursor.getString(phoneIdx);
                            String message = cursor.getString(messageIdx);

                            phoneEditText.setText(phone);
                            messageEditText.setText(message);
                        }
                    }
                    cursor.close();
                }
            });

        }
}