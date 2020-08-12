package com.tradestore.app.validator;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tradestore.app.exception.TradeStoreMessages;
import com.tradestore.app.model.TradeStore;

@Component
public class TradeStoreRequestValidator implements Validator {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeStoreRequestValidator.class);

	public static final String MATURITY_DATE = "maturityDate";	

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public boolean supports(Class clazz) {
		return TradeStore.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final TradeStore tradeStoreRequest = (TradeStore) target;
		final BindingResult bindingResult = (BindingResult) errors;
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("TradeStore validation starts.");
		}
		validateTradeStore(tradeStoreRequest, bindingResult);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("TradeStore validation ends.");
		}
	}

	private void validateTradeStore(TradeStore tradeStoreRequest, BindingResult bindingResult) {

		long millis = System.currentTimeMillis();
		String today = sdf.format(new Date(millis));
		Date sqlDateToday = null;
		try {
			sqlDateToday = new Date(sdf.parse(today).getTime());
		} catch (ParseException e) {
			LOGGER.info("Error while parsing date.", e);
		}

		boolean tradeExpired = false;

		if (tradeStoreRequest.getMaturityDate() != null
				&& (tradeStoreRequest.getMaturityDate().compareTo(sqlDateToday) < 0)) {
			tradeExpired = true;
		}

		if (tradeExpired) {
			bindingResult.rejectValue(MATURITY_DATE, HttpStatus.UNPROCESSABLE_ENTITY.toString(),
					TradeStoreMessages.TRADE_ALREADY_MATURED.value());
		}
	}
}