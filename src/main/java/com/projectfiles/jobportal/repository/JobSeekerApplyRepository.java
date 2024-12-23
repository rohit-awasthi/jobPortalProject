package com.projectfiles.jobportal.repository;

import com.projectfiles.jobportal.entity.JobPostActivity;
import com.projectfiles.jobportal.entity.JobSeekerApply;
import com.projectfiles.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {

    List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

    List<JobSeekerApply> findByJob(JobPostActivity job);

}