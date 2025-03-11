package ru.job4j.grabber.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.stores.Store;

import java.util.List;

public class SuperJobGrab implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var store = (Store) context.getJobDetail().getJobDataMap().get("store");
        var parse = (Parse) context.getJobDetail().getJobDataMap().get("parse");
        List<Post> posts = parse.fetch();
        posts.forEach(store::save);

    }
}