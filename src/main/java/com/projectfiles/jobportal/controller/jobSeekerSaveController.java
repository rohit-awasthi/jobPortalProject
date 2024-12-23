package com.projectfiles.jobportal.controller;

import com.projectfiles.jobportal.entity.JobPostActivity;
import com.projectfiles.jobportal.entity.JobSeekerProfile;
import com.projectfiles.jobportal.entity.JobSeekerSave;
import com.projectfiles.jobportal.entity.Users;
import com.projectfiles.jobportal.service.JobPostActivityService;
import com.projectfiles.jobportal.service.JobSeekerProfileService;
import com.projectfiles.jobportal.service.JobSeekerSaveService;
import com.projectfiles.jobportal.service.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class jobSeekerSaveController {

    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public jobSeekerSaveController(UsersService usersService, JobSeekerProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id, JobSeekerSave jobSeekerSave) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users user =  usersService.findByEmail(username);
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
            if (seekerProfile.isPresent() && jobPostActivity != null) {
                jobSeekerSave = new JobSeekerSave();
                jobSeekerSave.setUserId(seekerProfile.get());
                jobSeekerSave.setJob(jobPostActivity);
            } else {
                throw new RuntimeException("User not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);
        }

        return "redirect:/dashboard/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model) {
        List<JobPostActivity> jobPost = new ArrayList<>();
        Object currentUserProfile = usersService.getCurrentUserProfile();

        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
        for(JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
            jobPost.add(jobSeekerSave.getJob());
        }

        model.addAttribute("jobPost", jobPost);
        model.addAttribute("user", currentUserProfile);

        return "saved-jobs";
    }

}