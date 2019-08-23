package org.benetech.servicenet.matching.counter;

import java.math.BigDecimal;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WeightProvider {

    @Value("${similarity-ratio.weight.alternate-name}")
    private BigDecimal alternateNameWeight;

    @Value("${similarity-ratio.weight.description}")
    private BigDecimal descriptionWeight;

    @Value("${similarity-ratio.weight.email.base}")
    private BigDecimal emailWeight;

    @Value("${similarity-ratio.weight.location.base}")
    private BigDecimal locationWeight;

    @Value("${similarity-ratio.weight.name.base}")
    private BigDecimal nameWeight;

    @Value("${similarity-ratio.weight.url.base}")
    private BigDecimal urlWeight;

    @Value("${similarity-ratio.weight.year-incorporated.base}")
    private BigDecimal yearsIncorporatedWeight;
}
