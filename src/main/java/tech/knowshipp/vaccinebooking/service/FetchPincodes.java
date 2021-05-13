package tech.knowshipp.vaccinebooking.service;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.knowshipp.vaccinebooking.dtos.UserSubscribe;

public class FetchPincodes {
	
	private static final Logger log = LoggerFactory.getLogger(FetchPincodes.class);
	
	private final BlockingQueue<String> queuePincode;
	private final UserSubscribe user;
	
	public FetchPincodes(BlockingQueue<String> queuePincode, UserSubscribe user) {
		this.queuePincode = queuePincode;
		this.user = user;
	}
	
	public void getDistinctPincodes() throws InterruptedException {
		log.info("Fetching pincodes. inside producer");
		if(user.getPinCode() != null)
			queuePincode.offer(user.getPinCode());
		else 
			log.info("Nothing to offer to queue");
	}

}
