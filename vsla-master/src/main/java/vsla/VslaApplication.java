package vsla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VslaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VslaApplication.class, args);
	}

}
