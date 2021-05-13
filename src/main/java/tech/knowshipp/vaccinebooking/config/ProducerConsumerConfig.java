package tech.knowshipp.vaccinebooking.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tech.knowshipp.vaccinebooking.dtos.UserSubscribe;
import tech.knowshipp.vaccinebooking.service.FetchPincodes;
import tech.knowshipp.vaccinebooking.service.MusicService;
import tech.knowshipp.vaccinebooking.service.SlotFinder;

@Configuration
public class ProducerConsumerConfig {
	
	private static final Logger log = LoggerFactory.getLogger(ProducerConsumerConfig.class);
	
	private BlockingQueue<String> pincodeQueue;
	
	private ExecutorService executor;
	@Autowired
	private MusicService musicService;
	@Autowired
	@Qualifier("subscribed")
	private UserSubscribe user;

	@Bean
	public void init() {
		log.info("Available CPUs : {}", Runtime.getRuntime().availableProcessors());
	}

	@PostConstruct
	public void setupAndInit() {
		log.info("Producer consumer config init called.");
		pincodeQueue = new LinkedBlockingDeque<String>(50);
		executor = Executors.newCachedThreadPool();
		
		for (int i = 0; i < 1; i++) {
			log.info("Fetch pincodes producer starting...");
			final FetchPincodes producer = new FetchPincodes(pincodeQueue, user);
			executor.submit(() -> {
				while (true) {
					log.info("Fetch pincodes inside while loop...");
					producer.getDistinctPincodes();
					log.info("Making fetch pincodes sleep for 1 minute...");
					Thread.sleep(30000);
				}
			});
		}

		for (int i = 0; i < 1; i++) {
			log.info("Slotfinder consumer starting...");
			final SlotFinder consumer = new SlotFinder(pincodeQueue, musicService);
			executor.submit(() -> {
				while (true) {
					log.info("Slotfinder consumer insider while loop...");
					consumer.findSlotsConsumer();
//					log.info("Slotfinder consumer sleeping for 5 seconds...");
//					Thread.sleep(5000);
				}
			});
		}
	}
	
	@PreDestroy
	public void cleanThreadPool() {
		log.info("Pre destroy cleanup activity...");
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
			executor.shutdownNow();
			log.info("Thread pool destroyed...");
		} catch (InterruptedException e) {
			log.info("Error waiting for ExecutorService shutdown");
		}
	}

}
