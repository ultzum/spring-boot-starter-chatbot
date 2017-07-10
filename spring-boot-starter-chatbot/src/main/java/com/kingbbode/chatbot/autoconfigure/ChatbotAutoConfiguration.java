package com.kingbbode.chatbot.autoconfigure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.kingbbode.chatbot.autoconfigure.base.emoticon.component.EmoticonComponent;
import com.kingbbode.chatbot.autoconfigure.base.knowledge.component.KnowledgeComponent;
import com.kingbbode.chatbot.autoconfigure.base.stat.StatComponent;
import com.kingbbode.chatbot.autoconfigure.brain.DispatcherBrain;
import com.kingbbode.chatbot.autoconfigure.brain.aop.BrainCellAspect;
import com.kingbbode.chatbot.autoconfigure.brain.factory.BrainFactory;
import com.kingbbode.chatbot.autoconfigure.common.properties.BotProperties;
import com.kingbbode.chatbot.autoconfigure.common.util.RestTemplateFactory;
import com.kingbbode.chatbot.autoconfigure.conversation.ConversationService;
import com.kingbbode.chatbot.autoconfigure.event.EventQueue;
import com.kingbbode.chatbot.autoconfigure.event.TaskRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import static com.kingbbode.chatbot.autoconfigure.common.util.RestTemplateFactory.getRestOperations;

/**
 * Created by YG on 2017-07-10.
 */
@Configuration
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(ChatbotProperties.class)
@Import(TeamUpAutoConfiguration.class)
public class ChatbotAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public BrainFactory brainFactory(){
        return new BrainFactory();
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(JedisConnectionFactory.class)
    public ConversationService conversationService(){
        return new ConversationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventQueue eventQueue(){
        return new EventQueue();
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(JedisConnectionFactory.class)
    public StringRedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean(name = "messageRestOperations")
    @ConditionalOnMissingBean
    public RestOperations messageRestOperations() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(1000);
        return getRestOperations(factory);
    }

    @Bean(name = "fileRestOperations")
    public RestOperations fileRestOperations() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(20000);
        factory.setReadTimeout(20000);
        RestTemplate restTemplate = (RestTemplate) RestTemplateFactory.getRestOperations(factory);
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        return restTemplate;
    }

    @Bean(name = "eventRestOperations")
    public RestOperations eventRestOperations() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        return getRestOperations(factory);
    }

    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolTaskExecutor threadPoolTaskExecutorDefault() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean    
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chatbot", name = "enableEmoticon", havingValue = "true")
    public EmoticonComponent emoticonComponent(){
        return new EmoticonComponent();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chatbot", name = "enableKnowledge", havingValue = "true")
    public KnowledgeComponent knowledgeComponent(){
        return new KnowledgeComponent();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public StatComponent statComponent(){
        return new StatComponent();
    }

    @Bean
    @Profile({"bot"})
    public BotProperties realBotConfig(){
        return new BotProperties("#", false);
    }

    @Bean
    @Profile({"dev"})
    public BotProperties devBotConfig(){
        return new BotProperties("#@", true);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DispatcherBrain dispatcherBrain(){
        return new DispatcherBrain();
    }

    @Bean
    @ConditionalOnMissingBean
    public BrainCellAspect brainCellAspect(){
        return new BrainCellAspect();
    }
}
