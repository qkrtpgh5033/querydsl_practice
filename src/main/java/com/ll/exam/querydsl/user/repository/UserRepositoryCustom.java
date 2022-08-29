package com.ll.exam.querydsl.user.repository;

import com.ll.exam.querydsl.user.entity.SiteUser;

import java.util.List;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);

    Long getQslCount();

    SiteUser getQslOrderUser();

    List<SiteUser> getQslOrderAll();

}
