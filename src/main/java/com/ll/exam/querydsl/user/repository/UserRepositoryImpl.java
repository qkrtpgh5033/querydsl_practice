package com.ll.exam.querydsl.user.repository;

import com.ll.exam.querydsl.user.entity.QSiteUser;
import com.ll.exam.querydsl.user.entity.SiteUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ll.exam.querydsl.user.entity.QSiteUser.siteUser;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QSiteUser qSiteUser = siteUser;
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
        JPAQuery<SiteUser> usersQuery = jpaQueryFactory
                .select(qSiteUser)
                .from(qSiteUser)
                .where(
                        qSiteUser.username.contains(kw)
                                .or(qSiteUser.email.contains(kw))
                )
                .offset(pageable.getOffset()) // 몇개를 건너 띄어야 하는지 LIMIT {1}, ?
                .limit(pageable.getPageSize()); // 한페이지에 몇개가 보여야 하는지 LIMIT ?, {1}

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(siteUser.getType(), siteUser.getMetadata());
            usersQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        List<SiteUser> users = usersQuery.fetch();

        // return new PageImpl<>(users, pageable, usersQuery.fetchCount()); // 아래와 거의 동일

        return PageableExecutionUtils.getPage(users, pageable, usersQuery::fetchCount);
    }


}
