package com.hear.hear.Mappers;
import com.hear.hear.dtos.StudentScheduleDto;
import com.hear.hear.entities.Class;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserAllScheduleMapping {

    Set<StudentScheduleDto> toScheduleDto(Set<Class> classes);

}
