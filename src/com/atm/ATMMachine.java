package com.atm;
import java.util.ArrayList;
import java.util.List;

/**
 * 1) Input arguments will represents the denomination and value in key-value
 * pair 2) Will have Deposit and Withdrawal Operations 3) On Deposit, will show
 * balance and total information 4) On Withdrawal, will show balance, dispensed
 * and total information
 * 
 */
public class ATMMachine {
	public static void main(String[] args) {
		Process process = new Process();
		process.executeTransaction(createInput());

	}
	
	private static String[] createInput() {
		List<String> input = new ArrayList<String>();
		input.add("--tx=1, --10s=8, --5s=20, --operation=deposit");
		input.add("--tx=2, --20s=3, --5s=18, --1s=4, --operation=deposit");
		input.add("--tx=2, --20s=-10, --5s=18, --1s=4, --operation=deposit");
		input.add("--tx=2, --20s=0, --5s=18, --1s=4, --operation=deposit");
		input.add("--tx=1, --operation=withdrawal, --withdrawalAmount=75");
		input.add("--tx=2, --operation=withdrawal, --withdrawalAmount=122");
		input.add("--tx=3, --operation=withdrawal, --withdrawalAmount=253");
		input.add("--tx=4, --operation=withdrawal, --withdrawalAmount=0");
		input.add("--tx=5, --operation=withdrawal, --withdrawalAmount=-25");
		
		//To support 50 and 100 denominations
		input.add("--tx=2, --50s=10, --100s=18, --1s=4, --operation=deposit");
		
		return input.toArray(new String[input.size()]);
	}

}
