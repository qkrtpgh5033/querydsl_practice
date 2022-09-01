package com.ll.exam.querydsl.base;

import com.ll.exam.querydsl.interestkeyword.entity.InterestKeyword;
import com.ll.exam.querydsl.user.entity.SiteUser;
import com.ll.exam.querydsl.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("test") // 이 클래스 정의된 Bean 들은 test 모드에서만 활성화 된다.
public class TestInitData {

    // CommandLineRunner : 주로 앱 실행 직후 초기데이터 세팅 및 초기화에 사용
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            SiteUser u1 = SiteUser.builder()
                    .username("user1")
                    .password("{noop}1234")
                    .email("user1@test.com")
                    .build();

            SiteUser u2 = SiteUser.builder()
                    .username("user2")
                    .password("{noop}1234")
                    .email("user2@test.com")
                    .build();

            List <SiteUser> list = new ArrayList<>();
            list.add(u1);
            list.add(u2);
            List<SiteUser> siteUsers = userRepository.saveAll(list); // 부모를 먼저 저장

            u1.addInterestKeywordContent("축구");
            u1.addInterestKeywordContent("농구");

            u2.addInterestKeywordContent("농구");
            u2.addInterestKeywordContent("클라이밍");
            u2.addInterestKeywordContent("마라톤");

            userRepository.saveAll(list);



        };
    }


}



