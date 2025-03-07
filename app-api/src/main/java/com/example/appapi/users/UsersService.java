package com.example.appapi.users;

import com.example.appapi.users.model.Users;
import com.example.appapi.users.model.UsersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Object signup(UsersDto.SignupRequest dto) {
        
        Map<String, String> errors = new HashMap<>();
        // 이메일 중복 검사
        if (usersRepository.existsByEmail(dto.getEmail())) {
            errors.put("email", "이미 사용 중인 이메일입니다.");
        }
        // 아이디 중복 검사
        if (usersRepository.existsByUserId(dto.getUserId())) {
            errors.put("userId", "이미 사용 중인 아이디입니다.");
        }

        if(!errors.isEmpty()) {
            return errors;
        }

        Users user = usersRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        return UsersDto.SignupResponse.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> result = usersRepository.findByUserId(username);

        if (result.isPresent()) {
            Users users = result.get();
            return users;
        }

        return null;
    }

    @Transactional(readOnly = true)
    public UsersDto.UserResponse read(Long userIdx) {
        Users users = usersRepository.findById(userIdx).orElseThrow();
        return UsersDto.UserResponse.from(users);
    }

    public List<UsersDto.UserResponse> getList() {
        List<Users> userList = usersRepository.findAll();

        return userList.stream().map(UsersDto.UserResponse::from).toList();
    }
}
