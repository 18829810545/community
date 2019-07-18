package com.example.demo.service;

import com.example.demo.User.Question;
import com.example.demo.User.User;
import com.example.demo.dto.PaginationDTO;
import com.example.demo.dto.QuestionDto;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);
        if(page < 1){
            page=1;
        }
        if(page > paginationDTO.getTotalPage()){
            page=paginationDTO.getTotalPage();
        }
        //size*(page-1)
        Integer offset=size *(page-1);
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDto> questionDtoList=new ArrayList<>();
        for (Question question:questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDTO.setQuestions(questionDtoList);
        return paginationDTO;
    }
}
