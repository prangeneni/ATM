package com.atm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atm.exception.OperationException;
import com.atm.modal.Operation;

public class Process {
	public void executeTransaction(String[] args) {
		List<Map<String, String>> list = convertToListOfMap(args);

		for (Map<String, String> input : list) {
			OperationType type;
			try {
				type = OperationType.getValue(input);
				Operation operation = OperationFactory.getInstance(type);
				operation.execute(type, input);
				operation.printTransactionDetail();
			} catch (OperationException e) {
				System.out.println(e.getMessage());
				System.out.println();
				System.out.println();
			}
		}

	}

	private List<Map<String, String>> convertToListOfMap(String[] args) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (String arg : args) {
			Map<String, String> map = new HashMap<String, String>();
			String[] transactions = arg.split(", ");
			for (String trasaction : transactions) {
				String[] keyValue = trasaction.split("=");

				String key = keyValue[0].substring(2);
				String value = keyValue[1];

				map.put(key, value);
			}

			list.add(map);

		}

		return list;

	}
}
