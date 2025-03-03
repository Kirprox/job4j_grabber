package ru.job4j;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.stores.JdbcStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Config config = new Config();
        config.load("application.properties");
        String url = config.get("db.url");
        String username = config.get("db.username");
        String pasword = config.get("db.password");
        Class.forName(config.get("db.driver-class-name"));
        Connection connection = DriverManager.getConnection(url, username, pasword);
        var store = new JdbcStore(connection);
        var post = new Post();
        post.setTitle("Super Java Job");
        post.setTime(System.currentTimeMillis());
        store.save(post);
        var scheduler = new SchedulerManager();
        scheduler.init();
        scheduler.load(
                Integer.parseInt(config.get("rabbit.interval")),
                SuperJobGrab.class,
                store);
    }
}