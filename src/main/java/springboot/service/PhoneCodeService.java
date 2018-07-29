package springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.entity.PhoneCode;
import springboot.mapper.PhoneCodeMapper;

@Service
public class PhoneCodeService {

    @Autowired
    private PhoneCodeMapper phoneCodeMapper;



    public PhoneCode findPhoneCodeByPhoneAndCodeAndType(String phone, String code, String type){
        return phoneCodeMapper.findPhoneCodeByPhoneAndCodeAndType(phone,code,type);
    }

    public int createPhoneCode(PhoneCode phoneCode){
        return phoneCodeMapper.insert(phoneCode);
    }

    public PhoneCode findPhoneCodeById(String id){
        return  phoneCodeMapper.selectByPrimaryKey(id);
    }

    public int updatePhoneCode(PhoneCode phoneCode){
        return phoneCodeMapper.updateByPrimaryKeySelective(phoneCode);
    }

}
