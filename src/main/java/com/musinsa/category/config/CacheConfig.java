package com.musinsa.category.config;

import java.util.Objects;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

	@Bean
	public EhCacheManagerFactoryBean cacheManagerFactoryBean() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setShared(true);
		return ehCacheManagerFactoryBean;
	}

	@Bean
	public EhCacheCacheManager ehCacheCacheManager() {
		CacheConfiguration categoryCacheConfiguration = new CacheConfiguration()
			.name("category")
			.maxEntriesLocalHeap(10000)
			.maxEntriesLocalDisk(1000)
			.eternal(false) //true일 경우, timeout 관련 설정이 무시되어, element가 캐시에서 삭제되지 않음
			.timeToIdleSeconds(0) //지정한 시간 동안 사용(조회)되지 않으면 캐시에서 제거
			.timeToLiveSeconds(21600) //지정한 시간이 지나면 캐시에서 제거
			.memoryStoreEvictionPolicy("LRU");

		Cache categoryCache = new Cache(categoryCacheConfiguration);

		Objects.requireNonNull(cacheManagerFactoryBean().getObject()).addCache(categoryCache);

		return new EhCacheCacheManager(Objects.requireNonNull(cacheManagerFactoryBean().getObject()));
	}
}
