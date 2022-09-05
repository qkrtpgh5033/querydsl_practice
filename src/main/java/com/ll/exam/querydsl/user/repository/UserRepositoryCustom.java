package com.ll.exam.querydsl.user.repository;

import com.ll.exam.querydsl.interestkeyword.entity.InterestKeyword;
import com.ll.exam.querydsl.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);

    Long getQslCount();

    SiteUser getQslOrderUser();

    List<SiteUser> getQslOrderAll();

    List<SiteUser> searchQsl(String name);

    Page<SiteUser> searchQsl(String kw, Pageable pageable);

    List<SiteUser> searchInterest(String interest);

    List<String> followUserOfInterestKeyword(Long id);

}
