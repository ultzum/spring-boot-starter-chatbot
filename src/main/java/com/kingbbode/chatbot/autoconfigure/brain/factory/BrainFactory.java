package com.kingbbode.chatbot.autoconfigure.brain.factory;

import com.kingbbode.chatbot.autoconfigure.ChatbotProperties;
import com.kingbbode.chatbot.autoconfigure.base.emoticon.component.EmoticonComponent;
import com.kingbbode.chatbot.autoconfigure.base.knowledge.component.KnowledgeComponent;
import com.kingbbode.chatbot.autoconfigure.brain.cell.*;
import com.kingbbode.chatbot.autoconfigure.common.annotations.Brain;
import com.kingbbode.chatbot.autoconfigure.common.annotations.BrainCell;
import com.kingbbode.chatbot.autoconfigure.common.enums.BrainRequestType;
import com.kingbbode.chatbot.autoconfigure.common.properties.BotProperties;
import org.apache.commons.lang3.text.WordUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kingbbode.chatbot.autoconfigure.brain.cell.AbstractBrainCell.NOT_FOUND_BRAIN_CELL;

/**
 * Created by YG on 2017-01-23.
 */
public class BrainFactory {

    private static final Logger logger = LoggerFactory.getLogger(BrainFactory.class);

    @Autowired
    private BotProperties botProperties;
    
    @Autowired
    private BeanFactory beanFactory;
    
    @Autowired
    private EmoticonComponent emoticon;
    
    @Autowired
    private KnowledgeComponent knowledge;
    
    @Autowired
    private ChatbotProperties chatbotProperties;

    private KnowledgeBrainCell knowledgeBrainCell;
    
    private EmoticonBrainCell emoticonBrainCell;
    
    //#command, functionKey
    private Map<String, String> commandMap;
    //functionKey, BrainCell
    private Map<String, AbstractBrainCell> keyMap;
    //functionKey, ConversationInfo
    private Map<String, ConversationInfo> conversationInfoMap;
    
    //private Map<String, AbstractBrainCell> brainCellMap;

    @PostConstruct
    public void init() throws InvocationTargetException, IllegalAccessException, IOException {
        knowledgeBrainCell = new KnowledgeBrainCell(knowledge);
        emoticonBrainCell = new EmoticonBrainCell(emoticon);
        this.load(false);
    }
    
    public void update() throws IOException, InvocationTargetException, IllegalAccessException {
        this.load(true);
    }

    @SuppressWarnings("unchecked")
    public void load(boolean isUpdate) throws InvocationTargetException, IllegalAccessException, IOException {
        if(isUpdate){
            knowledge.init();
            emoticon.init();
        }
        Set<String> keyChecker = new HashSet<>();
        Map<String, String> command = new HashMap<>();
        Map<String, Map<String, String>> childCommand = new HashMap<>();
        Map<String, AbstractBrainCell> key = new HashMap<>();
        Map<String, ConversationInfo> conversationInfo = new HashMap<>();
        registerDefault(keyChecker, command, childCommand, key);
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(chatbotProperties.getBasePackage())).setScanners(
                        new TypeAnnotationsScanner(), new SubTypesScanner()));
        for(Class<?> clazz : reflections.getTypesAnnotatedWith(Brain.class)){
            if(clazz.getAnnotation(Brain.class).request() == BrainRequestType.COMMON) {
                registerCommonBrainCellByClass(keyChecker, command, childCommand, key, clazz);
            }else if(clazz.getAnnotation(Brain.class).request() == BrainRequestType.API){
                registerApiBrainCellByClass(isUpdate, command, key, clazz);
            }
        }
        
        childCommand.forEach((key2, value2) -> {
            ConversationInfo info = new ConversationInfo();
            value2.forEach((key1, value1) -> {
                if (key1.equals(key2 + "query")) {
                    info.setQuery(value1);
                } else {
                    info.add(key1, value1);
                }
            });
            conversationInfo.put(key2, info);
        });
        logger.info("Load Brain command : {}, key : {}, conversation : {}", command.size(), key.size(), conversationInfo.size());
        this.commandMap = command;
        this.keyMap = key;
        this.conversationInfoMap = conversationInfo;
    }

    private void registerDefault(Set<String> keyChecker, Map<String, String> command, Map<String, Map<String, String>> childCommand, Map<String, AbstractBrainCell> key) {
        Reflections defaults = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.kingbbode.chatbot.autoconfigure.base")).setScanners(
                        new TypeAnnotationsScanner(), new SubTypesScanner()));
        for(Class<?> clazz : defaults.getTypesAnnotatedWith(Brain.class)){
            if(clazz.getAnnotation(Brain.class).request() == BrainRequestType.COMMON) {
                if("EmoticonBrain".equals(clazz.getSimpleName()) && !chatbotProperties.isEnableEmoticon()){
                    continue;
                }else if("KnowledgeBrain".equals(clazz.getSimpleName()) && !chatbotProperties.isEnableKnowledge()){
                    continue;
                }else if("BaseBrain".equals(clazz.getSimpleName()) && !chatbotProperties.isEnableBase()){
                    continue;
                }
                registerCommonBrainCellByClass(keyChecker, command, childCommand, key, clazz);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void registerApiBrainCellByClass(boolean isUpdate, Map<String, String> command, Map<String, AbstractBrainCell> key, Class<?> clazz) throws IllegalAccessException, InvocationTargetException {
        Method execute = null;
        Method getCommands = null;
        Method update = null;
        for(Method method : clazz.getMethods()){
            switch (method.getName()) {
                case "execute":
                    execute = method;
                    break;
                case "getCommands":
                    getCommands = method;
                    break;
                case "update":
                    update = method;
                    break;
            }
        }
        if(execute == null || getCommands == null){
            return;
        }
        if(isUpdate && update != null){
            update.invoke(beanFactory.getBean(WordUtils.uncapitalize(clazz.getSimpleName())));
        }
        Set<String> list = (Set<String>) getCommands.invoke(beanFactory.getBean(WordUtils.uncapitalize(clazz.getSimpleName())));
        for(String cmd : list){
            command.put(cmd, cmd);
            key.put(cmd, new ApiBrainCell(clazz, execute, beanFactory));
        }
    }

    private void registerCommonBrainCellByClass(Set<String> keyChecker, Map<String, String> command, Map<String, Map<String, String>> childCommand, Map<String, AbstractBrainCell> key, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(BrainCell.class)) {
                BrainCell brainCell = method.getAnnotation(BrainCell.class);
                if("".equals(brainCell.parent())) {
                    command.put(botProperties.getCommandPrefix() + brainCell.key(), brainCell.function());
                }else{
                    if(!childCommand.containsKey(brainCell.parent())){
                        childCommand.put(brainCell.parent(), new HashMap<>());
                    }
                    childCommand.get(brainCell.parent()).put(brainCell.key().equals("query")?brainCell.parent() + brainCell.key():brainCell.key(), brainCell.function());
                }
                key.put(brainCell.function(), new CommonBrainCell(brainCell, clazz, method, beanFactory));
                String chkKey = brainCell.parent() + brainCell.key();
                if(keyChecker.contains(chkKey)){
                    throw new RuntimeException("중복된 Key 값이 존재합니다." + chkKey);
                }
                keyChecker.add(chkKey);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractBrainCell> T get(String command){
        if(commandMap.containsKey(command)){
            return (T) keyMap.get(commandMap.get(command));
        }
        return (T)(
                (
                    chatbotProperties.isEnableEmoticon() && 
                    command.startsWith(botProperties.getEmoticonPrefix()) && 
                    emoticon.contains(command.substring(1, command.length()))
                )? 
                emoticonBrainCell : 
                (chatbotProperties.isEnableKnowledge()?
                        knowledgeBrainCell:
                        NOT_FOUND_BRAIN_CELL));
    }

    public boolean containsFunctionKey(String function){
        return keyMap.containsKey(function);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractBrainCell> T getByFunctionKey(String function){
        return (T) keyMap.get(function);
    }
    
    public boolean containsConversationInfo(String function){
        return conversationInfoMap.containsKey(function);
    }
    
    public ConversationInfo getConversationInfo(String function){
        return conversationInfoMap.get(function);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractBrainCell> Set<BrainCellInfo<T>> findBrainCellByType(Class<T> type){
        return commandMap.entrySet().stream()
                .filter(entry -> keyMap.containsKey(entry.getValue()))
                .filter(entry -> keyMap.get(entry.getValue()).getClass().equals(type))
                .map(entry -> new BrainCellInfo<>(entry.getKey(), (T) keyMap.get(entry.getValue())))
                .collect(Collectors.toSet());
    }
    
    public static class BrainCellInfo<T extends AbstractBrainCell> {
        private String command;
        private T brainCell;

        BrainCellInfo(String command, T brainCell) {
            this.command = command;
            this.brainCell = brainCell;
        }

        @Override
        public String toString() {
            return command +
                    " : " +
                    brainCell.explain();
        }

        public String getCommand() {
            return command;
        }
    }

    public static class ConversationInfo {
        private Map<String, String> afters = new HashMap<>();
        private String query;

        public void setQuery(String query) {
            this.query = query;
        }

        void add(String key, String value){
            afters.put(key, value);
        }
        
        
        public String findFunctionKey(String command){
            if(afters.containsKey(command)){
                return afters.get(command);
            }
            return query;
        }
        
        public String example() {
            StringBuilder builder = new StringBuilder();
            this.afters.keySet().forEach(key -> {
                builder.append("'");
                builder.append(key);
                builder.append("'");
                builder.append(" ");
            });
            
            return builder.toString() + "중에 입력해주세요.";
        }
    }
}
