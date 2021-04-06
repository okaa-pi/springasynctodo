package be.smals.basic.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class TodoController {

    private final TodoService todoService;

    public TodoController(final TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todo")
    public String getTodo() {
        return todoService.getOne();
    }

    @GetMapping("/todoSynchronous")
    public List<String> getTodoSynchronous() {
        return todoService.getAllSynchronous();
    }

    @GetMapping("/todoAsynchronous")
    public List<String> getTodoAsynchronous() throws ExecutionException, InterruptedException {
        return todoService.getAllAsynchronous();
    }
}
