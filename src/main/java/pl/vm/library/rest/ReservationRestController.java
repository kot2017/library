package pl.vm.library.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.vm.library.service.ReservationService;
import pl.vm.library.to.ReservationTo;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationRestController {


    @Autowired
    private ReservationService reservationService;

    //   Create reservation.
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationTo create(@RequestBody ReservationTo reservationTo) {
        return reservationService.create(reservationTo);
    }


    //   Extend reservation - change the "toDate" Date in the given reservation
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationTo extendReservation(
            @PathVariable(value = "reservationId") Long reservationId,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "dd.MM.yyyy") Date dateTo) {
        ReservationTo reservationTo = new ReservationTo();
        reservationTo.setId(reservationId);
        reservationTo.setToDate(dateTo);
        return reservationService.extendReservation(reservationTo);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationTo> findAll() {
        return reservationService.findAll();
    }


}
