package com.chis.communityhealthis.service.dropdownChoice;

import com.chis.communityhealthis.bean.DiseaseBean;
import com.chis.communityhealthis.model.dropdown.DropdownChoiceModel;
import com.chis.communityhealthis.repository.disease.DiseaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DropdownChoiceServiceImpl implements DropdownChoiceService{

    @Autowired
    private DiseaseDao diseaseDao;

    @Override
    public List<DropdownChoiceModel> getDiseaseDropdownList() {
        List<DiseaseBean> beans = diseaseDao.getAll();
        List<DropdownChoiceModel> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(beans)) {
            for (DiseaseBean bean : beans) {
                list.add(new DropdownChoiceModel(bean.getDiseaseId().toString(), bean.getDiseaseName()));
            }
        }
        return list;
    }
}
