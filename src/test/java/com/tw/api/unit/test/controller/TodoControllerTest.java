package com.tw.api.unit.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.api.unit.test.domain.todo.Todo;
import com.tw.api.unit.test.domain.todo.TodoRepository;
import com.tw.api.unit.test.services.ShowService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
@ActiveProfiles(profiles = "test")
class TodoControllerTest {
    @Autowired
    private TodoController todoController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoRepository todoRepository;

    private List<Todo> todoList = new ArrayList<>();

    @Test
    void should_return_todo_when_getAll_is_triggered() throws Exception {
        Todo todo = new Todo(1,"To Pass ITA", true,2);
        todoList.add(todo);
        //given
        when(todoRepository.getAll()).thenReturn(todoList);
        //when
        ResultActions result = mvc.perform(get("/todos"));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title").value("To Pass ITA"));
    }

    @Test
    void should_return_todo_when_getTodo_is_triggered() throws Exception {
        Todo todo = new Todo(1,"To Pass ITA", true,2);
        todoList.add(todo);
        //given
        when(todoRepository.findById(1)).thenReturn(todoList.stream().findFirst());
        //when
        ResultActions result = mvc.perform(get("/todos/1"));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title").value("To Pass ITA"));
    }

    @Test
    void should_return_status_ok_when_saveTodo() throws Exception {
        //given
        Todo todo = new Todo(1,"To Pass ITA", true,2);
        //when
        ResultActions result = mvc.perform(get("/todos")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(todo)));
        //then
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void should_return_status_ok_when_deleteOneTodo() throws Exception {
        //given
        Todo todo = new Todo(1,"To Pass ITA", true,2);
        todoList.add(todo);
        when(todoRepository.findById(1)).thenReturn(todoList.stream().findFirst());
        //when
        ResultActions result = mvc.perform(delete("/todos/1"));

        //then
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void should_return_status_ok_when_updateTodo() throws Exception {

        //given
        Todo todo = new Todo(1,"To Pass ITA", true,2);
        Todo todoModify = new Todo(1,"To Pass ITA This year", true,2);
        todoList.add(todo);
        when(todoRepository.findById(1)).thenReturn(todoList.stream().findFirst());
        //when
        ResultActions result = mvc.perform(patch("/todos/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(todoModify)));

        //then
        result.andExpect(status().isOk())
                .andDo(print());
    }
}