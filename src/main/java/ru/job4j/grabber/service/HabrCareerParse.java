package ru.job4j.grabber.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final Logger LOG = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";

    private static final Integer COUNTPAGE = 5;

    @Override
    public List<Post> fetch() {
        HabrCareerDateTimeParser timeParser = new HabrCareerDateTimeParser();
        var result = new ArrayList<Post>();
        try {
            for (int i = 1; i <= COUNTPAGE; i++) {
            int pageNumber = i;
                String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
                var connection = Jsoup.connect(fullLink);
                var document = connection.get();
                var rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    var titleElement = row.select(".vacancy-card__title").first();
                    var dateElement = row.select(".vacancy-card__date").first();
                    var linkElement = titleElement.child(0);
                    String vacancyName = titleElement.text();
                    String link = String.format("%s%s", SOURCE_LINK,
                            linkElement.attr("href"));
                    String dateStr = dateElement.select("time").attr("datetime");
                    LocalDateTime date = timeParser.parse(dateStr);
                    long timestamp = date.toEpochSecond(ZoneOffset.UTC) * 1000L;
                    var post = new Post();
                    post.setTitle(vacancyName);
                    post.setLink(link);
                    post.setTime(timestamp);
                    post.setDescription(retrieveDescription(link));
                    result.add(post);
                });
            }

        } catch (IOException e) {
            LOG.error("When load page", e);
        }
        return result;
    }

    private String retrieveDescription(String link) {
        String descriptionResult = "";
        try {
            Document connection = Jsoup.connect(link).get();
            var description = connection.select(".style-ugc");
            return description.text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return descriptionResult;
    }
}