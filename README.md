# library
Library

Integration test for method  delete book

1. Checking, which books have reservation:
GET http://localhost:8080/reservations
The books id=1, 2, and 5 have reservations

2. Deleting book with reservation  book id =1
DELETE http://localhost:8080/books/1

3. Checking the list of books
GET http://localhost:8080/books
the book id=1  and book id=3 exists

4. deleting the book id=3
DELETE http://localhost:8080/books/3

5. cheking the list of books
GET http://localhost:8080/books
the book id=3 does not exists
