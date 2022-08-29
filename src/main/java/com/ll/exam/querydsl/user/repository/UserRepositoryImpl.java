package com.ll.exam.querydsl.user.repository;

import com.ll.exam.querydsl.user.entity.QSiteUser;
import com.ll.exam.querydsl.user.entity.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SiteUser getQslUser(Long id) {
        /*
        SELECT *
        FROM site_user
        WHERE id = 1
        */

        QSiteUser qSiteUser = QSiteUser.siteUser;

        return jpaQueryFactory
                .select(qSiteUser)
                .from(qSiteUser)
                .where(qSiteUser.id.eq(1L))
                .fetchOne();


    }

}
