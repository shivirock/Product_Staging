package com.karmaya.fulvila.repository;

import com.karmaya.fulvila.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, Long> {
    Admin findByEmail(String email);
    Admin findByEmailAndAuthProvider(String email, Admin.AuthProvider authProvider);
}
