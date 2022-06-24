package com.aecb.microblink.result.extract.blinkid;

import com.aecb.microblink.result.extract.BaseResultExtractorFactory;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;

public class BlinkIdResultExtractorFactory extends BaseResultExtractorFactory {

    @Override
    protected void addExtractors() {
        add(BlinkIdCombinedRecognizer.class,
                new BlinkIDCombinedRecognizerResultExtractor());
    }
}
