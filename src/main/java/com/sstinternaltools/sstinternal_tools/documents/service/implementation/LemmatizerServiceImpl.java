package com.sstinternaltools.sstinternal_tools.documents.service.implementation;

import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.LemmatizerService;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LemmatizerServiceImpl implements LemmatizerService {

    private StanfordCoreNLP pipeline;
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        pipeline = new StanfordCoreNLP(props);
    }

    @Override
    public String lemmatize(String word) {
        if (word == null || word.isBlank()) return "";

        word = word.trim().toLowerCase();

        return cache.computeIfAbsent(word, w -> {
            CoreDocument doc = new CoreDocument(w);
            pipeline.annotate(doc);
            List<CoreLabel> tokens = doc.tokens();
            if (tokens.isEmpty()) return w;
            return tokens.get(0).lemma(); // Return lemma of first token
        });
    }
}
