package com.refinedmods.refinedstorage.platform.common.support.amount;

import java.util.Optional;
import javax.annotation.Nullable;

public interface AmountOperations<N extends Number> {
    String format(N value);

    Optional<N> parse(String value);

    Optional<N> validate(N amount, @Nullable N minAmount, @Nullable N maxAmount);

    N changeAmount(N current, int delta, @Nullable N minAmount, @Nullable N maxAmount);
}
