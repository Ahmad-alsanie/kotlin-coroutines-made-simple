package com.sanie.com.sanie.controller

import com.sanie.com.sanie.model.Book
import com.sanie.com.sanie.service.BookService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService) {

    @GetMapping("/{id}")
    suspend fun getBookById(@PathVariable id: Long): Book? = bookService.findBookById(id)

    @GetMapping("/")
    fun getAllBooks(): Flow<Book> = flow {
        bookService.findAllBooks().forEach { emit(it) }
    }
}
