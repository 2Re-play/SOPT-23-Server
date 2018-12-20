package org.sopt.seminar8.repository;

import org.sopt.seminar8.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by ds on 2018-12-20.
 */
public interface UserRepository {

    /**
     * 로그인 시 email과 password를 비교후 결과값 반환
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return user
     */
    Optional<User> findByEmailAndPassword(final String email, final String password);

    Optional<User> findByEmail(final String email);

    /**
     * Delete 메소드 재 정의
     * JPA의 Delete가 내부적으로 find를 한번 하고 삭제를 진행하기 때문에, 재 정의
     * @param user_idx : User Resource index
     */
    @Transactional
    @Modifying
    @Query("DELETE User u where u.user_idx = :user_idx")
    void deleteByUserIdx(@Param("user_idx") final int user_idx);
}
