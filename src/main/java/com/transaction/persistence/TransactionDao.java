package com.transaction.persistence;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.transaction.bean.Transaction;

@Repository
public interface TransactionDao extends JpaRepository<Transaction, Integer> {
	Optional<Transaction> findById(int transactionId);

	@Transactional
	@Modifying
	@Query(value = "insert into transaction(card_id, source_metro_id, swipe_in_time) values(:cardId, :sourceMetroStationId, CURRENT_TIMESTAMP)", nativeQuery = true)
	int addTransaction(@Param("cardId") int cardId, @Param("sourceMetroStationId") int sourceMetroStationId);

	@Transactional
	@Modifying
	@Query(value = "update transaction set destination_metro_id = :destinationMetroId, swipe_out_time = CURRENT_TIMESTAMP, fare_calculated = :amount where id = :id", nativeQuery = true)
	int updateTransaction(@Param("id") int id, @Param("destinationMetroId") int destinationMetroId, @Param("amount") double amount);
	
	@Query(value = "select * from transaction where card_id = :cardId order by id desc limit 1", nativeQuery = true)
	Optional<Transaction> getLastTransactionByCardId(@Param("cardId") int cardId);
	
	@Query(value = "select * from transaction where card_id = :cardId", nativeQuery = true)
	List<Transaction> getTransactionHistoryByCardId(@Param("cardId") int cardId);
}
