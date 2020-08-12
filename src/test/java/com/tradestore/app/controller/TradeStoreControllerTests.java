package com.tradestore.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.tradestore.app.exception.TradeRejectedException;
import com.tradestore.app.exception.TradeStoreMessages;
import com.tradestore.app.model.TradeStore;
import com.tradestore.app.model.TradeStoreResponse;
import com.tradestore.app.service.ITradeStoreService;
import com.tradestore.app.validator.TradeStoreRequestValidator;

@SpringBootTest
class TradeStoreControllerTests {

	@Spy
	private ITradeStoreService tradeStoreService;

	@Spy
	private TradeStoreRequestValidator tradeStoreRequestValidator;

	@Spy
	private BindingResult bindingResult;

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private static final Date TODAY = new Date(Calendar.getInstance().getTime().getTime());
	private static final Date YESTERDAY = new Date(Calendar.getInstance().getTime().getTime() - 24 * 60 * 60 * 1000);
	private static final Date TOMORROW = new Date(Calendar.getInstance().getTime().getTime() + 24 * 60 * 60 * 1000);

	@InjectMocks
	private TradeStoreController tradeStoreController;

	@Before
	public void InitialiseTest() throws ParseException {

		MockitoAnnotations.initMocks(this);
		MockMvcBuilders.standaloneSetup(tradeStoreController)
				.setHandlerExceptionResolvers(new ExceptionHandlerExceptionResolver()).build();

		List<TradeStore> tradeStoreList = new ArrayList<>();
		TradeStore tradeStore1 = new TradeStore("T1", 1, "CP-1", "B1", TOMORROW, YESTERDAY, 'N');
		TradeStore tradeStore2 = new TradeStore("T2", 2, "CP-2", "B1", TOMORROW, YESTERDAY, 'N');
		TradeStore tradeStore3 = new TradeStore("T3", 3, "CP-3", "B2", TOMORROW, YESTERDAY, 'N');

		tradeStoreList.add(tradeStore1);
		tradeStoreList.add(tradeStore2);
		tradeStoreList.add(tradeStore3);

		when(tradeStoreService.findAll()).thenReturn(tradeStoreList);

	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testAddTradesShouldRejectLowerVersion() throws ParseException {

		TradeStore tradeStore = new TradeStore("T3", 1, "CP-1", "B2", TOMORROW, YESTERDAY, 'N');

		TradeStore existingTradeStore = new TradeStore("T3", 3, "CP-3", "B2", TOMORROW, YESTERDAY, 'N');

		when(tradeStoreService.findOneByTradeId("T3")).thenReturn(Optional.of(existingTradeStore));

		// when
		final Throwable raisedException = catchThrowable(
				() -> tradeStoreController.addTrades(tradeStore, bindingResult));

		// then
		assertThat(raisedException).isInstanceOf(TradeRejectedException.class)
				.hasMessageContaining(TradeStoreMessages.LOWER_VERSION_RECEIVED.value());

	}

	@Test
	public void testAddTradesShouldOverrideTradeWithSameVersion() throws ParseException {

		final TradeStore tradeStore = new TradeStore("T3", 3, "CP-3", "B2", TOMORROW, YESTERDAY, 'N');

		TradeStore existingTradeStore = new TradeStore("T3", 3, "CP-3", "B2", TOMORROW, YESTERDAY, 'N');

		when(tradeStoreService.findOneByTradeId("T3")).thenReturn(Optional.of(existingTradeStore));

		TradeStoreResponse tradeStoreResponse = tradeStoreController.addTrades(tradeStore, bindingResult);

		assertEquals(TradeStoreMessages.OPERATION_SUCCESSFUL.value(), tradeStoreResponse.getMessage().getMessage());
		assertEquals(HttpStatus.OK.toString(), tradeStoreResponse.getMessage().getStatus());

	}

}
