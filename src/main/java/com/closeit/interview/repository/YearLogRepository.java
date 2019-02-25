package com.closeit.interview.repository;

import com.closeit.interview.dataobject.YearLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface YearLogRepository extends MongoRepository<YearLog, String> {

    YearLog findByYear(String year);
}
