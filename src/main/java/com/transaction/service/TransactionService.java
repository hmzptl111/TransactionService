package com.transaction.service;

import java.util.List;
import java.util.Optional;

import com.transaction.bean.Transaction;

public interface TransactionService {
	boolean addTransaction(int cardId, int sourceMetroStationId);

	boolean updateTransaction(int transactionId, int destinationMetroStationId, double fare);

	Optional<Transaction> getLastTransactionByCardId(int cardId);
	
	List<Transaction> getTransactionHistoryByCardId(int cardId);
}
