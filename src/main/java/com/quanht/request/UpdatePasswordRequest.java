package com.quanht.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdatePasswordRequest {

    private String oldPassword;
    private String newPassword;

}
