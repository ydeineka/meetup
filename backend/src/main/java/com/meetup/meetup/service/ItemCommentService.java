package com.meetup.meetup.service;

import com.meetup.meetup.dao.ItemCommentDao;
import com.meetup.meetup.entity.ItemComment;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PropertySource("classpath:strings.properties")
public class ItemCommentService {

    private static Logger log = LoggerFactory.getLogger(ItemComment.class);

    private final ItemCommentDao itemCommentDao;

    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public ItemCommentService(ItemCommentDao itemCommentDao, AuthenticationFacade authenticationFacade, Environment env) {
        this.itemCommentDao = itemCommentDao;
        this.authenticationFacade = authenticationFacade;
    }

    public ItemComment findById(int id) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to get comment with id '{}'", id);
        return itemCommentDao.findById(id);
    }

    public ItemComment insert(ItemComment itemComment) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to insert comment '{}'", itemComment);
        return itemCommentDao.insert(itemComment);
    }

    public List<ItemComment> getCommentsByItemId(int itemId) {
        log.debug("Trying to get authenticated user");
        User user = authenticationFacade.getAuthentication();
        log.debug("User was successfully received");

        log.debug("Trying to get comments for item with id '{}'", itemId);
        return itemCommentDao.getCommentsForItemId(itemId);
    }


    public ItemComment deleteById(int commentId) {
        ItemComment deleteItem = findById(commentId);
        log.debug("Trying to delete comment with id '{}'", commentId);
        return itemCommentDao.delete(deleteItem);
    }
}