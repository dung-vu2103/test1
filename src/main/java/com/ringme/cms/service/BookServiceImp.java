package com.ringme.cms.service;


import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.kakoakcms.BookDto;
import com.ringme.cms.model.kakoakcms.Book;
import com.ringme.cms.repository.kakoakcms.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.logging.Handler;

@Service
public class BookServiceImp implements BookService {

 @Autowired
    BookRepository bookRepository;
    @Override
    public Page<Book> get(Integer userId, int pageNo, int pageSize) {
        Pageable pageable= PageRequest.of(pageNo-1,pageSize);
        return bookRepository.get(userId,pageable);
    }

    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void delete(Integer id) {
        bookRepository.deleteById(id);
    }
}
