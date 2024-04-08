package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.UserDetailsDao;
import com.github.blog.dto.UserDetailsDto;
import com.github.blog.model.UserDetails;
import com.github.blog.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDetailsDao userDetailsDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserDetailsServiceImpl(UserDetailsDao userDetailsDao, ObjectMapper objectMapper) {
        this.userDetailsDao = userDetailsDao;
        this.objectMapper = objectMapper;
    }

    public UserDetailsDto create(UserDetailsDto userDetailsDto) {
        UserDetails userDetails = convertToObject(userDetailsDto);
        return convertToDto(userDetailsDao.create(userDetails));
    }

    public UserDetailsDto findById(int id) {
        Optional<UserDetails> result = userDetailsDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("User Details not found");
        }
        return convertToDto(result.get());
    }

    public List<UserDetailsDto> findAll() {
        List<UserDetails> userDetails = userDetailsDao.findAll();
        if (userDetails.isEmpty()) {
            throw new RuntimeException("Cannot find any user details");
        }
        return userDetails.stream().map(this::convertToDto).toList();
    }

    public UserDetailsDto update(int id, UserDetailsDto userDetailsDto) {
        Optional<UserDetails> result = userDetailsDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User Details not found");
        }

        UserDetails updatedUserDetails = convertToObject(userDetailsDto);
        UserDetails userDetails = result.get();

        updatedUserDetails.setId(userDetails.getId());

        updatedUserDetails = userDetailsDao.update(updatedUserDetails);

        return convertToDto(updatedUserDetails);
    }

    public int remove(int id) {
        UserDetails userDetails = userDetailsDao.remove(id);
        if (userDetails == null) {
            return -1;
        } else return userDetails.getId();
    }

    private UserDetails convertToObject(UserDetailsDto userDetailsDto) {
        return objectMapper.convertValue(userDetailsDto, UserDetails.class);
    }

    private UserDetailsDto convertToDto(UserDetails userDetails) {
        return objectMapper.convertValue(userDetails, UserDetailsDto.class);
    }

}
