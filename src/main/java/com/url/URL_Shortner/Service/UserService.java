package com.url.URL_Shortner.Service;

import com.url.URL_Shortner.DTO.RegisterUserRequest;
import com.url.URL_Shortner.Entity.UserResponse;
import com.url.URL_Shortner.Entity.Users;
import com.url.URL_Shortner.Repository.UserDetailsRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(RegisterUserRequest registerUserRequest){
        if(userDetailsRepository.findByEmail(registerUserRequest.getUsername()).isPresent()){
            throw new RuntimeException("User Already Exist");
        }
        Users users = new Users();
        users.setEmail(registerUserRequest.getEmail());
        users.setRole(registerUserRequest.getRole());
        users.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        Users savedUser = userDetailsRepository.save(users);
        return new UserResponse(savedUser.getId(), savedUser.getUsername(),savedUser.getRole().name());
    }


    public Users findByEmail(String name) {
        return userDetailsRepository.findByEmail(name).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + name)
        );
    }


}

