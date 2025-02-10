package pro.sky.hogwarts_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"pro/sky/hogwarts_management_system/controller", "pro/sky/hogwarts_management_system/service", "pro/sky/hogwarts_management_system/repository"})
public class HogwartsManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HogwartsManagementSystemApplication.class, args);
	}

}
