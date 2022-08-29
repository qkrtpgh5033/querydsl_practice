package com.ll.exam.querydsl.user.repository;

import com.ll.exam.querydsl.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long>, UserRepositoryCustom  {

}
