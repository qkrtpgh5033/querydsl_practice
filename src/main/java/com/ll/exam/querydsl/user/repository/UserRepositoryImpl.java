package com.ll.exam.querydsl.user.repository;

import com.ll.exam.querydsl.user.entity.QSiteUser;
import com.ll.exam.querydsl.user.entity.SiteUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QSiteUser qSiteUser = QSiteUser.siteUser;
    @Override
    public SiteUser getQslUser(Long id) {
        /*
        SELECT *
        FROM site_user
        WHERE id = 1
        */

        return jpaQueryFactory
                .select(qSiteUser)
                .from(qSiteUser)
                .where(qSiteUser.id.eq(1L))
                .fetchOne();
    }

    @Override
    public Long getQslCount() {

        JPAQuery<SiteUser> from = jpaQueryFactory
                .select(qSiteUser)
                .from(qSiteUser);
        Long count = from.stream().count();

        return count;
    }

    @Override
    public SiteUser getQslOrderUser() {

        SiteUser siteUser = jpaQueryFactory
                .select(qSiteUser)
                .from(qSiteUser)
                .orderBy(qSiteUser.id.asc())
                .limit(1)
                .fetchOne();

        return siteUser;


    }

    @Override
    public List<SiteUser> getQslOrderAll() {
        List<SiteUser> list = jpaQueryFactory
                .select(qSiteUser)
                .from(qSiteUser)
                .orderBy(qSiteUser.id.asc())
                .fetch();

        return list;

    }

    @Override
    public List<SiteUser> searchQsl(String name) {

        List<SiteUser> list = jpaQueryFactory
                .select(qSiteUser)
                .from(qSiteUser)
                .where(qSiteUser.username.contains(name).or(qSiteUser.email.contains(name)))
                .fetch();

        return list;
    }

    @Override
    public Page<SiteUser> searchQsl(String kw, Pageable pageable) {
        List<SiteUser> list = jpaQueryFactory
                .select(qSiteUser)
                .from(qSiteUser)
                .where(qSiteUser.username.contains(kw).or(qSiteUser.email.contains(kw)))
                .orderBy(qSiteUser.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(qSiteUser.count())
                .from(qSiteUser)
                .where(qSiteUser.username.contains(kw).or(qSiteUser.email.contains(kw)))
                .fetchOne();

        return new PageImpl<>(list, pageable, count);
    }


}
