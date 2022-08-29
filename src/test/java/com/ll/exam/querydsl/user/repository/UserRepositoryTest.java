package com.ll.exam.querydsl.user.repository;


import com.ll.exam.querydsl.user.entity.SiteUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 클래스에 트랜잭셔널을 붙이면, 클래스의 각 테스트케이스에 전부 트랜잭셔널이 붙은 것과 동일.
// @Test + @Transactional 조합은 자동으로 롤백을 유발시켜준다.
@ActiveProfiles("test")
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
    void find() {
        SiteUser qslUser = userRepository.getQslUser(1L);
        System.out.println("qslUser.getUsername() = " + qslUser.getUsername());
        assertThat(qslUser.getUsername()).isEqualTo("user1");

    }

    @Test
    @DisplayName("모든 회원의 수")
    void count() {
        Long qslCount = userRepository.getQslCount();
        assertThat(qslCount).isEqualTo(2);

    }

    @Test
    @DisplayName("가장 오래된 회원")
    void orderUser() {
        SiteUser findUser = userRepository.getQslOrderUser();
        System.out.println(findUser.getUsername());
        assertThat(findUser.getUsername()).isEqualTo("user1");
    }

}

