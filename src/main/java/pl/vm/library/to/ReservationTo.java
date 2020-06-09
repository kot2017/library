package pl.vm.library.to;

import pl.vm.library.entity.Book;
import pl.vm.library.entity.User;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Transport Object of the Reservation class.
 */
public class ReservationTo implements Serializable {

	private static final long serialVersionUID = -60690548233543094L;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// TODO You can add fields which you need to finish the task.


	private Long userId;


	private Long bookId;


	@NotNull
	private Date fromDate;


	@NotNull
	private Date toDate;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}



	@Override
	public String toString() {
		return "ReservationTo{" +
				"id=" + id +
				", userId=" + userId +
				", bookId=" + bookId +
				", fromDate=" + fromDate +
				", toDate=" + toDate +
				'}';
	}
}