package com.cy.store.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.Serializable;

//q:
@Data
@NoArgsConstructor
public class UserForm implements Serializable {

    @NotEmpty(message = "名前を入力してください。")
    @Size(max=20,message = "名前は20桁以内で入力してください。")
    private String username;

    @NotEmpty(message="電話番号を入力してください。")
    private String phone;

    private String email;

    private int gender;

}
