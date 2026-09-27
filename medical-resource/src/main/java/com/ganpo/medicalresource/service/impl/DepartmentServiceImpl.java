package com.ganpo.medicalresource.service.impl;




import com.ganpo.medicalresource.entity.Department;
import com.ganpo.medicalresource.mapper.DepartmentMapper;
import com.ganpo.medicalresource.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> listAll() {
        return departmentMapper.selectList(null);
    }
}
