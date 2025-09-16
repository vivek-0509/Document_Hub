package com.sstinternaltools.sstinternal_tools.policyChatbot.repository;

import com.sstinternaltools.sstinternal_tools.policyChatbot.entity.ChatBotDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatBotDocRepository extends JpaRepository<ChatBotDoc, Long> {
}
