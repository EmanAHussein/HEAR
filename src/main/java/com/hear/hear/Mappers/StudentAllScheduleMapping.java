package com.hear.hear.Mappers;
import com.hear.hear.dtos.StudentScheduleDto;
import com.hear.hear.entities.Class;
import com.hear.hear.entities.Student;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.mapstruct.Mapper;

import java.time.DayOfWeek;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface StudentAllScheduleMapping {

    Set<StudentScheduleDto> toScheduleDto(Set<Class> classes);

}
