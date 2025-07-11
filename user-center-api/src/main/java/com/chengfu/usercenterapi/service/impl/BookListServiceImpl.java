package com.chengfu.usercenterapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chengfu.usercenterapi.model.domain.BookList;
import com.chengfu.usercenterapi.service.BookListService;
import com.chengfu.usercenterapi.mapper.BookListMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【book_list(书籍列表)】的数据库操作Service实现
* @createDate 2025-05-31 13:25:20
*/
@Service
public class BookListServiceImpl extends ServiceImpl<BookListMapper, BookList>
    implements BookListService{

}




