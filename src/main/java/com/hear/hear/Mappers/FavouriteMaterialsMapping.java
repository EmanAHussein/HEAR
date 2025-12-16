package com.hear.hear.Mappers;

import com.hear.hear.dtos.FavMaterials;
import com.hear.hear.dtos.UserDto;
import com.hear.hear.entities.Materials;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface FavouriteMaterialsMapping {

    Set<FavMaterials> getFavouriteMaterials(Set<Materials> materials);
}
