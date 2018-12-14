package org.benetech.servicenet.matching;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WeightProvider {

    @Value("${similarity-ratio.weight.alternate-name}")
    private float alternateNameWeight;

    @Value("${similarity-ratio.weight.description}")
    private float descriptionWeight;

    @Value("${similarity-ratio.weight.email.base}")
    private float emailWeight;

    @Value("${similarity-ratio.weight.location}")
    private float locationWeight;

    @Value("${similarity-ratio.weight.name}")
    private float nameWeight;

    @Value("${similarity-ratio.weight.url.base}")
    private float urlWeight;

    @Value("${similarity-ratio.weight.year-incorporated.base}")
    private float yearsIncorporatedWeight;
}
