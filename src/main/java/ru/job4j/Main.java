package ru.job4j;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.HabrCareerParse;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.stores.JdbcStore;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        HabrCareerParse hcp = new HabrCareerParse();
        Config config = new Config();
        config.load("application.properties");
        var store = new JdbcStore(config);
        var parse = new HabrCareerParse();
        List<Post> vacancies = hcp.fetch();
        for (Post post: vacancies) {
            var curPost = new Post();
            curPost.setTitle(post.getTitle());
            curPost.setTime(post.getTime());
            curPost.setDescription(post.getDescription());
            curPost.setLink(post.getLink());
            store.save(curPost);
        }
        var scheduler = new SchedulerManager();
        scheduler.init();
        scheduler.load(
                Integer.parseInt(config.get("rabbit.interval")),
                SuperJobGrab.class,
                store, parse);
    }
}