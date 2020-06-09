package pl.vm.library.service.impl;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.vm.library.repository.BookRepository;
import pl.vm.library.repository.ReservationRepository;
import pl.vm.library.repository.UserRepository;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    public BookServiceImpl bookService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    // TODO Tests for delete
    @Test
    public void testDelete() {
        bookService.delete(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);
    }


    public void tearDown() throws Exception {
    }
}
