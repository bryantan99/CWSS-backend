package com.chis.communityhealthis.repository.disease;

import com.chis.communityhealthis.bean.DiseaseBean;
import com.chis.communityhealthis.repository.GenericDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class DiseaseDaoImpl extends GenericDaoImpl<DiseaseBean, Integer> implements DiseaseDao {
}
