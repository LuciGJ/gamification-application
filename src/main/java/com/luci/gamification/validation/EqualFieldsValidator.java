package com.luci.gamification.validation;

import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EqualFieldsValidator implements ConstraintValidator<ValidEqualFields, Object> {

	// check if two fields are equal, used for password confirmation
	
	private String firstField;
	private String secondField;
	private String message;

	@Override
	public void initialize(ValidEqualFields constraintAnnotation) {
		firstField = constraintAnnotation.first();
		secondField = constraintAnnotation.second();
		message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// get the values and check if they are the same object or if they are equal
		final Object firstValue = new BeanWrapperImpl(value).getPropertyValue(firstField);
		final Object secondValue = new BeanWrapperImpl(value).getPropertyValue(secondField);
		boolean valid = firstValue == secondValue || firstValue.equals(secondValue);

		// if they are not equal display a message
		
		if (!valid) {
			context.buildConstraintViolationWithTemplate(message).addPropertyNode(firstField).addConstraintViolation()
					.disableDefaultConstraintViolation();
		}
		return valid;
	}

}
