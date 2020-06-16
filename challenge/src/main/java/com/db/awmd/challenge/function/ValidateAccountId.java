package com.db.awmd.challenge.function;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.InvalidAccountIdException;
import com.db.awmd.challenge.repository.AccountsRepository;

@Component
public class ValidateAccountId implements Function<Transfer,Transfer>{
	
	@Autowired
	AccountsRepository accountsRepository;
	@Override
	public Transfer apply(Transfer transfer) {
			// Check if account ids are valid
				Optional<Account> accountFrom = Optional.ofNullable(accountsRepository.getAccount(transfer.getAccountFrom()));
				Optional<Account> accountTo = Optional.ofNullable(accountsRepository.getAccount(transfer.getAccountTo()));
				//Checking if account ids are valid
				if (!accountFrom.isPresent()) {
					throw new InvalidAccountIdException("Account id: " + transfer.getAccountFrom() + " does not exist!");
				}
				if (!accountTo.isPresent()) {
					throw new InvalidAccountIdException("Account id: " + transfer.getAccountTo() + " does not exist!");
				}
		return transfer;
	}
	
	
	
	

}
