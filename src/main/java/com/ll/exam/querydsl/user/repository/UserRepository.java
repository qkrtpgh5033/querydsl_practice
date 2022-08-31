package com.ll.exam.querydsl.user.repository;

import com.ll.exam.querydsl.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<SiteUser, Long>, UserRepositoryCustom  {

    List<SiteUser> findByInterestKeywords_content(String content);
}
