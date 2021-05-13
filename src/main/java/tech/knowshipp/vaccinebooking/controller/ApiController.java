package tech.knowshipp.vaccinebooking.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.knowshipp.vaccinebooking.dtos.UserSubscribe;
import tech.knowshipp.vaccinebooking.service.ApiService;
import tech.knowshipp.vaccinebooking.service.MusicService;

@RestController
@CrossOrigin
@RequestMapping("api/")
public class ApiController {
	
	private static final Logger log = LoggerFactory.getLogger(ApiController.class);
	
	@Autowired
	private ApiService apiService;
	
	@Autowired
	private MusicService musicService;
	
	@Autowired
	@Qualifier("subscribed")
	private UserSubscribe subscribed;

	@PostMapping("notifyme")
	public ResponseEntity<Object> saveUser(@RequestBody @Valid UserSubscribe user){
		log.info("Prospect reveived : {}", user);
		Map<String, String> map = new HashMap<>();
		subscribed.setPinCode(user.getPinCode());
		log.info("Prospect save success : {}", subscribed);
		map.put("message", "Subscribe successful, wait for music to play...");
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}
	
	@GetMapping("find")
	public ResponseEntity<Object> findDetails(@RequestParam String pin, @RequestParam String date){
		return new ResponseEntity<Object>(apiService.findSlots(pin, date), HttpStatus.OK);
	}
	
	@GetMapping("play")
	public ResponseEntity<Object> playMusic(){
		try {
			musicService.playMusic();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}
