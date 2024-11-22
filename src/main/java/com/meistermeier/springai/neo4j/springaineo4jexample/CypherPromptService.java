package com.meistermeier.springai.neo4j.springaineo4jexample;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Gerrit Meier
 */
@Service
public class CypherPromptService {

	private final VectorStore vectorStore;

	private final ChatClient chatClient;

	private static final String SYSTEM_PROMPT_TEMPLATE = """
			You are an assistant that gives out Cypher code snippets.
			Use the information from the DOCUMENTS section only to provide accurate answers.
			Return just the code snippet without formatting. No descriptive text.
			Don't use any learned knowledge that is not within the DOCUMENTS section.
				
			DOCUMENTS:
			{documents}""";

	public CypherPromptService(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
		this.vectorStore = vectorStore;
		this.chatClient = chatClientBuilder.build();
	}


	public String convertToCypher(String message) {

		var result = vectorStore.similaritySearch(message);
		String documents = result.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));
		var systemMessage = new SystemPromptTemplate(SYSTEM_PROMPT_TEMPLATE)
				.createMessage(Map.of("documents", documents));

		var userMessage = new UserMessage(message);

		var prompt = new Prompt(List.of(systemMessage, userMessage));
		ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

		return response.getResult().getOutput().getContent();
	}

	public void createEmbeddings(String filePath) {
		List<Document> parsedDocuments = new PagePdfDocumentReader(filePath).get();
		parsedDocuments.forEach(document -> document.getMetadata().clear());

		vectorStore.add(parsedDocuments);
	}
}
