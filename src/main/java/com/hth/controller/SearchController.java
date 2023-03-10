package com.hth.controller;

import com.hth.entity.Msg;
import com.hth.log.Logweb;
import com.hth.log.TestSuccess;
import com.hth.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@TestSuccess
public class SearchController {
    @Autowired
    SearchService searchService;

    @Logweb("搜索")
    @GetMapping("/search")
    public Msg search(@RequestParam(required = true) String content){
        Map<String, List> search = searchService.search(content);
        return Msg.success().add("data",search);
    }
}
