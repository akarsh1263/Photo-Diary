package application.example.photodiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListenerInter{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DiaryDbHelper dbh=new DiaryDbHelper(this);
        SQLiteDatabase sqld=dbh.getReadableDatabase();
        Cursor c = sqld.query("photo_diary_table", null, null, null, null, null, null);
        int n = c.getCount();
        c.close();
        if(n>0){
            TextView wel=findViewById(R.id.welc);
            wel.setVisibility(View.INVISIBLE);
        }
        RecyclerView imglist=(RecyclerView) findViewById(R.id.plister);
        imglist.setLayoutManager(new LinearLayoutManager(this));
        ImageAdapter ia=new ImageAdapter(sqld);
        imglist.setAdapter(ia);
        ia.setInterListener(this);
    }
    public void onAdd(View view) {
        Intent i=new Intent(this,Add_Edit.class);
        i.putExtra("purpose","add");
        startActivity(i);
    }
    @Override
    public void delClicked(int id){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.del_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.del_bg));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        Button yes=dialog.findViewById(R.id.yesdel);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaryDbHelper dbh=new DiaryDbHelper(MainActivity.this);
                SQLiteDatabase sqld=dbh.getWritableDatabase();
                sqld.delete("photo_diary_table", "id=?", new String[]{String.valueOf(id)});
                MainActivity.this.recreate();
            }
        });
        Button no=dialog.findViewById(R.id.nodel);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
    @Override
    public void editClicked(int id){
        Intent i=new Intent(this,Add_Edit.class);
        i.putExtra("purpose","edit");
        i.putExtra("id",id);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.opt_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DiaryDbHelper dbh = new DiaryDbHelper(this);
        SQLiteDatabase sqld = dbh.getReadableDatabase();
        Cursor c = sqld.query("photo_diary_table", null, null, null, null, null, null);
        int n = c.getCount();
        c.close();
        Intent i = new Intent(MainActivity.this, Gallery.class);
        if (n > 0)
            startActivity(i);
        else
            Toast.makeText(this, "No photo present. Add a photo", Toast.LENGTH_LONG).show();
        return true;
    }
}