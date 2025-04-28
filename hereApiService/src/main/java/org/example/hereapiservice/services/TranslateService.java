package org.example.hereapiservice.services;

import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TranslateService {

    private final AmazonTranslate translateClient;

    @Value("${translation-default-target-language}")
    private String defaultTargetLanguage;

    @Autowired
    public TranslateService(AmazonTranslate translateClient) {
        this.translateClient = translateClient;
    }

    public String translate(String text) {
        TranslateTextRequest request = new TranslateTextRequest()
                .withText(text)
                .withSourceLanguageCode("auto")
                .withTargetLanguageCode(defaultTargetLanguage);
        TranslateTextResult result = translateClient.translateText(request);
        return result.getTranslatedText();
    }
}
