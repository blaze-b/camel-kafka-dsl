package com.ust.integration.kafka.configuration;

import org.apache.camel.CamelContext;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainKafkaConfiguration {

	public static final String MY_DEFAULT_PROFILE = "myDefaultKafkaProfile";
	public static final String KAFKA = "kafka";

	@Bean
	CamelContextConfiguration contextConfiguration(@Value("${kafka.host}") final String host,
			@Value("${kafka.port}") final String port, @Value("${kafka.channel}") final String groupID) {
		return new CamelContextConfiguration() {
			public void beforeApplicationStart(CamelContext camelContext) {
				// your custom configuration goes here
				ThreadPoolProfile threadPoolProfile = new ThreadPoolProfile();
				threadPoolProfile.setId(MY_DEFAULT_PROFILE);
				threadPoolProfile.setPoolSize(10);
				threadPoolProfile.setMaxPoolSize(15);
				threadPoolProfile.setMaxQueueSize(250);
				threadPoolProfile.setKeepAliveTime(25L);
				threadPoolProfile.setRejectedPolicy(ThreadPoolRejectedPolicy.Abort);
				camelContext.getExecutorServiceManager().registerThreadPoolProfile(threadPoolProfile);
				KafkaComponent kafkaComponent = new KafkaComponent();
				KafkaConfiguration kafkaConfig = new KafkaConfiguration();
				kafkaConfig.setBrokers(host + ":" + port);
				kafkaConfig.setGroupId(groupID);
				kafkaComponent.setConfiguration(kafkaConfig);
				camelContext.addComponent(KAFKA, kafkaComponent);
			}
		};
	}
}
