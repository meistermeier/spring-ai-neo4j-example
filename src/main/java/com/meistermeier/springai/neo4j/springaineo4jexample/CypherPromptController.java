package com.meistermeier.springai.neo4j.springaineo4jexample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Gerrit Meier
 */
@Controller
@ResponseBody
public class CypherPromptController {

	private final CypherPromptService promptService;

	public CypherPromptController(CypherPromptService promptService) {
		this.promptService = promptService;
	}

	@GetMapping("/")
	public String convertToCypher(@RequestParam("m") String message) {
		return this.promptService.convertToCypher(message);
	}

	@GetMapping("/create")
	public String createDocuments(@RequestParam("filePath") String filePath) {
		this.promptService.createEmbeddings(filePath);
		return "done";
	}
}
