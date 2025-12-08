package com.hear.hear.Repositories;
import com.hear.hear.entities.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;
import java.util.Set;

public interface ClassRepository extends JpaRepository<Class,Integer> {


}
