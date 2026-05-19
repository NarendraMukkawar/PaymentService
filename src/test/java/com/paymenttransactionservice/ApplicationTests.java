package com.paymenttransactionservice;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		properties = {
				"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
		}
)
class ApplicationTests {
}