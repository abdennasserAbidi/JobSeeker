package com.myjob.jobseeker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.myjob.jobseeker.model.datalist.AllCompanies;
import com.myjob.jobseeker.model.datalist.AllSubject;
import com.sun.tools.javac.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class JobseekerApplication {

	@Bean
	FirebaseMessaging firebaseMessaging () throws IOException {
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
				new ClassPathResource("firebase-service-account.json").getInputStream()
		);
		FirebaseOptions firebaseOptions = FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();
		FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions, "My_App");
		return FirebaseMessaging.getInstance(firebaseApp);
	}

	public static void main(String[] args) {

		ObjectMapper mapper = new ObjectMapper();

		/*try {
			InputStream inputStream = new ClassPathResource("companies.json").getInputStream();
			AllCompanies response = mapper.readValue(inputStream, AllCompanies.class);

		} catch (Exception e) {
			e.printStackTrace();
		}*/

		SpringApplication.run(JobseekerApplication.class, args);
	}

}