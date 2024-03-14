package com.ringme.cms.service;

import com.ringme.cms.model.kakoakcms.Book;
import org.springframework.data.domain.Page;

public interface BookService {

    Page<Book> get(Integer userId, int pageNo, int pageSize);
    Book findById(Integer id);
    void save(Book book);
    void delete(Integer id);
}
