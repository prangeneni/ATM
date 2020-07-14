package com.atm.modal;

public class Withdrawal extends Operation {

	@Override
	public void printTransactionDetail() {
		printDispense();
		printBalance();
		printTotal();

	}

}
