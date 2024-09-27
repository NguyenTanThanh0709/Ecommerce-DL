package com.example.productservice.Mapper;

import com.example.productservice.Entity.ElasticSearch.SizeQuantityE;
import com.example.productservice.Entity.SizeQuantity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SizeQuantityMapper {
    @Autowired
    private ModelMapper modelMapper;
    public  SizeQuantity toSizeQuantityJPA(SizeQuantityE sizeQuantityE){
        return modelMapper.map(sizeQuantityE, SizeQuantity.class);
    }

    public  SizeQuantityE toSizeQuantityElas(SizeQuantity sizeQuantity){
        return modelMapper.map(sizeQuantity, SizeQuantityE.class);
    }
}
