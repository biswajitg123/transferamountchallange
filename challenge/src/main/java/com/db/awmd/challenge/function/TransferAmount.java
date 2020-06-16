package com.db.awmd.challenge.function;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AmountGreaterThanAvailableBalanceException;
import com.db.awmd.challenge.repository.AccountsRepository;

@Component
public class TransferAmount implements Function<Transfer, Transfer> {

	@Autowired
	AccountsRepository accountsRepository;

	@Override
	public Transfer apply(Transfer transfer) {
		Account accountFrom = accountsRepository.getAccount(transfer.getAccountFrom());
		Account accountTo = accountsRepository.getAccount(transfer.getAccountTo());
		if (transfer.getAmount().compareTo(accountFrom.getBalance())==1) {
			throw new AmountGreaterThanAvailableBalanceException("Transfer amount "+transfer.getAmount()+" is greater than available balance");
		}
		else {
			
			accountFrom.setBalance(accountFrom.getBalance().subtract(transfer.getAmount()));
			accountTo.setBalance(accountTo.getBalance().add(transfer.getAmount()));
		}
		return transfer;

	}

}
