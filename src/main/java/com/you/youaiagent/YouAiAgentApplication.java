package com.you.youaiagent;

import com.you.youaiagent.rag.LoveAppVectorStoreConfig;
//import com.you.youaiagent.rag.PgVectorVectorStoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YouAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouAiAgentApplication.class, args);
    }
}
