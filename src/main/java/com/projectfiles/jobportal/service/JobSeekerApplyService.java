package com.projectfiles.jobportal.service;

import com.projectfiles.jobportal.entity.JobPostActivity;
import com.projectfiles.jobportal.entity.JobSeekerApply;
import com.projectfiles.jobportal.entity.JobSeekerProfile;
import com.projectfiles.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidateJobs(JobSeekerProfile userAccountId) {
        return jobSeekerApplyRepository.findByUserId(userAccountId);
    }

    public List<JobSeekerApply> getJobCandidates(JobPostActivity job) {
        return jobSeekerApplyRepository.findByJob(job);
    }

    public void addNew(JobSeekerApply jobSeekerApply) {
        jobSeekerApplyRepository.save(jobSeekerApply);
    }

}