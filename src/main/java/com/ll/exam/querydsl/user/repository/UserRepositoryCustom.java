package com.ll.exam.querydsl.user.repository;

import com.ll.exam.querydsl.user.entity.SiteUser;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);
}
