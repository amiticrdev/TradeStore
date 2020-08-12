package com.tradestore.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tradestore.app.model.TradeStore;  

public interface TradeStoreRepository extends CrudRepository<TradeStore, Integer>  {
	
	Optional<TradeStore> findOneByTradeId(String tradeId);

}
