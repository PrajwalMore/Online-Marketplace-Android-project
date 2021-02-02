package com.example.admin.BlockView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.programingViewHolder> {
    ArrayList<String> data;
    ArrayList<String> icons;
    ArrayList<String> num;
    String btnCall;
    Context context;

    public ProgrammingAdapter(ArrayList<String> data, ArrayList<String> icons, ArrayList<String> num) {
        this.data = data;
        this.icons = icons;
        this.num = num;
    }

    @NonNull
    @Override
    public programingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.customlayout, parent, false);
        return new programingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull programingViewHolder holder, int position) {
        String title = data.get(position);
        holder.textView.setText(title);
        //int img= icons.get(position);
        // holder.imageView.setImageResource(icons.get(position));
        try {
            //holder.imageView.setImageURI(Uri.parse(icons.get(position)));
            //holder.imageView.setImageBitmap(icons.get(position));
            Picasso.get().load(String.valueOf(icons.get(position))).fit().into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.textView2.setText(num.get(position) + "\nCall Seller");


//holder.textView2.
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class programingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, textView2;

        public programingViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            //imageView2 = itemView.findViewById(R.id.imageView2);
            imageView = itemView.findViewById(R.id.img);
            textView = itemView.findViewById(R.id.txt);
            textView2 = itemView.findViewById(R.id.btn);
            CardView cardView;
            cardView=itemView.findViewById(R.id.card);
           // cardView.setRadius(10);
            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("tel:" + textView2.getText().toString().replace("\nCall Seller","")));
                    if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        ActivityCompat.requestPermissions((Activity) context, new String[]{
                                Manifest.permission.CALL_PHONE  }, 2);
                        Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    context.startActivity(intent);
                }
            });
        }
    }

}
