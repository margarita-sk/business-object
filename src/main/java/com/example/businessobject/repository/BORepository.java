package com.example.businessobject.repository;

import com.example.businessobject.bo.BusinessObject;
import java.util.List;
import java.util.Map;

public interface BORepository<T extends BusinessObject> {

    Long create(T bo);

    void update(T bo);

    void delete(T bo);

    List<T> find(Map<String, String> parameters);
}
