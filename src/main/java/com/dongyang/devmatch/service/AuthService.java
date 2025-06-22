package com.dongyang.devmatch.service;


import com.dongyang.devmatch.dto.SignupRequestDto;
import com.dongyang.devmatch.entity.User;
import com.dongyang.devmatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean signup(SignupRequestDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return false;
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return true;
    }
}
