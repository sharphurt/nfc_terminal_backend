package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "Registration Request", description = "The registration request payload")
public class ClientRequestBody {
    @NotBlank(message = "First name cannot be blank")
    @ApiModelProperty(value = "A valid first name", required = true, allowableValues = "NonEmpty String")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @ApiModelProperty(value = "A valid last name", required = true, allowableValues = "NonEmpty String")
    private String lastName;

    @ApiModelProperty(value = "Patronymic")
    private String patronymic;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Length(min = 8, max = 50, message = "Password length must be between 8 and 50 characters")
    @ApiModelProperty(value = "A valid password string", required = true, allowableValues = "NonEmpty String")
    private String password;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "\\+7[\\d]{10}", message = "Invalid phone value")
    @ApiModelProperty(value = "A valid phone string", required = true, allowableValues = "NonEmpty String")
    private String phone;

    public ClientRequestBody(String email, String firstName, String lastName, String patronymic, String password, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public ClientRequestBody() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPhone() {
        return phone;
    }
}
