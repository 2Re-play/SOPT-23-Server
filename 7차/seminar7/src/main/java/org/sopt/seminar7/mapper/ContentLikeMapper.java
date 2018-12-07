package org.sopt.seminar7.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.seminar7.dto.ContentLike;

/**
 * Created by ds on 2018-12-04.
 */

@Mapper
public interface ContentLikeMapper {

    /**
     * 좋아요 조회
     *
     * @param userIdx    회원 고유 번호
     * @param contentIdx 컨텐츠 고유 번호
     * @return 좋아요 객체
     */
    @Select("SELECT * FROM contentLike WHERE contentIdx = #{contentIdx} AND userIdx = #{userIdx}")
    ContentLike findByUserIdxAndContentIdx(@Param("userIdx") final int userIdx, @Param("contentIdx") final int contentIdx);

    /**
     * 좋아요
     *
     * @param userIdx    회원 고유 번호
     * @param contentIdx 컨텐츠 고유 번호
     */
    @Insert("INSERT INTO contentLike(contentIdx, userIdx) VALUES(#{contentIdx}, #{userIdx})")
    void save(@Param("userIdx") final int userIdx, @Param("contentIdx") final int contentIdx);

    /**
     * 좋아요 취소
     *
     * @param userIdx    회원 고유 번호
     * @param contentIdx 컨텐츠 고유 번호
     */
    @Delete("DELETE FROM contentLike WHERE contentIdx = #{contentIdx} AND userIdx = #{userIdx}")
    void deleteByUserIdxAndContentIdx(@Param("userIdx") final int userIdx, @Param("contentIdx") final int contentIdx);
}
