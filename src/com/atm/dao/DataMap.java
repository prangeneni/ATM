package com.atm.dao;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.atm.exception.OperationException;

public class DataMap {
	private static DataMap instance = null;
	private static Map<String, Long> data = new HashMap<String, Long>();

	private DataMap() {
		// Prevent from instantiation
	}

	public Map<String, Long> getData() {
		Comparator<Entry<String, Long>> byKey = (Entry<String, Long> entry1, Entry<String, Long> entry2) -> {
			Long d1 = Long.valueOf(entry1.getKey().substring(0, entry1.getKey().length() - 1));
			Long d2 = Long.valueOf(entry2.getKey().substring(0, entry2.getKey().length() - 1));
			return d2.compareTo(d1);
		};
		return this.data.entrySet().stream().sorted(byKey)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public static DataMap getInstance() {
		if (instance == null) {
			synchronized (DataMap.class) {
				if (instance == null) {
					instance = new DataMap();
				}
			}
		}

		return instance;
	}

	public void deposit(Map<String, String> input) throws OperationException {
		List<Entry<String, String>> inputEntries = input.entrySet().stream()
				.filter(item -> item.getValue() != null && !item.getValue().isEmpty()
						&& !"operation".equals(item.getKey()) && !"withdrawalAmount".equals(item.getKey())
						&& !"tx".equals(item.getKey()))
				.collect(Collectors.toList());

		Optional<Entry<String, String>> invalidEntry = inputEntries.stream()
				.filter(item -> Long.valueOf(item.getValue()) <= 0).findFirst();
		if (invalidEntry.isPresent()) {
			if (Long.valueOf(invalidEntry.get().getValue()) < 0) {
				throw new OperationException("Incorrect deposit amount");
			} else if (Long.valueOf(invalidEntry.get().getValue()) == 0) {
				throw new OperationException("Deposit amount cannot be zero");
			}
		}

		for (Entry<String, String> entry : inputEntries) {
			if (data.containsKey(entry.getKey())) {
				Long value = data.get(entry.getKey());
				value = Long.sum(value.longValue(), Long.valueOf(entry.getValue()));
				data.put(entry.getKey(), value);
			} else {
				data.put(entry.getKey(), Long.valueOf(entry.getValue()));
			}
		}
	}

	public String withdrawal(Map<String, String> input) throws OperationException {
		StringBuilder sb = new StringBuilder();
		String withdrawalAmount = input.get("withdrawalAmount");
		long amount = 0L;

		try {
			amount = Long.valueOf(withdrawalAmount).longValue();
		} catch (Exception e) {
			throw new OperationException("Incorrect or insufficient funds");
		}

		long sum = getData().entrySet().stream().collect(Collectors.summingLong(entry -> {
			long denomination = Long.valueOf(entry.getKey().substring(0, entry.getKey().length() - 1)).longValue();
			return denomination * entry.getValue();
		}));

		if (amount <= 0 || sum < amount) {
			throw new OperationException("Incorrect or insufficient funds");
		}

		
		
		for (Entry<String, Long> entry : getData().entrySet()) {
			long denomination = Long.valueOf(entry.getKey().substring(0, entry.getKey().length() - 1)).longValue();

			boolean changed = false;
			int i = 0;
			while (amount >= denomination && i+1 <= entry.getValue()) {
				changed = true;			
				amount = amount - denomination;
				i++;
			}

			if (changed) {
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(i);
				sb.append(", ");
				data.put(entry.getKey(), entry.getValue() - i);
			}
			
		}

		return sb.toString();

	}
}
