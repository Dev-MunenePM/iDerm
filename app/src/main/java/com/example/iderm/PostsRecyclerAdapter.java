package com.example.iderm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<Postinfo> messageList;
    private ArrayList<Postinfo> messageListFull;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public PostsRecyclerAdapter(Context mContext, ArrayList<Postinfo> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
        messageListFull=new ArrayList<>(messageList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(messageList.get(position).getImageUrl())
                .into(holder.imageView);
        holder.textView.setText(messageList.get(position).getOwnerName());

        holder.textViewTime.setText(
                "Posted on" + ":" + " " + messageList.get(position).getTimePosted()
        );

    }

    @Override
    public int getItemCount() {

        return messageList.size();
    }
    private Filter examplefilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Postinfo> filterlist=new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filterlist.addAll(messageListFull);
            }
            else{
                String pattrn=constraint.toString().toLowerCase().trim();
                for(Postinfo item :messageListFull){
                    if(item.getNumber().toLowerCase().contains(pattrn)||item.getOwnerName().toLowerCase().contains(pattrn)||item.getType().toLowerCase()
                            .contains(pattrn)){
                        filterlist.add(item);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filterlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            messageList.clear();
            messageList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    public Filter getFilter() {
        return examplefilter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, textViewTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        int position =getAbsoluteAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);

                        }
                    }
                }
            });

        }
    }

}
