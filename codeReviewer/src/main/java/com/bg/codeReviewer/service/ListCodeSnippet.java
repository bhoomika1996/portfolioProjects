package com.bg.codeReviewer.service;

import com.bg.codeReviewer.model.CodeSnippet;
import com.bg.codeReviewer.repository.CodeSnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCodeSnippet {
    @Autowired
    private CodeSnippetRepository codeSnippetRepository;

    public List<CodeSnippet> findByUserId(String userId) {
        return codeSnippetRepository.findByUserId(userId);
    }

    public List<CodeSnippet> findAllSnippets() {
        return codeSnippetRepository.findAll();
    }
}
