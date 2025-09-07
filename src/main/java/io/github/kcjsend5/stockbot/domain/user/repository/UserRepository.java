package io.github.kcjsend5.stockbot.domain.user.repository;

import io.github.kcjsend5.stockbot.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //이메일로 유저 검색
    Optional<User> findByEmail(String email);
}
