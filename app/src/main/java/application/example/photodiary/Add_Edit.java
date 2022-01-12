package application.example.photodiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Add_Edit extends AppCompatActivity {
    ImageView img;
    String dates;
    String note;
    Bitmap storeimg;
    byte[]imgbt;
    int id;
    boolean gal=true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Intent i=getIntent();
        String purp=i.getStringExtra("purpose");
        if(purp.equals("edit"))
            id=i.getIntExtra("id",-1);
        img = (ImageView) findViewById(R.id.photo);
        EditText noter=(EditText)findViewById(R.id.noter);
        if(purp.equals("add")) {
            try {
                if(gal)
                    gallery();
                else {
                    Bitmap bmp = BitmapFactory.decodeByteArray(imgbt, 0, imgbt.length);
                    img.setImageBitmap(bmp);
                }
            } catch (Exception e) {
                Toast.makeText(Add_Edit.this, "Some error occured", Toast.LENGTH_LONG).show();
            }
        }
        else if(purp.equals("edit")){
                DiaryDbHelper dbh = new DiaryDbHelper(Add_Edit.this);
                SQLiteDatabase sqld = dbh.getReadableDatabase();
                Cursor c = sqld.query("photo_diary_table", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
                c.moveToFirst();
                int imi = c.getColumnIndex("pic");
                byte[] bi = c.getBlob(imi);
                Bitmap bmp = BitmapFactory.decodeByteArray(bi, 0, bi.length);
                img.setImageBitmap(bmp);
                int ni = c.getColumnIndex("note");
                String nt = c.getString(ni);
                noter.setText(nt);
                c.close();
        }
        Button saver=(Button) findViewById(R.id.saver);
        saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(purp.equals("add")) {
                    String tknt = noter.getText().toString();
                    if (tknt.trim().equals(""))
                        Toast.makeText(Add_Edit.this, "Please write a note", Toast.LENGTH_LONG).show();
                    else {
                        Date date = new Date();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = df.format(date.getTime());
                        dates = formattedDate;
                        note = tknt;
                        try {
                            fileWork();
                        } catch (Exception e) {
                            Toast.makeText(Add_Edit.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                        Intent im = new Intent(Add_Edit.this, MainActivity.class);
                        startActivity(im);
                    }
                }
                else if(purp.equals("edit")){
                    DiaryDbHelper dbh=new DiaryDbHelper(Add_Edit.this);
                    SQLiteDatabase sqld=dbh.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("note",noter.getText().toString());
                    sqld.update("photo_diary_table", values, "id=?", new String[]{String.valueOf(id)});
                    Intent im = new Intent(Add_Edit.this, MainActivity.class);
                    startActivity(im);
                }
            }
        });
        Button discard=(Button) findViewById(R.id.disc);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent im=new Intent(Add_Edit.this,MainActivity.class);
                startActivity(im);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByteArray("picarray",imgbt);
        outState.putBoolean("gal",false);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imgbt=savedInstanceState.getByteArray("picarray");
        gal=savedInstanceState.getBoolean("gal");
    }

    void gallery()throws Exception{
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,""),2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            img.setImageURI(uri);
            try {
                storeimg=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                ByteArrayOutputStream baros=new ByteArrayOutputStream();
                storeimg.compress(Bitmap.CompressFormat.JPEG,100,baros);
                imgbt= baros.toByteArray();
            } catch (IOException e) {
                Toast.makeText(Add_Edit.this,"Some error occured", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Intent im=new Intent(Add_Edit.this,MainActivity.class);
            startActivity(im);
        }
    }
    public void fileWork()throws Exception {
            DiaryDbHelper ddh = new DiaryDbHelper(this);
            SQLiteDatabase sqld = ddh.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("note", note);
            values.put("date", dates);
            values.put("pic", imgbt);
            sqld.insert("photo_diary_table", null, values);
    }
}