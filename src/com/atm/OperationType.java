package com.atm;
import java.util.Map;

import com.atm.exception.OperationException;

public enum OperationType {
	DEPOSIT,
	WITHDRAWAL;
	
	private String type;

	public String getType() {
		return type;
	}
	
	public static OperationType getValue(Map<String, String> input) throws OperationException {
		if(!input.isEmpty()) {
			if("deposit".equals(input.get("operation"))) {
				return DEPOSIT;
			} else if("withdrawal".equals(input.get("operation"))) {
				return WITHDRAWAL;
			}
		}
		
		throw new OperationException("Invalid operation of type "+input.get("operation"));
	}
}
