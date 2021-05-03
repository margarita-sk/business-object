package com.example.businessobject.repository;

import com.example.businessobject.bo.AbstractEntityBO;
import lombok.AllArgsConstructor;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
abstract class AbstractEntityBORepository<T extends AbstractEntityBO> implements BORepository<T> {

    private final EntityManager entityManager;
    private Class<T> clazz;

    @PostConstruct
    public void init() {
        clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Long create(T bo) {
        this.entityManager.persist(bo);
        this.entityManager.flush();
        return bo.getId();
    }

    @Override
    public void update(T bo) {
        this.entityManager.merge(bo);
    }

    @Override
    public void delete(T bo) {
        this.entityManager.remove(bo);
    }

    @Override
    public List<T> find(Map<String, String> parameters) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
        var root = criteriaQuery.from(clazz);

        parameters.forEach((key, value) -> {
            var predicate = criteriaBuilder.equal(root.get(key), value);
            criteriaQuery.where(predicate);
        });

        var typedQuery = entityManager.createQuery(criteriaQuery);
        var resultList = typedQuery.getResultList();
        return resultList;
    }

}
