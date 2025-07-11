package com.chengfu.usercenterapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chengfu.usercenterapi.model.domain.UserMessage;
import com.chengfu.usercenterapi.service.UserMessageService;
import com.chengfu.usercenterapi.mapper.UserMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author 30362
* @description 针对表【user_message】的数据库操作Service实现
* @createDate 2025-05-16 10:59:33
*/
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage>
    implements UserMessageService{

}




