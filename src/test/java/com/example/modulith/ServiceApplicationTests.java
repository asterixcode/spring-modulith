package com.example.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class ServiceApplicationTests {

	@Test
	void contextLoads() {

		var modules = ApplicationModules.of(ServiceApplication.class);

    for (var m : modules) System.out.println(
						"module: " + m.getName() + " | base package: " + m.getBasePackage());

		modules.verify();

		new Documenter(modules)
						.writeIndividualModulesAsPlantUml()
						.writeModulesAsPlantUml();
	}
}
