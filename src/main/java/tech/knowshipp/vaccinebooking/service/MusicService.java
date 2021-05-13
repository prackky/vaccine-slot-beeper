package tech.knowshipp.vaccinebooking.service;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

@Service
public class MusicService {

	private static final Logger log = LoggerFactory.getLogger(MusicService.class);

	public void playMusic() throws InterruptedException {
		Clip clip;

		try {
			log.info("Play music...");
			AudioInputStream input = AudioSystem.getAudioInputStream(new File("/home/shatakshi/Desktop/dev/git/fantasy-league/vaccinebookingvocal/bikehorn.wav"));
			clip = AudioSystem.getClip();
			clip.open(input);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void textToSpeech(String text) {
		log.info("Play Kevin voice...");
		Voice voice = VoiceManager.getInstance().getVoice("kevin");// Getting voice
		if (voice != null) {
			voice.allocate();// Allocating Voice
		}
		try {
			voice.setRate(190);// Setting the rate of the voice
			voice.setPitch(150);// Setting the Pitch of the voice
			voice.setVolume(3);// Setting the volume of the voice
			voice.speak(text);// Calling speak() method

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
