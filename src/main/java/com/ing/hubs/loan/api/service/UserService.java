package com.ing.hubs.loan.api.service;

import com.ing.hubs.loan.api.dto.UserDto;
import com.ing.hubs.loan.api.exception.ResourceNotFoundException;
import com.ing.hubs.loan.api.mapper.UserMapper;
import com.ing.hubs.loan.api.model.User;
import com.ing.hubs.loan.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        User User = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return UserMapper.toDto(User);
    }

    public UserDto findDtoByUsername(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userName " + userName));
        return UserMapper.toDto(user);
    }

    public User findByUsername(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userName " + userName));
    }



    public UserDto addUser(UserDto UserDto) {
        User User = UserMapper.toEntity(UserDto);
        return UserMapper.toDto(userRepository.save(User));
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
    }
    
}
