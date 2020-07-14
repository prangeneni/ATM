package com.atm.modal;
import java.util.Map;
import java.util.stream.Collectors;

import com.atm.OperationType;
import com.atm.dao.DataMap;
import com.atm.exception.OperationException;

public abstract class Operation {

	private DataMap dataMap = DataMap.getInstance();
	private String dispensedString = null;

	public void execute(OperationType type, Map<String, String> map) throws OperationException {
		String transactionNo = map.get("tx");
		if (OperationType.DEPOSIT == type) {
			String depositString = map.entrySet().stream()
					.filter(item -> !"operation".equals(item.getKey()) && !"withdrawalAmount".equals(item.getKey()) && !"tx".equals(item.getKey()))
					.map(item -> item.getKey() + ":" + item.getValue()).collect(Collectors.joining(", "));
			System.out.println("Deposit " + transactionNo + ": " + depositString);
			System.out.println("----------------------------------------------");
			dataMap.deposit(map);
		} else if (OperationType.WITHDRAWAL == type) {
			if (!map.containsKey("withdrawalAmount")) {
				throw new OperationException("Missing withdrawalAmount");
			}

			String withdrawalAmount = map.get("withdrawalAmount");
			System.out.println("Withdraw " + transactionNo + ": " + withdrawalAmount);
			System.out.println("----------------------------------------------");
			dispensedString = dataMap.withdrawal(map);
		}
	}

	void printBalance() {
		String balanceString = dataMap.getData().entrySet().stream().map(item -> item.getKey() + "=" + item.getValue())
				.collect(Collectors.joining(", "));
		System.out.println("Balance: " + balanceString);
	}

	void printDispense() {
		System.out.println("Dispensed: " + dispensedString);
	}

	void printTotal() {
		long total = dataMap.getData().entrySet().stream().collect(Collectors.summingLong(entry -> {
			long denomination = Long.valueOf(entry.getKey().substring(0, entry.getKey().length() - 1)).longValue();
			return denomination * entry.getValue();
		}));
		System.out.println("Total: " + total);
		System.out.println();
		System.out.println();
	}

	public abstract void printTransactionDetail();
}
