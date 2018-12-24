package org.sopt.seminar8.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.seminar8.domain.Item;
import org.sopt.seminar8.mapper.ItemMapper;
import org.sopt.seminar8.repository.ItemRepository;
import org.sopt.seminar8.repository.MongoDBRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ds on 2018-12-24.
 */

@Slf4j
@RestController
public class TestController {

    //mybatis
    private final ItemMapper itemMapper;
    //jpa
    private final ItemRepository itemRepository;
    //mongoDB
    private final MongoDBRepository mongoDBRepository;

    public TestController(final ItemMapper itemMapper, final ItemRepository itemRepository,
                          final MongoDBRepository mongoDBRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.mongoDBRepository = mongoDBRepository;
    }

    @GetMapping("mybatis")
    public ResponseEntity mybatisTest() {
        try {
            log.info("----------------mybatis test start----------------");

            log.info("----------------mybatis insert data----------------");
            itemMapper.save(new Item());

            log.info("----------------mybatis findAll data----------------");
            List<Item> contentList = itemMapper.findAll();
            log.info(contentList.toString());

            log.info("----------------mybatis delete data----------------");
            itemMapper.deleteByName("test");

            log.info("----------------mybatis findAll data----------------");
            contentList = itemMapper.findAll();
            log.info(contentList.toString());

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("jpa")
    public ResponseEntity jpaTest() {
        try {
            log.info("----------------jpa test start----------------");

            log.info("----------------jpa insert data----------------");
            itemRepository.save(new Item());

            log.info("----------------jpa findAll data----------------");
            Iterable<Item> contentList = itemRepository.findAll();
            log.info(contentList.toString());

            log.info("----------------jpa delete data----------------");
            itemRepository.deleteByName("test");
            Item item = new Item();
            item.setName("test");
            //id로 삭제
            itemRepository.delete(item);

            log.info("----------------jpa findAll data----------------");
            contentList = itemRepository.findAll();
            log.info(contentList.toString());

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("mongodb")
    public ResponseEntity mongoDBTest() {
        try {
            log.info("mongodb test");
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("redis")
    public ResponseEntity redisRedis() {
        try {
            log.info("redis test");
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
