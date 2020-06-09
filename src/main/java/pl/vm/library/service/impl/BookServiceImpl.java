package pl.vm.library.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.vm.library.entity.Book;
import pl.vm.library.entity.Reservation;
import pl.vm.library.exception.ParameterValidationException;
import pl.vm.library.repository.BookRepository;
import pl.vm.library.repository.ReservationRepository;
import pl.vm.library.service.BookService;
import pl.vm.library.service.ReservationService;
import pl.vm.library.to.BookTo;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	private ModelMapper mapper = new ModelMapper();

	@Override
	public List<BookTo> findAll() {
		List<Book> books = (List<Book>) bookRepository.findAll();

		return books.stream()
				.map(bookEntity -> mapper.map(bookEntity, BookTo.class))
				.collect(Collectors.toList());
	}

	@Override
	public BookTo findById(Long id) {
		return bookRepository.findById(id)
				.map(userEntity -> mapper.map(userEntity, BookTo.class))
				.orElseThrow(EntityNotFoundException::new);
	}

	@Override
	public BookTo create(BookTo bookTo) {
		 validateNewBook(bookTo);

		Book bookEntity = mapper.map(bookTo, Book.class);

		bookRepository.save(bookEntity);

		return mapper.map(bookEntity, BookTo.class);
	}

	@Override
	public void delete(Long id) {
		//   implement deleting Book - but only if the given book is not reserved by
		// any user
		Book book = new Book();
		book.setId(id);
		List<Reservation> list = (List<Reservation>) reservationRepository.findAll();

		long  bookReservation =	 list.stream()
				.filter(reservation -> reservation.getBook().getId()==id )
				.count();

	  	if(bookReservation == 0){
			bookRepository.deleteById(id);
	  	}
	}


	private void validateNewBook(BookTo book) {
		if (book.getId() != null) {
			throw new ParameterValidationException("When creating new Book, the ID should be null.");
		}
	}

}
