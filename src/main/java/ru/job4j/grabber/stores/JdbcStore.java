package ru.job4j.grabber.stores;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {
    private Connection connection;

    public JdbcStore(Config config) {
        init(config);
    }

    private void init(Config config) {
        try {
            String url = config.get("db.url");
            String username = config.get("db.username");
            String password = config.get("db.password");
            Class.forName(config.get("db.driver-class-name"));
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                connection.prepareStatement("INSERT INTO post(title, link, description, time) \n"
                        + "VALUES (?, ?, ?, ?)\n"
                        + "ON CONFLICT (link) \n"
                        + "DO UPDATE SET title = EXCLUDED.title, description = "
                        + "EXCLUDED.description, time = EXCLUDED.time;\n")) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            statement.setLong(4, post.getTime());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Post> getAll() throws SQLException {
        List<Post> postList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    postList.add(createPost(resultSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return postList;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post post = null;
        try (PreparedStatement statement = connection.
                prepareStatement("SELECT * FROM post WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = createPost(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(post);
    }

    private Post createPost(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getString("link"),
                resultSet.getString("description"),
                resultSet.getLong("time")
        );
    }
}