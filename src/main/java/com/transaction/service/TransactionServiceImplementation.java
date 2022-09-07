package com.transaction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.transaction.bean.Card;
import com.transaction.bean.MetroStation;
import com.transaction.bean.Transaction;
import com.transaction.persistence.TransactionDao;

@Service
public class TransactionServiceImplementation implements TransactionService {
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public boolean addTransaction(int cardId, int sourceMetroStationId) {
		try {
			ResponseEntity<Card> card = restTemplate.getForEntity("http://cardService/cards/" + cardId, Card.class);
			ResponseEntity<MetroStation> metroStation = restTemplate.getForEntity("http://metroStationService/stations/" + sourceMetroStationId, MetroStation.class);
			if(card.getStatusCode() == HttpStatus.FOUND && metroStation.getStatusCode() == HttpStatus.FOUND) {
				return transactionDao.addTransaction(cardId, sourceMetroStationId) > 0;
			}
		} catch(HttpClientErrorException e) {
			return false;
		}
		
		return false;
	}

	@Override
	public boolean updateTransaction(int transactionId, int destinationMetroStationId, double amount) {
		try {
			Optional<Transaction> transaction = transactionDao.findById(transactionId);
			ResponseEntity<MetroStation> destinationMetroStation = restTemplate.getForEntity("http://metroStationService/stations/" + destinationMetroStationId, MetroStation.class);
			if(transaction.isPresent() && destinationMetroStation.getStatusCode() == HttpStatus.FOUND) {
				return transactionDao.updateTransaction(transactionId, destinationMetroStationId, amount) > 0;
			}
		} catch(HttpClientErrorException e) {
			return false;
		}
		
		return false;
	}
	
	@Override
	public Optional<Transaction> getLastTransactionByCardId(int cardId) {
		return transactionDao.getLastTransactionByCardId(cardId);
	}

	@Override
	public List<Transaction> getTransactionHistoryByCardId(int cardId) {
		return transactionDao.getTransactionHistoryByCardId(cardId);
	}
}