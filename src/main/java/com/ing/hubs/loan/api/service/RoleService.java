package com.ing.hubs.loan.api.service;

import com.ing.hubs.loan.api.dto.RoleDto;
import com.ing.hubs.loan.api.exception.ResourceNotFoundException;
import com.ing.hubs.loan.api.mapper.RoleMapper;
import com.ing.hubs.loan.api.model.Role;
import com.ing.hubs.loan.api.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }

    public RoleDto findById(Long id) {
        Role Role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + id));
        return RoleMapper.toDto(Role);
    }

    public RoleDto addRole(RoleDto RoleDto) {
        Role Role = RoleMapper.toEntity(RoleDto);
        return RoleMapper.toDto(roleRepository.save(Role));
    }

    public void deleteRole(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Role with id " + id + " not found");
        }
    }

}
