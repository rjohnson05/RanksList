package edu.carroll.ranks_list;

import edu.carroll.ranks_list.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;


@SpringBootApplication
public class RanksListApplication {
	public static void main(String[] args) {
		SpringApplication.run(RanksListApplication.class, args);
	}
}