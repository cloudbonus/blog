package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.UserDetailsDao;
import com.github.blog.dto.UserDetailsDto;
import com.github.blog.model.UserDetails;
import com.github.blog.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDetailsDao userDetailsDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserDetailsServiceImpl(UserDetailsDao userDetailsDao, ObjectMapper objectMapper) {
        this.userDetailsDao = userDetailsDao;
        this.objectMapper = objectMapper;
    }

    public int create(UserDetailsDto userDetailsDto) {
        UserDetails userDetails = convertToObject(userDetailsDto);
        return userDetailsDao.save(userDetails);
    }

    public UserDetailsDto readById(int id) {
        Optional<UserDetails> result = userDetailsDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("User Details not found");
        }
        return convertToDto(result.get());
    }

    public List<UserDetailsDto> readAll() {
        List<UserDetails> userDetails = userDetailsDao.getAll();
        if (userDetails.isEmpty()) {
            throw new RuntimeException("Cannot find any user details");
        }
        return userDetails.stream().map(this::convertToDto).toList();
    }

    public UserDetailsDto update(int id, UserDetailsDto userDetailsDto) {
        Optional<UserDetails> result = userDetailsDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User Details not found");
        }

        UserDetails updatedUserDetails = convertToObject(userDetailsDto);
        UserDetails userDetails = result.get();

        updatedUserDetails.setId(userDetails.getId());

        result = userDetailsDao.update(updatedUserDetails);

        if (result.isEmpty()) {
            throw new RuntimeException("Couldn't update user details");
        }

        return convertToDto(result.get());
    }

    public boolean delete(int id) {
        return userDetailsDao.deleteById(id);
    }

    private UserDetails convertToObject(UserDetailsDto userDetailsDto) {
        return objectMapper.convertValue(userDetailsDto, UserDetails.class);
    }

    private UserDetailsDto convertToDto(UserDetails userDetails) {
        return objectMapper.convertValue(userDetails, UserDetailsDto.class);
    }

}
