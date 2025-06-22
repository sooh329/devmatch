package com.dongyang.devmatch.service;

import com.dongyang.devmatch.entity.User;
import com.dongyang.devmatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // username으로 User 조회
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다: " + username));
    }

    // 유저 탈퇴
    public void deleteByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(userRepository::delete);
    }

}
