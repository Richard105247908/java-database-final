package com.project.code;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.awt.print.Book;
import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByGenre(String genre);
}