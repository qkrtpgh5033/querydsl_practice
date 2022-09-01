package com.ll.exam.querydsl.user.entity;
import com.ll.exam.querydsl.interestkeyword.entity.InterestKeyword;
import lombok.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<InterestKeyword> interestKeywords = new HashSet<>();

    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<SiteUser> followers = new HashSet<>();


    public void addInterestKeywordContent(String keywordContent) {

        interestKeywords.add(new InterestKeyword(keywordContent));
//        if(!interestKeywords.contains(new InterestKeyword(keywordContent)))
//            System.out.println("hello");
    }

    //u1.follow(u2); ul -> u2를 팔로우 한다.
    public void follow(SiteUser following) {
        if (following.getId() == getId())
            return;
        if (following == null)
            return;

        following.getFollowers().add(this);
    }
}