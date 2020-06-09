package pl.vm.library.service.impl;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.vm.library.entity.Book;
import pl.vm.library.entity.Reservation;
import pl.vm.library.entity.User;
import pl.vm.library.exception.ParameterValidationException;
import pl.vm.library.repository.BookRepository;
import pl.vm.library.repository.ReservationRepository;
import pl.vm.library.repository.UserRepository;
import pl.vm.library.service.BookService;
import pl.vm.library.service.ReservationService;
import pl.vm.library.service.UserService;
import pl.vm.library.to.ReservationTo;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;


    long MAX_TIME = 30;


    // TODO Create reservation.
    @Override
    public ReservationTo create(ReservationTo reservationTo) {
        validateNewReservation(reservationTo);

        Reservation reservationEntity = mapper.map(reservationTo, Reservation.class);
        reservationRepository.save(reservationEntity);

        return mapper.map(reservationEntity, ReservationTo.class);
    }


    @Override
    public ReservationTo create(Long userId, Long bookId, Date dateFrom, Date dateTo) {
        validateNewReservation(userId, bookId, dateFrom, dateTo);
        Optional<User> user = userRepository.findById(userId);
        Optional<Book> book = bookRepository.findById(bookId);
        ReservationTo reservationTo = new ReservationTo();

        reservationTo.setFromDate(dateFrom);
        reservationTo.setToDate(dateTo);

        Reservation reservationEntity = mapper.map(reservationTo, Reservation.class);
        reservationEntity.setBook(book.get());
        reservationEntity.setUser(user.get());

        reservationRepository.save(reservationEntity);
        return mapper.map(reservationEntity, ReservationTo.class);
    }


    /**
     * @param reservationTo
     * @return
     */
    //   Extend reservation - change the "toDate" Date in the given reservation
    @Override
    public ReservationTo extendReservation(ReservationTo reservationTo) {
        validateExtendReservation(reservationTo);

        Optional<Reservation> reservationOld = reservationRepository.findById(reservationTo.getId());
        Date fromDate = reservationOld.get().getFromDate();
        User user = reservationOld.get().getUser();
        Book book = reservationOld.get().getBook();

        Reservation reservationEntity = mapper.map(reservationTo, Reservation.class);

        reservationEntity.setUser(user);
        reservationEntity.setFromDate(fromDate);
        reservationEntity.setBook(book);

        //TODO we should also notice, how many times the extensions took place
        //TODO it will be necessary to add some field in table
        //TODO and set max number of extensions

        reservationRepository.save(reservationEntity);
        return mapper.map(reservationEntity, ReservationTo.class);
    }


    @Override
    public List<ReservationTo> findAll() {
        List<Reservation> list = (List<Reservation>) reservationRepository.findAll();
        return list.stream()
                .map(reservationEntity -> mapper.map(reservationEntity, ReservationTo.class))
                .collect(Collectors.toList());
    }

    /**
     * @param reservationTo
     */
    private void validateExtendReservation(ReservationTo reservationTo) {
        if (reservationTo.getId() == null) {
            throw new ParameterValidationException("When extending Reservation, the ID should be not null.");
        }
        validateReservationTime(new Date(), reservationTo.getToDate());
    }

    /**
     * @param reservationTo
     */
    private void validateNewReservation(ReservationTo reservationTo) {
        if (reservationTo.getId() != null) {
            throw new ParameterValidationException("When creating new Reservation, the ID should be null.");
        }
        validateReservation(reservationTo);
    }

    /**
     * @param reservationTo
     */
    private void validateReservation(ReservationTo reservationTo) {

        validateReservationTime(reservationTo.getFromDate(), reservationTo.getToDate());

        if (reservationTo.getUserId() == null) {
            throw new ParameterValidationException("When creating new Reservation, the user should be not null.");
        }
        if (reservationTo.getBookId() == null) {
            throw new ParameterValidationException("When creating new Reservation, the book should be not null.");
        }
    }

    /**
     * @param userId
     * @param bookId
     * @param fromDate
     * @param toDate
     */
    private void validateNewReservation(Long userId, Long bookId, Date fromDate, Date toDate) {

        validateReservationTime(fromDate, toDate);

        if (userId == null) {
            throw new ParameterValidationException("When creating new Reservation, the userID should be not null.");
        }
        if (bookId == null) {
            throw new ParameterValidationException("When creating new Reservation, the bookId should be not null.");
        }

    }


    /**
     * @param fromDate
     * @param toDate
     */
    private void validateReservationTime(Date fromDate, Date toDate) {
        if (fromDate == null) {
            throw new ParameterValidationException("When creating new Reservation, the date from should be not null.");
        }
        if (toDate == null) {
            throw new ParameterValidationException("When creating new Reservation, the date to should be not null.");
        }
        if (fromDate != null
                && toDate != null
                && toDate.before(fromDate)) {
            throw new ParameterValidationException("When creating new Reservation, the date from should be after the date to.");
        }

        long diffInMillies = Math.abs(toDate.getTime() - fromDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff > MAX_TIME) {
            throw new ParameterValidationException("When creating new Reservation, the time of reservation may be not greater then " + MAX_TIME + " days");
        }
    }


}
