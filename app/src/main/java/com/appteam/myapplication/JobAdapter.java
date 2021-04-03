package com.appteam.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appteam.myapplication.databinding.ItemJobBinding;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> listJob;
    ItemJobBinding binding;

    public JobAdapter(List<Job> listJob) {
        this.listJob = listJob;
    }

    class JobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void bind(Job job,int position){
            this.position = position;
            binding.btnDelete.setOnClickListener(this);
//            binding.image.setImageURI(job.getImage());
            binding.name.setText(job.getName());
            binding.salary.setText(String.valueOf(job.getSalary()));
            binding.dateCreate.setText(job.getDate_created().toString());
            binding.activated.setText(job.isActivated()?"Activated":"Inactivated");
        }

        @Override
        public void onClick(View v) {
            removeJob(position);
        }
    }
    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemJobBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false);
        return new JobViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        holder.bind(listJob.get(position),position);
    }

    @Override
    public int getItemCount() {
        return listJob.size();
    }
    public void removeJob(int position){
        listJob.remove(position);
        notifyItemChanged(position);
    }
    public void updateList(Job job){
        listJob.add(job);
    }
}
