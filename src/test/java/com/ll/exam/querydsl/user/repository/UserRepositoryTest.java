package com.ll.exam.querydsl.user.repository;


import com.ll.exam.querydsl.user.entity.SiteUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        SiteUser u1 = new SiteUser(null, "user5", "{noop}12345", "user5@test.com");
        SiteUser u2 = new SiteUser(null, "user6", "{noop}12345", "user6@test.com");

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
        assertThat(qslCount).isEqualTo(4);

    }

    @Test
    @DisplayName("가장 오래된 회원")
    void orderUser() {
        SiteUser findUser = userRepository.getQslOrderUser();
        System.out.println(findUser.getUsername());
        assertThat(findUser.getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("전체회원, 오래된 순 ")
    void allUser_Order() {
        List<SiteUser> list = userRepository.getQslOrderAll();
        for (SiteUser siteUser : list) {
            System.out.println("siteUser.getId() = " + siteUser.getId());
        }
        SiteUser lastUser = list.get(list.size() - 1);
        assertThat(lastUser.getId()).isEqualTo(4L);

    }

    @Test
    @DisplayName("회원 이름으로 검색")
    void searchByName(){
        List<SiteUser> list = userRepository.searchQsl("user1");
        for (SiteUser siteUser : list) {
            System.out.println("siteUser.getId() = " + siteUser.getId());
        }

    }




    @Test
    @DisplayName("검색, Page 리턴")
    void t8() {
        int itemsInAPage = 1; // 한 페이지에 보여줄 아이템 개수
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(1, itemsInAPage, Sort.by(sorts)); // 한 페이지에 10까지 가능
        Page<SiteUser> users = userRepository.searchQsl("user", pageable);
        // 검색어 : user1
        // 한 페이지에 나올 수 있는 아이템 수 : 1개
        // 현재 페이지 : 1
        // 정렬 : id 역순

        // 내용 가져오는 SQL
        /*
        SELECT site_user.*
        FROM site_user
        WHERE site_user.username LIKE '%user%'
        OR site_user.email LIKE '%user%'
        ORDER BY site_user.id ASC
        LIMIT 1, 1
         */

        // 전체 개수 계산하는 SQL
        /*
        SELECT COUNT(*)
        FROM site_user
        WHERE site_user.username LIKE '%user%'
        OR site_user.email LIKE '%user%'
         */
    }
}

