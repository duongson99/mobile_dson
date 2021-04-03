package com.appteam.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JobDao {
    @Query("select * from job")
    List<Job> getAllJob();

    @Insert
    void insertJob(Job job);

    @Delete
    void deleteJob(Job job);
}
