package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * ${table.comment!} 服务类
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>() {

}
<#else>
public class ${table.serviceImplName} extends BaseService<${table.mapperName}, ${entity}> {

    /**
    * @Description: 导入 ${entity} (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: ${author}
    * @Date: ${date}
    */
    public boolean imput${entity}s(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<${entity}> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),${entity}.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

}
</#if>
