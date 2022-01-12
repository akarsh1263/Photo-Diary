package application.example.photodiary;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    SQLiteDatabase sqld;
    ListenerInter li;
    public ImageAdapter(SQLiteDatabase db){
        sqld=db;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.image_layout,parent,false);
        return new ImageViewHolder(view);
    }
    public void setInterListener(ListenerInter list){
        li=list;
    }
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Cursor c=sqld.query("photo_diary_table",null,null,null,null,null,null);
        int l=c.getCount();
        c.moveToPosition(l-1-position);
        int idi=c.getColumnIndex("id");
        int di=c.getColumnIndex("date");
        int ni=c.getColumnIndex("note");
        int ii=c.getColumnIndex("pic");
        int idt=c.getInt(idi);
        holder.date_text.setText(c.getString(di));
        holder.note_text.setText(c.getString(ni));
        byte[] bi=c.getBlob(ii);
        Bitmap bmp=BitmapFactory.decodeByteArray(bi,0,bi.length);
        holder.img.setImageBitmap(bmp);
        c.close();
        holder.dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                li.delClicked(idt);
            }
        });
        holder.edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                li.editClicked(idt);
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

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView date_text;
        TextView note_text;
        ImageView img;
        Button edt;
        Button dlt;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            date_text=itemView.findViewById(R.id.datetxt);
            note_text=itemView.findViewById(R.id.notetxt);
            img=(ImageView) itemView.findViewById(R.id.pic);
            edt=itemView.findViewById(R.id.edtbt);
            dlt=itemView.findViewById(R.id.dltbt);
        }
    }
}