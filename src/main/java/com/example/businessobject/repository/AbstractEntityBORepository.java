package com.example.businessobject.repository;

import com.example.businessobject.bo.AbstractEntityBO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

abstract class AbstractEntityBORepository<T extends AbstractEntityBO> implements BORepository<T> {

    private final EntityManager entityManager;
    private Class<T> clazz;

    public AbstractEntityBORepository(EntityManager entityManager){
        this.entityManager = entityManager;
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root root = criteriaQuery.from(clazz);

        parameters.forEach((key, value) -> {
            Predicate predicate = criteriaBuilder.equal(root.get(key), value);
            criteriaQuery.where(predicate);
        });

        TypedQuery typedQuery = entityManager.createQuery(criteriaQuery);
        List resultList = typedQuery.getResultList();
        return resultList;
    }

}
