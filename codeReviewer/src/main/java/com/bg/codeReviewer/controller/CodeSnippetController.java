package com.bg.codeReviewer.controller;

import com.bg.codeReviewer.model.CodeSnippet;
import com.bg.codeReviewer.service.ListCodeSnippet;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/api/code-snippet-records")
public class CodeSnippetController {

    private final ListCodeSnippet codeSnippetService;

    public CodeSnippetController(ListCodeSnippet codeSnippetService) {
        this.codeSnippetService = codeSnippetService;
    }

    @GetMapping
    public ResponseEntity<List<CodeSnippet>> getAllSnippets() {
        List<CodeSnippet> snippets = codeSnippetService.findAllSnippets();
        return ResponseEntity.ok(snippets);
    }
}
