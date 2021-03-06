package org.sopt.seminar7.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * Created by ds on 2018-11-30.
 */

@Data
public class ContentReq {

    private int contentIdx;
    private int userIdx;
    private String body;
    private MultipartFile[] photo;
    private Date createdDate = new Date();

    public boolean checkProperties() {
        return (body != null && photo != null);
    }
}
