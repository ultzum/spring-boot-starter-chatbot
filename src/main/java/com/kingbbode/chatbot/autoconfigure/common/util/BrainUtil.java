package com.kingbbode.chatbot.autoconfigure.common.util;

import com.kingbbode.chatbot.autoconfigure.brain.cell.AbstractBrainCell;
import com.kingbbode.chatbot.autoconfigure.brain.factory.BrainFactory;

import java.util.*;

/**
 * Created by YG on 2016-04-12.
 */
public class BrainUtil {
    public static List sortByValue(Map<String, String> map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());

        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                int n1 = Integer.parseInt(map.get(o1).split("@")[0]);
                int n2 = Integer.parseInt(map.get(o2).split("@")[0]);

                return n1 > n2 ? -1 : 1;
            }
        });
        Collections.reverse(list);
        return list;
    }


    public static <T extends AbstractBrainCell> String explainDetail(Set<BrainFactory.BrainCellInfo<T>> entrySet) {
        StringBuilder stringBuilder = new StringBuilder();
        entrySet.stream()
                .forEach(info -> {
                            stringBuilder.append(info.toString());
                            stringBuilder.append("\n");
                        }
                );
        return stringBuilder.toString();
    }

    public static <T extends AbstractBrainCell> String explainSimple(Set<BrainFactory.BrainCellInfo<T>> entrySet) {
        StringBuilder stringBuilder = new StringBuilder();
        entrySet.stream()
                .forEach(info -> {
                            stringBuilder.append(info.getCommand());
                            stringBuilder.append("\n");
                        }
                );
        return stringBuilder.toString();
    }
    
    public static String explainForKnowledge(Set<Map.Entry<String, List<String>>> entrySet){
        StringBuilder stringBuilder = new StringBuilder();
        entrySet.stream()
                .forEach(entry ->{
                    stringBuilder.append(entry.getKey());
                    stringBuilder.append(" - 지식깊이 : ");
                    stringBuilder.append(entry.getValue().size());
                    stringBuilder.append("\n");
                });
        
        return stringBuilder.toString();
    }

    public static String explainForEmoticon(Map<String, String> map){
        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) ->{
                    stringBuilder.append(key);
                    stringBuilder.append("\n");
                });
        return stringBuilder.toString();
    }
}
