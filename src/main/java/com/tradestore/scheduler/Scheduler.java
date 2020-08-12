package com.tradestore.scheduler;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tradestore.app.model.TradeStore;
import com.tradestore.app.service.ITradeStoreService;

@Component
public class Scheduler {

	@Autowired
	private ITradeStoreService tradeStoreService;

	private final char TRADE_EXPIRED = 'Y';

	/*
	 * Cron to runs daily at midnight, to automatically update the expire flag
	 * if in a store the trade crosses the maturity date
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void expireTradeScheduler() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long millis = System.currentTimeMillis();

		String today = sdf.format(new Date(millis));
		Date sqlDateToday = new Date(sdf.parse(today).getTime());

		List<TradeStore> tradeStores = tradeStoreService.findAll();

		for (TradeStore tradeStore : tradeStores) {

			if (tradeStore.getMaturityDate().compareTo(sqlDateToday) < 0) {
				tradeStore.setExpired(TRADE_EXPIRED);
				tradeStoreService.saveOrUpdate(tradeStore);
			}

		}

	}
}