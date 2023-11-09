package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.RoleEntity;
import bg.softuni.childrenkitchen.model.entity.enums.UserRoleEnum;
import bg.softuni.childrenkitchen.repository.RoleRepository;
import bg.softuni.childrenkitchen.service.interfaces.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void initDB() {
        if (roleRepository.count() > 0){
            return;
        }
        RoleEntity admin = new RoleEntity();
        admin.setRole(UserRoleEnum.ADMIN);
        RoleEntity user = new RoleEntity();
        user.setRole(UserRoleEnum.USER);
        roleRepository.saveAll(List.of(admin, user));
    }

    @Override
    public Optional<RoleEntity> getRole(String role) {
       return roleRepository.findByRole( UserRoleEnum.valueOf(role));
    }
}
