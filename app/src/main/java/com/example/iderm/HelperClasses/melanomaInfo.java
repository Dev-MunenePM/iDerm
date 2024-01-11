package com.example.iderm.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.two.R;

import java.util.ArrayList;

public class melanomaInfo extends RecyclerView.Adapter<melanomaInfo.diseaseHold>  {

    ArrayList<infoHelper> diseases;
    final private ListItemClickListener mOnClickListener;

    public melanomaInfo(ArrayList<infoHelper> diseases, ListItemClickListener listener) {
        this.diseases = diseases;
        mOnClickListener = listener;
    }

    @NonNull

    @Override
    public diseaseHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diseaserecylercard, parent, false);
        return new diseaseHold(view);

    }

    @Override
    public void onBindViewHolder(@NonNull diseaseHold holder, int position) {


        infoHelper infoHelper = diseases.get(position);
        holder.image.setImageResource(infoHelper.getImage());
        holder.title.setText(infoHelper.getTitle());
        holder.relativeLayout.setBackground(infoHelper.getgradient());
    }

    @Override
    public int getItemCount() {
        return diseases.size();

    }

    public interface ListItemClickListener {
        void onphoneListClick(int clickedItemIndex);
    }

    public class diseaseHold extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView image;
        TextView title;
        RelativeLayout relativeLayout;


        public diseaseHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //hooks

            image = itemView.findViewById(R.id.phone_image);
            title = itemView.findViewById(R.id.phone_title);
            relativeLayout = itemView.findViewById(R.id.background_color);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onphoneListClick(clickedPosition);
        }
    }

}
