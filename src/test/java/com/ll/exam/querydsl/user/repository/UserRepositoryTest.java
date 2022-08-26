package com.ll.exam.querydsl.user.repository;


import com.ll.exam.querydsl.user.entity.SiteUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 생성")
    void t1() {
        SiteUser u1 = new SiteUser(null, "user3", "{noop}12345", "user3@test.com");
        SiteUser u2 = new SiteUser(null, "user4", "{noop}12345", "user4@test.com");

        userRepository.saveAll(Arrays.asList(u1, u2));
    }

    @Test
    @DisplayName("회원 찾기")
    void find(){
        SiteUser qslUser = userRepository.getQslUser(1L);
        System.out.println("qslUser.getUsername() = " + qslUser.getUsername());
        assertThat(qslUser.getUsername()).isEqualTo("user1");

    }
}