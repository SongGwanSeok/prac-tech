package org.example.javaconcert.concert;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@DependsOn("entityManagerFactory")
public class DbCleaner {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
            .filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
            .map(EntityType::getName)
            .toList();
    }

    @Transactional
    public void clean() {
        entityManager.flush();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for (String tableName : tableNames) {
            try {
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName.toLowerCase()).executeUpdate();
                entityManager.createNativeQuery("ALTER TABLE " + tableName.toLowerCase() + " AUTO_INCREMENT = 1")
                    .executeUpdate();
            } catch (Exception e) {
                System.out.println("테이블 " + tableName.toLowerCase() + "을 찾을 수 없습니다.");
            }
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}
