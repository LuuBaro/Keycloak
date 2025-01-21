package org.example.asm_1.mapper;

import org.example.asm_1.dto.request.RegisterRequest;
import org.example.asm_1.dto.request.UpdateRequest;
import org.example.asm_1.dto.response.ProfileResponse;
import org.example.asm_1.model.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(RegisterRequest request);
    ProfileResponse toProfileResponse(Profile profile);
    Profile toUpdateProfile(UpdateRequest request);
    UpdateRequest toUpdateUserResponse(Profile profile);

}
