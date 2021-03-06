package springboot.core.core.validator;

import springboot.core.anno.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

/**
 * hibernate validator自定义注解实现
 */

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private String regexp;

    @Override
    public void initialize(Phone phone) {
        this.regexp = phone.regexp();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isBlank(s) || s.matches(regexp);
    }
}

