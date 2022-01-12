package application.example.photodiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class Gallery extends AppCompatActivity implements GalListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        DiaryDbHelper dbh=new DiaryDbHelper(this);
        SQLiteDatabase sqld=dbh.getReadableDatabase();
        RecyclerView imglist=(RecyclerView) findViewById(R.id.imsm);
        GridLayoutManager glm=new GridLayoutManager(this,5,GridLayoutManager.VERTICAL,false);
        imglist.setLayoutManager(glm);
        Gal_Adapter ia=new Gal_Adapter(sqld);
        imglist.setAdapter(ia);
        ia.setInterListener(this);
    }

    @Override
    public void imgClicked(int id) {
        Intent im=new Intent(Gallery.this,SepView.class);
        im.putExtra("id",id);
        startActivity(im);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent im=new Intent(Gallery.this,MainActivity.class);
        startActivity(im);
        return super.onOptionsItemSelected(item);
    }
}