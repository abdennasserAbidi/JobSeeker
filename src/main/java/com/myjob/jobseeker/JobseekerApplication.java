package com.myjob.jobseeker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.myjob.jobseeker.model.Companies;
import com.myjob.jobseeker.model.activity.Activities;
import com.myjob.jobseeker.model.field.Fields;
import com.myjob.jobseeker.model.institute.Institutes;
import com.myjob.jobseeker.services.JsonService;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class JobseekerApplication implements CommandLineRunner {

	@Autowired
    ObjectMapper mapper;

	@Autowired
	JsonService service;

	@Override
    public void run(String... args) {
        if (service.getSchoolsCount() == 0) {
            try {
				String contentSchools = StreamUtils.copyToString(
                    new ClassPathResource("schools.json").getInputStream(), 
                    StandardCharsets.UTF_8
                );
               
                Institutes institutes = mapper.readValue(contentSchools, Institutes.class);
                service.saveInstitutesFromJsonFile(institutes);
			} catch(Exception e) {
               e.printStackTrace();
			}
		}

		if (service.getActivityCount() == 0) {
            try {
				String contentSubjects = StreamUtils.copyToString(
                    new ClassPathResource("subjects.json").getInputStream(), 
                    StandardCharsets.UTF_8
                );
               
                Activities activities = mapper.readValue(contentSubjects, Activities.class);
                service.saveActivitiesFromJsonFile(activities);
			} catch(Exception e) {
               e.printStackTrace();
			}
		}

		if (service.getFieldCount() == 0) {
            try {
				String contentField = StreamUtils.copyToString(
                    new ClassPathResource("studyfieldfr.json").getInputStream(), 
                    StandardCharsets.UTF_8
                );
               
                Fields activities = mapper.readValue(contentField, Fields.class);
                service.saveFieldsFromJsonFile(activities);
			} catch(Exception e) {
               e.printStackTrace();
			}
		}


		if (service.getCompaniesCount() == 0) {
		  try {
			   String content = StreamUtils.copyToString(
                new ClassPathResource("companies.json").getInputStream(), 
                StandardCharsets.UTF_8
               );
               
                Companies companies = mapper.readValue(content, Companies.class);
                service.saveFromJsonFile(companies);
            } catch (Exception e) {
			  e.printStackTrace();
            }	
	    }
    }

	@Bean
	FirebaseMessaging firebaseMessaging () throws IOException {
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
				new ClassPathResource("firebase-service-account.json").getInputStream()
		);
		FirebaseOptions firebaseOptions = FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();


		FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions, "dev project");
		return FirebaseMessaging.getInstance(firebaseApp);
	}

	/*@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("/opt/backend/firebase/firebase-service-account.json");

		FirebaseOptions firebaseOptions = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

		FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions, "My_App");
		return FirebaseMessaging.getInstance(firebaseApp);
	}*/

	public static void main(String[] args) {
		// Load .env file
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		// Set environment variables for Spring
		/*System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
		System.setProperty("SOCKET_HOST", dotenv.get("SOCKET_HOST"));
		System.setProperty("SPRING_DATA_MONGODB_URI", dotenv.get("SPRING_DATA_MONGODB_URI"));
		System.setProperty("SECURITY_JWT_SECRET_KEY", dotenv.get("SECURITY_JWT_SECRET_KEY"));
		System.setProperty("SECURITY_JWT_EXPIRATION_TIME", dotenv.get("SECURITY_JWT_EXPIRATION_TIME"));
		System.setProperty("SPRING_MAIL_HOST", dotenv.get("SPRING_MAIL_HOST"));
		System.setProperty("SPRING_MAIL_PORT", dotenv.get("SPRING_MAIL_PORT"));
		System.setProperty("SPRING_MAIL_USERNAME", dotenv.get("SPRING_MAIL_USERNAME"));
		System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));
		System.setProperty("FILE_UPLOAD_DIR", dotenv.get("FILE_UPLOAD_DIR"));
		System.setProperty("MAX_FILE_SIZE", dotenv.get("MAX_FILE_SIZE"));
		System.setProperty("MAX_REQUEST_SIZE", dotenv.get("MAX_REQUEST_SIZE"));
		System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET"));
		System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY"));
		System.setProperty("CLOUDINARY_CLOUD_NAME", dotenv.get("CLOUDINARY_CLOUD_NAME"));
		System.setProperty("RESEND_API_KEY", dotenv.get("RESEND_API_KEY"));*/


		SpringApplication.run(JobseekerApplication.class, args);
	}

}