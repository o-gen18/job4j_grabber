package ru.job4j.database;

import ru.job4j.model.Post;

import java.util.List;

public interface Store {
    void save(Post post);

    List<Post> getAll();

    Post findById(String id);
}
