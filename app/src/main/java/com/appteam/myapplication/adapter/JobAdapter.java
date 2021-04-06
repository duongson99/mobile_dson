package com.appteam.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appteam.myapplication.R;
import com.appteam.myapplication.model.Job;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobAdapterViewHolder> {
    private List<Job> listJob = new ArrayList<>();
    public void addList(Job job){
        this.listJob.add(job);
        notifyItemChanged(listJob.size()-1);
    }
    @NonNull
    @Override
    public JobAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapterViewHolder holder, int position) {
        holder.bind(listJob.get(position),position);
    }

    @Override
    public int getItemCount() {
        return listJob.size();
    }

    class JobAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName,tvSalary,tvDate,tvActivated;
        ImageView imageView;
        Button btnDelete;
        int pos;
        public JobAdapterViewHolder(@NonNull View itemview) {
            super(itemview);
            tvName = itemview.findViewById(R.id.tv_name);
            tvSalary = itemview.findViewById(R.id.tv_salary);
            tvDate = itemview.findViewById(R.id.tv_date_created);
            tvActivated = itemview.findViewById(R.id.tv_activated);
            imageView = itemview.findViewById(R.id.image);
            btnDelete = itemview.findViewById(R.id.btn_delete);
        }
        public void bind(Job job,int pos){
            this.pos = pos;
            tvName.setText(job.getName());
            tvSalary.setText(String.valueOf(job.getSalary()));
            tvDate.setText(job.getDateCreated());
            tvActivated.setText(job.isActivated() ? "Activated" : "Inactivated");
            imageView.setImageURI(job.getImage());
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            delete(pos);
        }
    }
    private void delete(int position){
        listJob.remove(position);
        notifyDataSetChanged();
    }
}
