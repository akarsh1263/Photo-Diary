package application.example.photodiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SepView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sep_view);

        //setting up action bar with back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        int id = i.getIntExtra("id",-1);

        //loading database
        DiaryDbHelper dbh=new DiaryDbHelper(this);
        SQLiteDatabase sqld=dbh.getReadableDatabase();
        Cursor c=sqld.query("photo_diary_table",null,"id=?",new String[]{String.valueOf(id)},null,null,null);
        c.moveToFirst();

        //getting data for a single record from the database
        int dati=c.getColumnIndex("date");
        int imi=c.getColumnIndex("pic");
        int ni=c.getColumnIndex("note");

        //setting up retreived data in the views
        TextView date_view=findViewById(R.id.date);
        date_view.setText(c.getString(dati));
        byte[] bi=c.getBlob(imi);
        Bitmap bmp= BitmapFactory.decodeByteArray(bi,0,bi.length);
        ImageView img_view=findViewById(R.id.image);
        img_view.setImageBitmap(bmp);
        TextView note_view=findViewById(R.id.capt);
        note_view.setText(c.getString(ni));
        c.close();

        //setting up event listener for edit button
        Button edit=findViewById(R.id.edit_but);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SepView.this,Add_Edit.class);
                i.putExtra("purpose","edit");
                i.putExtra("id",id);
                startActivity(i);
            }
        });

        //setting up event listener for delete button
        Button delete=findViewById(R.id.del_but);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //setting up dialog box
                Dialog dialog = new Dialog(SepView.this);
                dialog.setContentView(R.layout.del_dialog);
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.del_bg));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();

                //setting up event listener for yes button
                Button yes=dialog.findViewById(R.id.yesdel);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //loading up database
                        DiaryDbHelper dbh=new DiaryDbHelper(SepView.this);
                        SQLiteDatabase sqld2=dbh.getWritableDatabase();

                        //deleting record and starting another activity
                        sqld2.delete("photo_diary_table", "id=?", new String[]{String.valueOf(id)});
                        Intent im=new Intent(SepView.this,MainActivity.class);
                        startActivity(im);
                    }
                });

                //setting up event listener for new button
                Button no=dialog.findViewById(R.id.nodel);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

    //starting Gallery activity on clicking back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent im=new Intent(SepView.this,Gallery.class);
        startActivity(im);
        return super.onOptionsItemSelected(item);
    }
}