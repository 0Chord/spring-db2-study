package hello.itemservice.config;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.itemservice.repository.jpa.JpaItemRepositoryV3;
import hello.itemservice.repository.v2.ItemQueryRepositoryV2;
import hello.itemservice.repository.v2.ItemRepositoryV2;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV2;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class V2Config {

	private final ItemRepositoryV2 itemRepositoryV2;
	private final EntityManager em;

	@Bean
	public ItemService itemService() {
		return new ItemServiceV2(itemRepositoryV2, itemQueryRepositoryV2());
	}

	@Bean
	public ItemQueryRepositoryV2 itemQueryRepositoryV2() {
		return new ItemQueryRepositoryV2(em);
	}

	@Bean
	public JpaItemRepositoryV3 itemRepository() {
		return new JpaItemRepositoryV3(em);
	}
}
