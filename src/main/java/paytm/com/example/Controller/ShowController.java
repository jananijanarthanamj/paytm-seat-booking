package paytm.com.example.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import paytm.com.example.Entity.Show;
import paytm.com.example.Service.ShowService;
import paytm.com.example.dto.CreateShowRequest;

@RestController
@RequestMapping("/shows")
public class ShowController {

	 private final ShowService showService;

	    public ShowController(ShowService showService) {
	        this.showService = showService;
	    }

	    @PostMapping
	    @ResponseStatus(HttpStatus.CREATED)
	    public Show createShow(@RequestBody CreateShowRequest request) {

	        return showService.createShow(request);
	    }
	    
}
