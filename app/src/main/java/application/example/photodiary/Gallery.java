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

        //setting up action bar with back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //loading database
        DiaryDbHelper dbh=new DiaryDbHelper(this);
        SQLiteDatabase sqld=dbh.getReadableDatabase();

        //setting up recycler view
        RecyclerView imglist=(RecyclerView) findViewById(R.id.imsm);
        GridLayoutManager glm=new GridLayoutManager(this,5,GridLayoutManager.VERTICAL,false);
        imglist.setLayoutManager(glm);

        //setting up adapter for recycler view
        Gal_Adapter ia=new Gal_Adapter(sqld);
        imglist.setAdapter(ia);

        //setting up listener interface for the adapter
        ia.setInterListener(this);
    }

    //onClick method for clicking image
    @Override
    public void imgClicked(int id) {
        Intent im=new Intent(Gallery.this,SepView.class);
        im.putExtra("id",id);
        startActivity(im);
    }

    //starting main activity on clicking back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent im=new Intent(Gallery.this,MainActivity.class);
        startActivity(im);
        return super.onOptionsItemSelected(item);
    }
}