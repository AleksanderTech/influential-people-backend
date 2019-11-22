package com.alek.influentialpeople.article.comment;

import com.alek.influentialpeople.user.entity.User;
import com.alek.influentialpeople.user.service.UserDataHolder;
import org.springframework.stereotype.Service;

@Service
public class TheArticleCommentService implements ArticleCommentService {

    private ArticleCommentRepository repository;
    private UserDataHolder<User> userHolder;

    public TheArticleCommentService(ArticleCommentRepository repository, UserDataHolder<User> userHolder) {
        this.repository = repository;
        this.userHolder = userHolder;
    }

    @Override
    public ArticleComment addComment(ArticleComment articleComment) {

        articleComment.setUser(new User(userHolder.getUsername()));
        return repository.save(articleComment);
    }

    @Override
    public void deleteComment(long id) {
        repository.deleteById(id);
    }
}
