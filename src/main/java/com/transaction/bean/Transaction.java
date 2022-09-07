package com.transaction.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "transaction")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "card_id")
	private Integer cardId;
	
	@Column(name = "source_metro_id")
	private Integer sourceId;
	
	@Column(name = "destination_metro_id")
	private Integer destinationId;
	
	@Column(name = "swipe_in_time")
	private Timestamp swipeInTime;
	
	@Column(name = "swipe_out_time")
	private Timestamp swipeOutTime;
	
	@Column(name = "fare_calculated")
	private Double fare;
}
