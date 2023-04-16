package com.quanht.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository{

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductCategoryRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean deleteByCategoryId(Long id) {
        String query = "DELETE FROM product_category WHERE category_id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }
}
