package be.smals.basic.todo;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Repository
public class TodoRestClient {

    private final Random random;
    private final RestTemplate restTemplate;

    public TodoRestClient(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.random = new Random();
    }

    public String getTodo() {
        System.out.println(Thread.currentThread().getId() + " Get todo from rest");
        return Optional.ofNullable(restTemplate.getForObject("https://jsonplaceholder.typicode.com/todos/1", ToDo.class))
                .map(ToDo::getTitle)
                .orElse("");
    }

    @Async
    public CompletableFuture<String> getTodoAsync(int id) {
        String url = random.nextInt(10) < 5 ? "https://jsonplaceholder.typicode.com/todos/" + id : "https://jsonplaceholder.typicode.be/todos/1";
        String todo = Optional.ofNullable(restTemplate.getForObject(url, ToDo.class))
                .map(ToDo::getTitle)
                .orElse("");
        return CompletableFuture.completedFuture(todo);
    }

}
