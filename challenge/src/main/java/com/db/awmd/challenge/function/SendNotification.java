package com.db.awmd.challenge.function;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SendNotification implements Function<Transfer, Transfer> {

	@Autowired
	NotificationService notificationService;

	@Autowired
	AccountsService service;

	@Override
	public Transfer apply(Transfer transfer) {
		Account accountFrom = service.getAccountsRepository().getAccount(transfer.getAccountFrom());
		Account accountTo = service.getAccountsRepository().getAccount(transfer.getAccountTo());
		log.info("sending notification");
		notificationService.notifyAboutTransfer(accountTo, "Your account has been credited with Amount: "
				+ transfer.getAmount() + " by Account: " + accountFrom.getAccountId());

		notificationService.notifyAboutTransfer(accountFrom, "Your account has been debited with Amount: "
				+ transfer.getAmount() + " and transfered to Account: " + accountTo.getAccountId());
		return null;
	}

}
