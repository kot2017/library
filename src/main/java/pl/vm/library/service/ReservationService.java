package pl.vm.library.service;

import pl.vm.library.to.ReservationTo;

import java.util.List;

/**
 * The Service which contains business logic for Reservation.
 */
public interface ReservationService {

    // TODO Create reservation.
    public ReservationTo create(ReservationTo reservationTo);


    // TODO Extend reservation - change the "toDate" Date in the given reservation
    public ReservationTo extendReservation(ReservationTo reservationTo);

    public List<ReservationTo> findAll();
}
