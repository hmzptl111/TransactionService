package com.transaction.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.bean.Transaction;
import com.transaction.bean.Transactions;
import com.transaction.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionResource {
	@Autowired
	TransactionService transactionService;
	
	@GetMapping(path = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Transactions> getTransactionHistoryByCardId(@PathVariable("cardId") int cardId) {
		List<Transaction> transactionHistory = transactionService.getTransactionHistoryByCardId(cardId);
		Transactions transactions = new Transactions(transactionHistory);
		
		return new ResponseEntity<Transactions>(transactions, transactions.getTransactions().size() > 0 ? HttpStatus.OK: HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/last/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Transaction> getLastTransactionByCardId(@PathVariable("cardId") int cardId) {
		Optional<Transaction> lastTransaction = transactionService.getLastTransactionByCardId(cardId);
		if(lastTransaction.isPresent()) {
			return new ResponseEntity<Transaction>(lastTransaction.get(), HttpStatus.FOUND);
		}
		
		return new ResponseEntity<Transaction>(new Transaction(), HttpStatus.NOT_FOUND);
	}

	@GetMapping(path = "/swipein/{cardId}/{sourceStationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean swipeIn(@PathVariable("cardId") int cardId, @PathVariable("sourceStationId") int sourceStationId) {
		return transactionService.addTransaction(cardId, sourceStationId);
	}
	
	@GetMapping(path = "/swipeout/{transactionId}/{destinationStationId}/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean swipeOut(@PathVariable("transactionId") int transactionId, @PathVariable("destinationStationId") int destinationStationId, @PathVariable("amount") double amount) {
		return transactionService.updateTransaction(transactionId, destinationStationId, amount);
	}
}