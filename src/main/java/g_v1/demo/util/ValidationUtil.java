package g_v1.demo.util;

import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ValidationUtil {
    private final Validator validator;

    public void validate(Object o) {
        Set<ConstraintViolation<Object>> errors = validator.validate(o);
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
    }
}
