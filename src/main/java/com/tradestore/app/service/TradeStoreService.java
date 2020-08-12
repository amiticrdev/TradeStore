package com.tradestore.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tradestore.app.model.TradeStore;
import com.tradestore.app.repository.TradeStoreRepository;

@Service
public class TradeStoreService implements ITradeStoreService {

	@Autowired
	TradeStoreRepository tradeStoreRepository;

	@Override
	public List<TradeStore> findAll() {

		List<TradeStore> tradeStoreList = new ArrayList<TradeStore>();

		tradeStoreRepository.findAll().forEach(tradeStore -> tradeStoreList.add(tradeStore));

		return tradeStoreList;
	}

	@Override
	public void saveOrUpdate(TradeStore tradeStore) {

		tradeStoreRepository.save(tradeStore);

	}

	@Override
	public Optional<TradeStore> findOneByTradeId(String tradeId) {
		Optional<TradeStore> tradeStore = tradeStoreRepository.findOneByTradeId(tradeId);
		return tradeStore;
	}

	@Override
	public void delete(TradeStore tradeStore) {

		tradeStoreRepository.delete(tradeStore);

	}

	@Override
	public void saveAll(List<TradeStore> tradeStores) {
		tradeStoreRepository.saveAll(tradeStores);
	}

}