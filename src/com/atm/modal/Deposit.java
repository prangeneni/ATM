package com.atm.modal;

public class Deposit extends Operation {

	@Override
	public void printTransactionDetail() {
		printBalance();
		printTotal();		
	}

}
