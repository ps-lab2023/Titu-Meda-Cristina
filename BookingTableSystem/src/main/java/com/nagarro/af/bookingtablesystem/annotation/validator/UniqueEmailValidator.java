package com.nagarro.af.bookingtablesystem.annotation.validator;

import com.nagarro.af.bookingtablesystem.annotation.UniqueEmail;
import com.nagarro.af.bookingtablesystem.dto.UserDTO;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import com.nagarro.af.bookingtablesystem.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserService<? extends UserDTO> userService;

    public UniqueEmailValidator(UserService<? extends UserDTO> userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            userService.findByEmail(email);
            return false;
        } catch (NotFoundException e) {
            return true;
        }
    }
}
