package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AmountGreaterThanAvailableBalanceException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InvalidAccountIdException;
import com.db.awmd.challenge.function.SendNotification;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.NotificationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

	@Mock
	NotificationService notificationService;

	@Autowired
	@InjectMocks
	SendNotification sendNotification;

	@Autowired
	private AccountsService accountsService;

	private String uniqueAccountIdFrom;
	private String uniqueAccountIdTo;
	Account accountFrom;
	Account accountTo;

	@Before
	public void setup() {
		accountsService.getAccountsRepository().clearAccounts();
		ReflectionTestUtils.setField(accountsService, "sendNotification", sendNotification);
	}
	
	@Test
	public void addAccount() throws Exception {
		Account account = new Account("Id-123");
		account.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(account);
		assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
	}

	@Test
	public void addAccount_failsOnDuplicateId() throws Exception {
		String uniqueId = "Id-" + System.currentTimeMillis();
		Account account = new Account(uniqueId);
		this.accountsService.createAccount(account);

		try {
			this.accountsService.createAccount(account);
			fail("Should have failed when adding duplicate account");
		} catch (DuplicateAccountIdException ex) {
			assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
		}
	}

								/*-----New Test Cases-----*/
	@Test
	public void transferAmount() throws Exception {
		setUpAccounts();
		Transfer transfer = new Transfer(uniqueAccountIdFrom, uniqueAccountIdTo, BigDecimal.valueOf(100));
		accountsService.transferAmount(transfer);
		assertEquals(BigDecimal.valueOf(100), accountFrom.getBalance());
		assertEquals(BigDecimal.valueOf(200), accountTo.getBalance());

	}

	@Test(expected = InvalidAccountIdException.class)
	public void transferAmount_When_Invalid_AccountId() throws Exception {
		setUpAccounts();
		Transfer transfer = new Transfer("INVALID_ID", uniqueAccountIdTo, BigDecimal.valueOf(100));
		accountsService.transferAmount(transfer);
	}
	
	@Test(expected = AmountGreaterThanAvailableBalanceException.class)
	public void transferAmount_When_Amount_Greater_Than_Balance() throws Exception {
		setUpAccounts();
		Transfer transfer = new Transfer(uniqueAccountIdFrom, uniqueAccountIdTo, BigDecimal.valueOf(250));
		accountsService.transferAmount(transfer);
	}

	public void setUpAccounts() {
		uniqueAccountIdFrom = "Id-" + System.currentTimeMillis();
		uniqueAccountIdTo = "Id-" + System.currentTimeMillis() + 10;
		accountFrom = new Account(uniqueAccountIdFrom, new BigDecimal("200"));
		accountTo = new Account(uniqueAccountIdTo, new BigDecimal("100"));
		this.accountsService.createAccount(accountFrom);
		this.accountsService.createAccount(accountTo);
	}
}
