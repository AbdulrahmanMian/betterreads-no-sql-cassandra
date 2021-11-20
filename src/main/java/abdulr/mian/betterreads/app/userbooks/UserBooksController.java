package abdulr.mian.betterreads.app.userbooks;

import abdulr.mian.betterreads.app.book.Book;
import abdulr.mian.betterreads.app.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class UserBooksController {
    @Autowired
    UserBooksRepository userBooksRepository;

    @Autowired
    UserBooksRepository booksByUserRepository;

    @Autowired
    BookRepository bookRepository;

    @PostMapping("/addUserBook")
    public ModelAndView addBook( @RequestBody MultiValueMap<String, String> formData,
        @AuthenticationPrincipal OAuth2User principal){
        if (principal == null || principal.getAttribute("login") == null) {
            return null;
        }
        String bookId = formData.getFirst("bookId");
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            return new ModelAndView("redirect:/");
        }
        Book book = optionalBook.get();
        UserBooks userBooks  = new UserBooks();
        UserBooksPrimaryKey key = new UserBooksPrimaryKey();
        String userId = principal.getAttribute("login");
        key.setUserId(userId);
        key.setBookId(bookId);
        userBooks.setKey(key);
        userBooks.setStartedDate(LocalDate.parse(formData.getFirst("startDate")));
        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        int rating = Integer.parseInt(formData.getFirst("rating"));
        userBooks.setRating(rating);
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));

        userBooksRepository.save(userBooks);

        return new ModelAndView("redirect:/books/" + bookId);

    }
}
