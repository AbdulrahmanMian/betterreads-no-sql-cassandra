package abdulr.mian.betterreads.app.book;


import abdulr.mian.betterreads.app.userbooks.UserBooks;
import abdulr.mian.betterreads.app.userbooks.UserBooksPrimaryKey;
import abdulr.mian.betterreads.app.userbooks.UserBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    UserBooksRepository userBooksRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping(value = "/books/{bookId}")
    public String displayBookByID(@PathVariable String bookId, Model model,
                                  @AuthenticationPrincipal OAuth2User principal) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book bookByID = optionalBook.get();
            String coverImageUrl = "/images/no-image.png";
            if (bookByID.getCoverIds() != null && bookByID.getCoverIds().size() > 0) {
                coverImageUrl = COVER_IMAGE_ROOT + bookByID.getCoverIds().get(0) + "-M.jpg";
            }
            model.addAttribute("coverImage", coverImageUrl);
            model.addAttribute("book", bookByID);
            if (principal != null && principal.getAttribute("login") != null) {
                String userId = principal.getAttribute("login");
                model.addAttribute("loginId", userId);
                UserBooksPrimaryKey key = new UserBooksPrimaryKey();
                key.setBookId(bookId);
                key.setUserId(userId);
                UserBooks userBooks = userBooksRepository.findById(key).orElse(new UserBooks());
                model.addAttribute("userBooks", userBooks);

            }
            return "book";

        }
        return "book-not-found";
    }
}