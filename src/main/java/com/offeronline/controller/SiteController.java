package com.offeronline.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.offeronline.domain.City;
import com.offeronline.domain.Comment;
import com.offeronline.domain.Contact;
import com.offeronline.domain.Customer;
import com.offeronline.domain.Deal;
import com.offeronline.domain.DealLabel;
import com.offeronline.domain.ItemCategory;
import com.offeronline.domain.ItemStatus;
import com.offeronline.domain.Person;
import com.offeronline.domain.Transaction;
import com.offeronline.exception.DealExpiredException;
import com.offeronline.gateway.PaymentGateway;
import com.offeronline.propertyeditor.CityEditor;
import com.offeronline.service.CityService;
import com.offeronline.service.CommentService;
import com.offeronline.service.CustomerService;
import com.offeronline.service.DealOptionService;
import com.offeronline.service.DealService;
import com.offeronline.service.ItemCategoryService;
import com.offeronline.service.accounting.TransactionService;
import com.offeronline.service.order.OrderService;
import com.offeronline.service.person.PersonService;
import com.offeronline.util.Util;

@Controller
@RequestMapping(value = "/")
public class SiteController {
	private static final Logger logger = LoggerFactory
			.getLogger(SiteController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PersonService personService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DealService dealService;

	@Autowired
	private ItemCategoryService categoryService;

	@Autowired
	private DealOptionService optionService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CityService cityService;
	
	@Autowired
	private CommentService commentService;

	@Autowired
	private PaymentGateway paymentGateway;

	@Autowired
	TransactionService txnService;

	private @Autowired CityEditor cityEditor;

	@InitBinder
	public void iniBinder(WebDataBinder binder) {
		binder.registerCustomEditor(City.class, this.cityEditor);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(
			@RequestParam(value = "category", required = false) Integer catergoryId,
			Locale locale, Model model) {

		model.addAttribute("title",
				messageSource.getMessage("website.home.title", null, locale));
		model.addAttribute("categories", categoryService.findAll());
		
		if (catergoryId != null && catergoryId > 0) {
			ItemCategory category = categoryService.find(catergoryId);
			if (category != null) {
				model.addAttribute("deals", dealService
						.findDealsByCategoryAndStatusAndNotLabel(category,
								DealLabel.FEATURED, ItemStatus.ON));
			} else {
				model.addAttribute("deals", new ArrayList<Deal>());
				
			}

		} else {
			model.addAttribute("deals", dealService
					.findDealsByStatusAndNotLabel(DealLabel.FEATURED,
							ItemStatus.ON));
			model.addAttribute("featureds", dealService.findDealsByLabelAndStatus(
					DealLabel.FEATURED, ItemStatus.ON));
		}
		return "website/index";
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(@RequestParam("deal") int dealId, Locale locale,
			Model uiModel) {
		uiModel = fillDetail(dealId, locale, uiModel);		
		return "website/detail";
	}

	@RequestMapping(value = "/newcomment", method = RequestMethod.POST)
	public String newComment(@RequestParam("dealId") int dealId,
			@RequestParam("memo") String memo, Locale locale, Model uiModel) {
			Deal deal = dealService.find(dealId);
			Person author = personService.findByUserName(Util.getCurrentUserName());
			if(deal !=null && author !=null){
				Comment comment = new Comment();
				comment.setDeal(deal);
				comment.setDate(new Date());
				comment.setCommentText(memo);
				comment.setAuthor(author);

				comment.setAccepted(false);
				
				 Comment com = commentService.create(comment);
				 if(com !=null)
				 {
					 uiModel.addAttribute("successMsg",
								messageSource.getMessage("comment.insert.success", null, locale));
				 }else{
					 uiModel.addAttribute("errorMsg",
								messageSource.getMessage("comment.insert.failed", null, locale));
				 }
			}
		uiModel = fillDetail(dealId, locale, uiModel);
		return "website/detail";
	}
	
	private Model fillDetail(int dealId, Locale locale, Model uiModel){
		
		Deal deal = dealService.find(dealId);
		boolean expired = false;
		if (deal == null)
			throw new ResourceNotFoundException(dealId + "");
		else {
			uiModel.addAttribute("deal", deal);

			Date now = new Date();
			if ((deal.getValidity().compareTo(now) < 0)
					|| deal.getStatus() != ItemStatus.ON) {
				expired = true;
			}
			List<Comment> comments = commentService.findByDeal(deal, true);
			uiModel.addAttribute("comments", comments);
			List<Deal> similars = dealService.findSimilars(deal);
			uiModel.addAttribute("sold", dealService.getSold(deal));
			uiModel.addAttribute("similars", similars);

		}
		uiModel.addAttribute("expired", expired);
		uiModel.addAttribute("title",
		messageSource.getMessage("website.detail.title", null, locale));
		return uiModel;
	}

	@RequestMapping(value = "/buy", method = RequestMethod.POST)
	public String newOrder(@RequestParam("dealId") int dealId,
			@RequestParam("optionId") int optionId,
			@RequestParam("qty") int qty, Locale locale, Model uiModel) {

		Transaction txn = null;
		try {
			txn = orderService.buy(dealId, optionId, qty);
		} catch (DealExpiredException e) {
			uiModel.addAttribute("errorMsg",
					messageSource.getMessage("deal.expired", null, locale));
			uiModel = fillModelForIndex(uiModel, locale);
			return "website/index";
		}
		return "redirect:" + paymentGateway.GetRequestUrl(txn);
	}

	@RequestMapping(value = "/myerror", method = RequestMethod.GET)
	public String myerror() throws Throwable {
		throw new Exception("This is a sample exception.");

	}

	@RequestMapping(value = "/resetpass", method = RequestMethod.GET)
	public String resetPassword(Model uiModel) throws Throwable {

		return "website/resetpass";
	}

	@RequestMapping(value = "/resetpass", method = RequestMethod.POST)
	public String resetPassword(@RequestParam("email") String email,
			Model uiModel, Locale locale) throws Throwable {

		Person person = personService.findByUserName(email);
		if (person != null && person.getId() > 0) {
			int result = personService.resetPassword(person.getId(), null);
			if (result > 0)
				uiModel.addAttribute("successMsg", messageSource.getMessage(
						"security.resetpass.success.message", null, locale));
		}
		uiModel = fillModelForIndex(uiModel, locale);
		return "website/index";

	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.GET)
	public String changePassword(Model uiModel) throws Throwable {

		return "website/changePassword";
	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public String changePassword(
			@RequestParam("new_password") String newPassword, Model uiModel,
			Locale locale) throws Throwable {

		Person person = personService.findByUserName(Util.getCurrentUserName());
		if (person != null && person.getId() > 0) {
			int result = personService.changePassword(person.getId(),
					newPassword);
			if (result > 0)
				uiModel.addAttribute("successMsg", messageSource.getMessage(
						"security.password.change.success", null, locale));
		}
		uiModel = fillModelForIndex(uiModel, locale);
		return "website/index";

	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String RegisterCustomer(Model uiModel, Locale locale) {

		uiModel.addAttribute("title", messageSource.getMessage(
				"website.register.title", null, locale));
		uiModel.addAttribute("cities", cityService.findAll());
		Customer customer = new Customer();
		customer.setContact(new Contact());
		uiModel.addAttribute("customer", customer);
		return "website/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String RegisterCustomerPost(
			 @Valid @ModelAttribute("customer") Customer customer,
			BindingResult result, Locale locale, Model uiModel) {

		if (result.hasErrors()) {
			uiModel.addAttribute("title", messageSource.getMessage(
					"website.register.title", null, locale));
			uiModel.addAttribute("cities", cityService.findAll());
			return "website/register";
		}
		customer = customerService.create(customer);
		logger.debug("customer= " + customer.getId() + ", "
				+ customer.getFirstName() + " " + customer.getLastName());
		if (customer != null && customer.getId() > 0) {
			uiModel.addAttribute("successMsg", messageSource.getMessage(
					"customer.register.success", null, locale));
		}

		uiModel = fillModelForIndex(uiModel, locale);

		return "website/index";
	}

	private Model fillModelForIndex(Model uiModel, Locale locale) {
		uiModel.addAttribute("title",
				messageSource.getMessage("website.home.title", null, locale));
		uiModel.addAttribute("categories", categoryService.findAll());
		uiModel.addAttribute("featureds", dealService
				.findDealsByLabelAndStatus(DealLabel.FEATURED, ItemStatus.ON));
		uiModel.addAttribute("deals", dealService.findDealsByStatusAndNotLabel(
				DealLabel.FEATURED, ItemStatus.ON));

		return uiModel;
	}
}
