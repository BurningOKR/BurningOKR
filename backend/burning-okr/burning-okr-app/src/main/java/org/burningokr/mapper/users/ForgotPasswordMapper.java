package org.burningokr.mapper.users;

import org.burningokr.dto.users.ForgotPasswordDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.users.ForgotPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ForgotPasswordMapper implements DataMapper<ForgotPassword, ForgotPasswordDto> {
  private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordMapper.class);

  @Override
  public ForgotPassword mapDtoToEntity(ForgotPasswordDto input) {
    ForgotPassword output = new ForgotPassword();

    output.setEmail(input.getEmail());

    logger.debug("Mapped ForgotPasswordDto to ForgotPassword.");
    return output;
  }

  @Override
  public ForgotPasswordDto mapEntityToDto(ForgotPassword input) {
    ForgotPasswordDto output = new ForgotPasswordDto();

    output.setEmail(input.getEmail());

    logger.debug("Mapped ForgotPassword to ForgotPasswordDto.");
    return output;
  }

  @Override
  public Collection<ForgotPassword> mapDtosToEntities(Collection<ForgotPasswordDto> input) {
    return input.stream()
        .map(this::mapDtoToEntity)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public Collection<ForgotPasswordDto> mapEntitiesToDtos(Collection<ForgotPassword> input) {
    return input.stream()
        .map(this::mapEntityToDto)
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
