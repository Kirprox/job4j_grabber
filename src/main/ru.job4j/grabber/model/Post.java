package grabber.model;

import java.util.Objects;

public class Post {
    Long id;
    String title;
    String link;
    String description;
    Long time;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(title, post.title) && Objects.equals(link, post.link) && Objects.equals(description, post.description) && Objects.equals(time, post.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, description, time);
    }

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", time=" + time
                + '}';
    }
}
