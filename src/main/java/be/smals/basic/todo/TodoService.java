package be.smals.basic.todo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TodoService {

    private final TodoRestClient todoRestClient;

    public TodoService(final TodoRestClient todoRestClient) {
        this.todoRestClient = todoRestClient;
    }

    public String getOne() {
        return todoRestClient.getTodo();
    }

    public List<String> getAllSynchronous() {
        return IntStream.range(0, 10)
                .mapToObj(operand -> todoRestClient.getTodo())
                .collect(Collectors.toList());
    }

    public List<String> getAllAsynchronous() throws ExecutionException, InterruptedException {
        List<Integer> failures = new ArrayList<>();
        List<CompletableFuture<String>> futureTodos = IntStream.range(0, 10)
                .mapToObj(operand -> todoRestClient.getTodoAsync(operand).exceptionally(throwable -> {
                    failures.add(operand);
                    return null;
                }))
                .collect(Collectors.toList());

        CompletableFuture
                .allOf(futureTodos.toArray(new CompletableFuture[0]))
                .join();

        List<String> list = new ArrayList<>();
        for (CompletableFuture<String> futureTodo : futureTodos) {
            String s = futureTodo.get();
            if(s != null) list.add(s); // You can either collect failures based on the CompletableFuture response
        }

        System.out.println("These failed: " + failures); // Or add them to a list in the exceptionally block
        return list;
    }

}
