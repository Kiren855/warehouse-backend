package com.sunny.scm.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {
		"com.sunny.scm.common",
		"com.sunny.scm.warehouse",
		"com.sunny.scm.grpc_common"
})
public class WarehouseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseServiceApplication.class, args);
	}

}
