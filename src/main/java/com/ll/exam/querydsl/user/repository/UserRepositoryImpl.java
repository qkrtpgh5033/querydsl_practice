package com.ll.exam.querydsl.user.repository;

import com.ll.exam.querydsl.interestkeyword.entity.InterestKeyword;
import com.ll.exam.querydsl.interestkeyword.entity.QInterestKeyword;
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

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Set;

import static com.ll.exam.querydsl.interestkeyword.entity.QInterestKeyword.*;
import static com.ll.exam.querydsl.user.entity.QSiteUser.siteUser;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QSiteUser qSiteUser = siteUser;
    QInterestKeyword interestKeyword = QInterestKeyword.interestKeyword;
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
                .where(qSiteUser.id.eq(id))
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
                .offset(pageable.getOffset()) // ????????? ?????? ????????? ????????? LIMIT {1}, ?
                .limit(pageable.getPageSize()); // ??????????????? ????????? ????????? ????????? LIMIT ?, {1}

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(siteUser.getType(), siteUser.getMetadata());
            usersQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        List<SiteUser> users = usersQuery.fetch();

        // return new PageImpl<>(users, pageable, usersQuery.fetchCount()); // ????????? ?????? ??????

        return PageableExecutionUtils.getPage(users, pageable, usersQuery::fetchCount);
    }

    @Override
    public List<SiteUser> searchInterest(String interest) {
        /**
         * select U.id
         * from site_user AS U
         * INNER JOIN site_user_interest_keywords AS SUIK
         * on U.id = SUIK.site_user_id
         * INNER JOIn interest_keyword as IK
         * ON IK.content = SUIK.interest_keywords_content
         * WHERE IK.CONTENT = "??????";
         */
//        List<SiteUser> find = jpaQueryFactory
//                .select(qSiteUser)
//                .from(qSiteUser)
//                .where(qSiteUser.interestKeywords.contains(new InterestKeyword(interest)))
//                .fetch();

//        QInterestKeyword IK = new QInterestKeyword("IK"); // AS ??????
        List<SiteUser> find = jpaQueryFactory
                .select(qSiteUser)
                .from(qSiteUser)
                .innerJoin(qSiteUser.interestKeywords, QInterestKeyword.interestKeyword)
                .where(
                        QInterestKeyword.interestKeyword.content.eq(interest))
                .fetch();

        return find;
    }

    // ?????? ????????? ?????? ?????? ????????? ???????????? ????????? ?????? ????????????

    /**
     SELECT DISTINCT IK.content
     FROM interest_keyword AS IK
     INNER JOIN site_user_followings AS SUF
     ON IK.user_id = SUF.followings_id
     WHERE SUF.site_user_id = 8;
     */
    @Override
    public List<String> followUserOfInterestKeyword(Long id) {

        SiteUser user = getQslUser(id);
        List<String> fetch = jpaQueryFactory
                .select(interestKeyword.content).distinct()
                .from(interestKeyword)
                .innerJoin(interestKeyword.user, siteUser)
                .where(interestKeyword.user.in(user.getFollowings()))
                .fetch();

        return fetch;

    }

//    @Override
//    public void removeInterest(SiteUser user, String keyword) {
//        jpaQueryFactory
//                .delete(qSiteUser)
//                .where(qSiteUser.id.eq(user.getId()).and(qSiteUser.interestKeywords.contains(new InterestKeyword())))
//                .execute();
//
//    }


}
