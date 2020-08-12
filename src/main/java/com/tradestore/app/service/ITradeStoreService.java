package com.tradestore.app.service;

import java.util.List;
import java.util.Optional;

import com.tradestore.app.model.TradeStore;

public interface ITradeStoreService {

	List<TradeStore> findAll();

	void saveOrUpdate(TradeStore tradeStore);
	
	void saveAll(List<TradeStore> tradeStores);
	
	Optional<TradeStore> findOneByTradeId(String tradeId);
	
	void delete(TradeStore tradeStore);

}