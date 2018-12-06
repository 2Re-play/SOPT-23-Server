package org.sopt.seminar7.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.seminar7.dto.Content;
import org.sopt.seminar7.mapper.ContentLikeMapper;
import org.sopt.seminar7.mapper.ContentMapper;
import org.sopt.seminar7.model.ContentReq;
import org.sopt.seminar7.model.DefaultRes;
import org.sopt.seminar7.model.Pagination;
import org.sopt.seminar7.utils.ResponseMessage;
import org.sopt.seminar7.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by ds on 2018-11-30.
 */

@Slf4j
@Service
public class ContentService {

    private final ContentMapper contentMapper;
    private final ContentLikeMapper contentLikeMapper;
    private final S3FileUploadService s3FileUploadService;

    public ContentService(final ContentMapper contentMapper, final ContentLikeMapper contentLikeMapper, final S3FileUploadService s3FileUploadService) {
        this.contentMapper = contentMapper;
        this.contentLikeMapper = contentLikeMapper;
        this.s3FileUploadService = s3FileUploadService;
    }

    /**
     * 모든 게시글 조회
     *
     * @param pagination 페이지네이션
     * @return DefaultRes
     */
    public DefaultRes<List<Content>> findAll(final Pagination pagination) {
        final List<Content> contentList = contentMapper.findAll(pagination);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_CONTENTS, contentList);
    }

    /**
     * 게시글 조회
     *
     * @param contentIdx 글 고유 번호
     * @return DefaultRes
     */
    public DefaultRes<Content> findByContentIdx(final int contentIdx) {
        final Content content = contentMapper.findByContentIdx(contentIdx);
        if (content == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, content);
    }

    /**
     * 컨텐츠 작성
     *
     * @param contentReq 컨텐츠 데이터
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes save(final ContentReq contentReq) {
        if (contentReq.checkProperties()) {
            try {
                contentMapper.save(contentReq);
                final int contentIdx = contentReq.getContentIdx();

                for (MultipartFile photo : contentReq.getPhoto()) {
                    contentMapper.savePhoto(contentIdx, s3FileUploadService.upload(photo));
                }

                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_CONTENT);
            } catch (Exception e) {
                log.info(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_CREATE_CONTENT);
    }

//
//    @Transactional
//    public DefaultRes likes(final int userIdx, final int contentIdx) {
//        //글 조회
//        Content content = findByContentIdx(contentIdx).getData();
//        if (content == null)
//            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
//
//        ContentLike contentLike = contentLikeMapper.findByUserIdxAndContentIdx(userIdx, contentIdx);
//
//        try {
//            if (contentLike == null) {
//                content.likes();
//                contentMapper.like(contentIdx, content.getB_like());
//                contentLikeMapper.save(userIdx, contentIdx);
//            } else {
//                content.unLikes();
//                contentMapper.like(contentIdx, content.getB_like());
//                contentLikeMapper.deleteByUserIdxAndContentIdx(userIdx, contentIdx);
//            }
//
//            content = findByContentIdx(contentIdx).getData();
//            content.setAuth(checkAuth(userIdx, contentIdx));
//
//            return DefaultRes.res(StatusCode.OK, ResponseMessage.LIKE_CONTENT, content);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//        }
//    }
//
//    @Transactional
//    public DefaultRes update(final int contentIdx, final ContentReq contentReq) {
//        //글 조회
//        Content content = findByContentIdx(contentIdx).getData();
//        if (content == null)
//            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
//
//        //수정
//        try {
//            if (contentReq.getPhoto() != null) content.setB_photo(fileUploadService.upload(contentReq.getPhoto()));
//            content.update(contentReq);
//            contentMapper.updateByContentIdx(content);
//
//            content = findByContentIdx(contentIdx).getData();
//            content.setAuth(checkLike(contentReq.getU_id(), contentIdx));
//
//            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CONTENT, content);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//        }
//    }

    /**
     * 컨텐츠 삭제
     * @param contentIdx 컨텐츠 고유 번호
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes deleteByContentIdx(final int contentIdx) {
        try {
            contentMapper.deleteByContentIdx(contentIdx);
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_CONTENT);
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 글 권환 확인
     *
     * @param userIdx    사용자 고유 번호
     * @param contentIdx 글 고유 번호
     * @return boolean
     */
    public boolean checkAuth(final int userIdx, final int contentIdx) {
        return userIdx == findByContentIdx(contentIdx).getData().getUserIdx();
    }

    /**
     * 좋아요 여부 확인
     *
     * @param userIdx    사용자 고유 번호
     * @param contentIdx 글 고유 번호
     * @return boolean
     */
    public boolean checkLike(final int userIdx, final int contentIdx) {
        return contentLikeMapper.findByUserIdxAndContentIdx(userIdx, contentIdx) != null;
    }
}
