package com.example.productservice.Mapper;

import com.example.productservice.Entity.Category;
import com.example.productservice.Entity.ElasticSearch.CategoryE;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CategoryMapper {
    @Autowired
    private ModelMapper modelMapper;

    public  Category toCategoryJPA (CategoryE categoryE){
        return modelMapper.map(categoryE, Category.class);
    }
    public  CategoryE toCategoryElas(Category category){
        return modelMapper.map(category, CategoryE.class);
    }
}
