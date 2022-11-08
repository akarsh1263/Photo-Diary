package application.example.photodiary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//setting up adapter vor gallery view of the app
public class Gal_Adapter extends RecyclerView.Adapter<Gal_Adapter.SlimAdapter>{
    SQLiteDatabase sqld;
    GalListener li;
    public Gal_Adapter(SQLiteDatabase db){
        sqld=db;
    }
    @NonNull
    @Override
    public SlimAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.slimg,parent,false);
        return new SlimAdapter(view);
    }

    //setting up event listener interface
    public void setInterListener(GalListener list){
        li=list;
    }

    //binding images to the view
    @Override
    public void onBindViewHolder(@NonNull SlimAdapter holder, int position) {
        //getting the picture
        Cursor c=sqld.query("photo_diary_table",null,null,null,null,null,null);
        int l=c.getCount();
        c.moveToPosition(l-1-position);
        int idi=c.getColumnIndex("id");
        int ii=c.getColumnIndex("pic");
        int idt=c.getInt(idi);
        byte[] bi=c.getBlob(ii);
        Bitmap bmp= BitmapFactory.decodeByteArray(bi,0,bi.length);

        //binding image to the view
        holder.img.setImageBitmap(bmp);
        c.close();

        //setting up event listener for the picture
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                li.imgClicked(idt);
            }
        });
    }

    //getting no. of records/items
    @Override
    public int getItemCount() {
        Cursor c=sqld.query("photo_diary_table",null,null,null,null,null,null);
        int n=c.getCount();
        c.close();
        return n;
    }

    public class SlimAdapter extends RecyclerView.ViewHolder {
        ImageView img;
        public SlimAdapter(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.slayer);
        }
    }
}
