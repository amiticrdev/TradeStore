package com.tradestore.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tradestore.app.exception.TradeStoreMessage;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeStoreResponse {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private TradeStoreMessage message;

	/**
	 * no-arg constructor
	 */
	public TradeStoreResponse() {
	}

	public TradeStoreResponse(TradeStoreMessage message) {
		this.message = message;
	}

	public TradeStoreMessage getMessage() {
		return message;
	}

	public void setMessage(TradeStoreMessage message) {
		this.message = message;
	}

}