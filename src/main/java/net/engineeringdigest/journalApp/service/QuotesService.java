package net.engineeringdigest.journalApp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QuotesService {
    @Value("${quotes.api.key}")
    private String apiKey;
}
