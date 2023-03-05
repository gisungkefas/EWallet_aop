package com.kefas.EWallet_aop.configuration;

import com.google.code.ssm.Cache;
import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.AbstractSSMConfiguration;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.providers.xmemcached.XMemcachedConfiguration;
import com.google.code.ssm.spring.ExtendedSSMCacheManager;
import com.google.code.ssm.spring.SSMCache;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableCaching
public class MemcachedConfig extends AbstractSSMConfiguration {

    @Value("localhost")
    private String memcachedHost;

    @Value("11211")
    private int memcachedPort;

    Logger logger = LoggerFactory.getLogger(MemcachedConfig.class);

    @Bean
    public MemcachedClient memcachedClient(){
        MemcachedClient client = null;

        try{
            client = new XMemcachedClient(memcachedHost, memcachedPort);
        } catch (NumberFormatException | IOException e){
            e.printStackTrace();
        }

        return client;
    }

    @Bean
    @Override
    public CacheFactory defaultMemcachedClient() {
        String serverString = memcachedHost+ ":"+memcachedPort;
        final XMemcachedConfiguration config = new XMemcachedConfiguration();
        config.setUseBinaryProtocol(true);

        final CacheFactory cacheFactory = new CacheFactory();
        cacheFactory.setCacheClientFactory(new MemcacheClientFactoryImpl());
        cacheFactory.setAddressProvider(new DefaultAddressProvider(serverString));
        cacheFactory.setConfiguration(config);
        return cacheFactory;
    }

    @Bean
    public CacheManagerCustomizer<ExtendedSSMCacheManager> cacheManagerCustomizer(){
        return cacheManager -> {
            Cache cache = null;

            try {
                cache = defaultMemcachedClient().getObject();
            } catch (Exception e){
                e.printStackTrace();
            }
            cacheManager.setCaches(Arrays.asList(new SSMCache(cache, 0, false)));
        };
    }
}
