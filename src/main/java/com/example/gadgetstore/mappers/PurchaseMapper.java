package com.example.gadgetstore.mappers;

import org.mapstruct.Mapper;
import com.example.gadgetstore.entities.Purchase;
import com.example.gadgetstore.dtos.PurchaseDto;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseDto toDto(Purchase p);
    Purchase toEntity(PurchaseDto p);
}
