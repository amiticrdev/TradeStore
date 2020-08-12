package com.tradestore.app.exception;

public enum TradeStoreMessages   {
	
LOWER_VERSION_RECEIVED("Lower Version Received: "),
TRADE_ALREADY_MATURED("Trade Already Matured"),
GENERIC_ERROR("Generic Error"),
OPERATION_SUCCESSFUL("Operation Successful");


private final String value;

TradeStoreMessages(String v) {
        value = v;
    }

public String value() {
    return value;
}

public static TradeStoreMessages fromValue(String v)
{
    for (final TradeStoreMessages c: TradeStoreMessages.values())
    {
        if (c.value.equals(v))
        {
            return c;
        }
    }
    throw new IllegalArgumentException(v);
}
}	
