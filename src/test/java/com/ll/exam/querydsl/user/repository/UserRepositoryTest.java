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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
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
//        SiteUser u1 = new SiteUser(null, "user5", "{noop}12345", "user5@test.com");
//        SiteUser u2 = new SiteUser(null, "user6", "{noop}12345", "user6@test.com");

//        userRepository.saveAll(Arrays.asList(u1, u2));
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
    @Test
    @DisplayName("검색, Page 리턴, id asc")
    void t8() {
        int itemsInAPage = 1; // 한 페이지에 보여줄 아이템 개수
        long totalCount = userRepository.count();
        int page = 1;
        int pageSize = 1;
        int totalPages = (int) Math.ceil(totalCount / (double)pageSize);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(1, itemsInAPage, Sort.by(sorts)); // 한 페이지에 10까지 가능
        Page<SiteUser> usersPage = userRepository.searchQsl("user", pageable);

        assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
        assertThat(usersPage.getNumber()).isEqualTo(page);
        assertThat(usersPage.getSize()).isEqualTo(pageSize);

        List<SiteUser> list = usersPage.get().toList();
        assertThat(list.size()).isEqualTo(pageSize);

        SiteUser u = list.get(0);

        assertThat(u.getId()).isEqualTo(2L);
        assertThat(u.getUsername()).isEqualTo("user2");
        assertThat(u.getEmail()).isEqualTo("user2@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");
    }

    @Test
    @DisplayName("검색, Page 리턴, id DESC")
    void t9() {
        long totalCount = userRepository.count();
        int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
        int totalPages = (int)Math.ceil(totalCount / (double)pageSize);
        int page = 1;
        String kw = "user";

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts)); // 한 페이지에 10까지 가능
        Page<SiteUser> usersPage = userRepository.searchQsl(kw, pageable);

        assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
        assertThat(usersPage.getNumber()).isEqualTo(page);
        assertThat(usersPage.getSize()).isEqualTo(pageSize);

        List<SiteUser> users = usersPage.get().toList();

        assertThat(users.size()).isEqualTo(pageSize);

        SiteUser u = users.get(0);

        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getUsername()).isEqualTo("user1");
        assertThat(u.getEmail()).isEqualTo("user1@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");


    }

    @Test
    @Rollback(false)
    void t10(){
        SiteUser user = userRepository.getQslUser(2L);

        user.addInterestKeywordContent("축구");
        user.addInterestKeywordContent("롤");
        user.addInterestKeywordContent("헬스");
        user.addInterestKeywordContent("클라이밍");
        user.addInterestKeywordContent("헬스"); // 중복등록은 무시

        userRepository.save(user);
        // 엔티티 클래스 InterestKeyword
    }

    @Test
    @Rollback(false)
    @DisplayName("축구에 관심이 있는 회원을 검색할 수 있다.")
    void t11(){

        List<SiteUser> list = userRepository.searchInterest("축구");
        System.out.println(list.size());
        for (SiteUser findUser : list) {
            System.out.println("findUser.getUsername() = " + findUser.getUsername());
        }
        
    }

    @Test
    @Rollback(false)
    @DisplayName("축구에 관심이 있는 회원을 검색할 수 있다. (queryDsl 없이)")
    void t12(){
        List<SiteUser> list = userRepository.findByInterestKeywords_content("축구");
        System.out.println(list.size());
        for (SiteUser findUser : list) {
            System.out.println("findUser.getUsername() = " + findUser.getUsername());
        }

    }


    @Test
    @Rollback(false)
    @DisplayName("u2 = 아이돌, u1=팬, u1은 u2의 팔로워 이다.")
    void t13(){

        SiteUser u1 = userRepository.getQslUser(1L);
        SiteUser u2 = userRepository.getQslUser(2L);

        u1.follow(u2);

        userRepository.save(u2);



    }

    @Test
    @Rollback(false)
    @DisplayName("본인이 본인을 팔로우 할 수 없다.")
    void t14(){
        SiteUser u1 = userRepository.getQslUser(1L);
        u1.follow(u1);
        assertThat(u1.getFollowers().size()).isEqualTo(0);
    }
}

