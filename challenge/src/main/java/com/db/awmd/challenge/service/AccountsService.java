package com.db.awmd.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.function.SendNotification;
import com.db.awmd.challenge.function.TransferAmount;
import com.db.awmd.challenge.function.ValidateAccountId;
import com.db.awmd.challenge.repository.AccountsRepository;

import lombok.Getter;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;
	
	@Autowired
	private ValidateAccountId validateAccountId;
	@Autowired
	private TransferAmount transferAmount;
	@Autowired
	private SendNotification sendNotification;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	//Method to manage amount transfer
	public void transferAmount(Transfer transfer) {
		validateAccountId
		.andThen(transferAmount)
		.andThen(sendNotification)
		.apply(transfer);
	}

}
