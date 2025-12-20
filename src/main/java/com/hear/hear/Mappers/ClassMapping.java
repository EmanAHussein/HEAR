package com.hear.hear.Mappers;

import com.hear.hear.dtos.ClassDto;
import com.hear.hear.dtos.RegisterClassDto;
import com.hear.hear.dtos.RegisterCourseDto;
import com.hear.hear.dtos.UpdateClassDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import com.hear.hear.entities.Class;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassMapping {
    Class toClass(RegisterClassDto registerClassDto);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateFromDto(
            UpdateClassDto updateClassDto,
            @MappingTarget Class updatedClass
    );

    ClassDto toDto(Class aClass);
    List<ClassDto> toDto(List<Class> classList);
}
