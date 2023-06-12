package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.RevisionDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.users.UserMapper;
import org.burningokr.model.okr.Revision;
import org.burningokr.model.okr.RevisionValueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class RevisionMapper implements DataMapper<Revision, RevisionDto> {

  private final UserMapper userMapper;

  @Autowired
  public RevisionMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public Revision mapDtoToEntity(RevisionDto input) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RevisionDto mapEntityToDto(Revision input) {
    RevisionDto revisionDto = new RevisionDto();
    revisionDto.setDate(input.getDate());
    if (input.getUser() != null) {
      revisionDto.setUser(input.getUser());
    }
    revisionDto.setRevisionType(input.getRevisionType());
    revisionDto.setChangedField(input.getChangedField());
    revisionDto.setRevisionValueType(input.getRevisionValueType());
    if (input.getRevisionValueType() == RevisionValueType.USER_COLLECTION) {
      revisionDto.setOldValue(input.getOldValue() == null ? Collections.EMPTY_LIST : input.getOldValue());
      revisionDto.setNewValue(input.getNewValue() == null ? Collections.EMPTY_LIST : input.getNewValue());
    }
    else {
      revisionDto.setOldValue(input.getOldValue());
      revisionDto.setNewValue(input.getNewValue());
    }
    return revisionDto;
  }

  @Override
  public Collection<Revision> mapDtosToEntities(Collection<RevisionDto> input) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<RevisionDto> mapEntitiesToDtos(Collection<Revision> input) {
    return input.stream().map(this::mapEntityToDto).collect(Collectors.toList());
  }

}
