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
    public void setInterListener(GalListener list){
        li=list;
    }
    @Override
    public void onBindViewHolder(@NonNull SlimAdapter holder, int position) {
        Cursor c=sqld.query("photo_diary_table",null,null,null,null,null,null);
        int l=c.getCount();
        c.moveToPosition(l-1-position);
        int idi=c.getColumnIndex("id");
        int ii=c.getColumnIndex("pic");
        int idt=c.getInt(idi);
        byte[] bi=c.getBlob(ii);
        Bitmap bmp= BitmapFactory.decodeByteArray(bi,0,bi.length);
        holder.img.setImageBitmap(bmp);
        c.close();
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                li.imgClicked(idt);
            }
        });
    }

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
