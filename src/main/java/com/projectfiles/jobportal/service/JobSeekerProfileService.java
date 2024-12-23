package com.projectfiles.jobportal.service;

import com.projectfiles.jobportal.entity.JobSeekerProfile;
import com.projectfiles.jobportal.entity.Users;
import com.projectfiles.jobportal.repository.JobSeekerProfileRepository;
import com.projectfiles.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UsersRepository usersRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<JobSeekerProfile> getOne(Integer id) {
        return jobSeekerProfileRepository.findById(id);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currUsername = authentication.getName();
            Users users =  usersRepository.findByEmail(currUsername).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            Optional<JobSeekerProfile> jobSeekerProfile = getOne(users.getUserId());
            return jobSeekerProfile.orElse(null);
        }

        return null;
    }

}