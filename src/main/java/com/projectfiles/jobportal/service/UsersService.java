package com.projectfiles.jobportal.service;

import com.projectfiles.jobportal.entity.JobSeekerProfile;
import com.projectfiles.jobportal.entity.RecruiterProfile;
import com.projectfiles.jobportal.entity.Users;
import com.projectfiles.jobportal.repository.JobSeekerProfileRepository;
import com.projectfiles.jobportal.repository.RecruiterProfileRepository;
import com.projectfiles.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, JobSeekerProfileRepository jobSeekerProfileRepository, RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users addNew(Users user) {
        user.setActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int userTypeId = user.getUserTypeId().getUserTypeId();
        Users savedUser = usersRepository.save(user);
        if(userTypeId == 1) {
            recruiterProfileRepository.save(new RecruiterProfile(savedUser));
        }
        else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
        }

        return savedUser;
    }

    public Optional<Users> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user with this email exists!"));
            int userId = users.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                return recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
            }

            else {
                return jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
            }
        }

        return null;
    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            return usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user with this email exists!"));
        }

        return null;
    }

    public Users findByEmail(String currUsername) {
        return usersRepository.findByEmail(currUsername).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

}