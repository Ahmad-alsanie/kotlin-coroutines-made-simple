package com.sanie.com.sanie.service

import com.sanie.com.sanie.model.Book
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

@Service
class BookService {
    // simulate an asynchronous operation e.g. fetching data from a database
    suspend fun findBookById(id: Long): Book? {
        delay(1000) // db delay
        return Book(id, "Coroutine Made Simple!", "A.Sanie")
    }

    suspend fun findAllBooks(): List<Book> {
        delay(1000) // db delay
        return listOf(
            Book(1, "Design Patterns Made Simple!", "A.Sanie"),
            Book(2, "Spring-boot for Dummies!", "Malcom X")
        )
    }
}
