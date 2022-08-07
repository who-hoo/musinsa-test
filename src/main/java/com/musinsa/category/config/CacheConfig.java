package com.musinsa.category.config;

import java.util.Objects;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
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
		/*
		 * category 캐시
		 * 최대 10000개의 객체를 저장할 수 있으며, 1시간 이상 캐시에 저장되어 있을 경우 캐시에서 제거된다.
		 */
		CacheConfiguration categoryCacheConfiguration = new CacheConfiguration()
			.name("category")
			.maxEntriesLocalHeap(10000)
			.maxEntriesLocalDisk(1000)
			.eternal(false)
			.timeToIdleSeconds(0)
			.timeToLiveSeconds(3600)
			.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU);

		Cache categoryCache = new Cache(categoryCacheConfiguration);

		if (!Objects.requireNonNull(cacheManagerFactoryBean().getObject()).cacheExists("category")) {
			Objects.requireNonNull(cacheManagerFactoryBean().getObject()).addCache(categoryCache);
		}

		return new EhCacheCacheManager(Objects.requireNonNull(cacheManagerFactoryBean().getObject()));
	}
}
