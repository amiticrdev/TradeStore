package com.tradestore.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.tradestore.app.exception.TradeRejectedException;
import com.tradestore.app.exception.TradeStoreMessage;
import com.tradestore.app.exception.TradeStoreMessages;
import com.tradestore.app.model.TradeStore;
import com.tradestore.app.model.TradeStoreResponse;
import com.tradestore.app.service.ITradeStoreService;
import com.tradestore.app.validator.TradeStoreRequestValidator;

@Controller
public class TradeStoreController {

	@Autowired
	private ITradeStoreService tradeStoreService;
	
	@Autowired
	private TradeStoreRequestValidator tradeStoreRequestValidator;


	public static final String VERSION = "version";

	@GetMapping("/")
	public String index(Model model) {

		return "index";
	}

	@GetMapping("/tradeStore")
	public ModelAndView getTrades() {

		List<TradeStore> tradeStores = tradeStoreService.findAll();

		Map<String, Object> params = new HashMap<>();
		params.put("tradeStores", tradeStores);

		return new ModelAndView("tradeStores", params);
	}

	@PostMapping("/tradeStore")
	@ResponseBody
	public TradeStoreResponse addTrades(@RequestBody TradeStore tradeStore, BindingResult bindingResult) {

		ValidationUtils.invokeValidator(tradeStoreRequestValidator, tradeStore, bindingResult, tradeStoreService);

		if (bindingResult.getFieldErrorCount() > 0) {

			throw new TradeRejectedException(TradeStoreMessages.TRADE_ALREADY_MATURED.value(),
					bindingResult.getFieldError());
		}

		Optional<TradeStore> existingTradeStore = tradeStoreService.findOneByTradeId(tradeStore.getTradeId());

		if (existingTradeStore.isPresent() && (existingTradeStore.get()).getTradeId().equals(tradeStore.getTradeId())) {
			if (existingTradeStore.isPresent()
					&& (existingTradeStore.get()).getTradeId().equals(tradeStore.getTradeId())) {

				if (existingTradeStore.get().getVersion() > tradeStore.getVersion()) {

					bindingResult.rejectValue(VERSION, HttpStatus.UNPROCESSABLE_ENTITY.toString(),
							TradeStoreMessages.LOWER_VERSION_RECEIVED.value());
					throw new TradeRejectedException(TradeStoreMessages.LOWER_VERSION_RECEIVED.value(),
							bindingResult.getFieldError());
				}

			}

			if (existingTradeStore.get().getVersion() == tradeStore.getVersion()) {
				tradeStoreService.delete(existingTradeStore.get());
			}
		}

		tradeStoreService.saveOrUpdate(tradeStore);

		TradeStoreMessage tradeStoreMessage = new TradeStoreMessage(HttpStatus.OK.toString(), TradeStoreMessages.OPERATION_SUCCESSFUL.value());

		return new TradeStoreResponse(tradeStoreMessage);

	}

	@ExceptionHandler({ TradeRejectedException.class })
	@ResponseBody
	public TradeStoreResponse handleInvalidRequest(RuntimeException runtimeException) {
		final TradeRejectedException tradeRejectedException = (TradeRejectedException) runtimeException;
		final FieldError fieldError = tradeRejectedException.getFieldError();

		final TradeStoreMessage error = new TradeStoreMessage(HttpStatus.UNPROCESSABLE_ENTITY.name(),
				fieldError.getDefaultMessage());

		final TradeStoreResponse tradeStoreResponse = new TradeStoreResponse(error);

		return tradeStoreResponse;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public TradeStoreResponse handleAllExceptions(Exception exception) {
		final TradeStoreMessage error = new TradeStoreMessage(HttpStatus.INTERNAL_SERVER_ERROR.name(), TradeStoreMessages.GENERIC_ERROR.value());

		final TradeStoreResponse tradeStoreResponse = new TradeStoreResponse(error);

		return tradeStoreResponse;
	}

}