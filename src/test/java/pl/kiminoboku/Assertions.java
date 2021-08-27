package pl.kiminoboku;


import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.api.AbstractAssert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Assertions {
    public static  <VALUE> OptionAssert<VALUE> assertThat(Option<VALUE> option) {
        return new OptionAssert<>(option);
    }

    public static class OptionAssert<VALUE> extends AbstractAssert<OptionAssert<VALUE>, Option<VALUE>> {
        public OptionAssert(Option<VALUE> value) {
            super(value, OptionAssert.class);
        }

        public OptionAssert<VALUE> isDefined() {
            isNotNull();
            if (actual.isEmpty()) {
                failWithMessage("Option expected to be defined but is empty");
            }
            return this;
        }

        public OptionAssert<VALUE> isEmpty() {
            isNotNull();
            if(actual.isDefined()) {
                failWithMessage("Option expected to be empty but is defined");
            }
            return this;
        }
    }
}
