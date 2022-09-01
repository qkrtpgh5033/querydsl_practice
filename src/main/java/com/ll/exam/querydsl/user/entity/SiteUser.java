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

    public void addFollower(SiteUser follower) {
        followers.add(follower);
    }
}