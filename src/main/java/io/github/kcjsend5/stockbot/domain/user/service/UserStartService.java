package io.github.kcjsend5.stockbot.domain.user.service;

import io.github.kcjsend5.stockbot.domain.user.domain.User;
import io.github.kcjsend5.stockbot.domain.user.repository.UserRepository;
import io.github.kcjsend5.stockbot.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserStartService implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${master.password}")
    private String password;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        String masterPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .userName("Master")
                .email("master@naver.com")
                .password(masterPassword)
                .role(Role.MANAGER)
                .build();

        userRepository.save(user);
    }
}
