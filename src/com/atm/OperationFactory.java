package com.atm;
import com.atm.modal.Deposit;
import com.atm.modal.Operation;
import com.atm.modal.Withdrawal;

public class OperationFactory {
	public static Operation getInstance(OperationType type) {
		if(type == OperationType.DEPOSIT) {
			return new Deposit();
		} else {
			return new Withdrawal();
		}
	}
}
