package ${package.Controller};

import ${package}.Util.FileUtil;
import ${package}.Util.Msg;
import ${package.Entity}.${entity};
import ${package.ServiceImpl}.${table.serviceImplName};
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Api(tags = {"${table.comment!}接口"})
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${entity}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>

    @Autowired
    ${table.serviceImplName} baseService;

    /**
    * @Description: 新增 ${table.comment!}
    * @Param: [${table.name}]
    * @Author: ${author}
    * @Date: 2018/9/30
    */
    @ApiOperation("新增 ${entity}")
    @RequestMapping(value = "/save${entity}",method = RequestMethod.PUT)
    <#if restControllerStyle>
    <#else>@ResponseBody
    </#if>
    private Msg save${entity}(@RequestBody ${entity} ${table.name}){
        boolean state=baseService.save(${table.name});
        if (state){
            return Msg.SUCCESS();
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 ${entity} (非逻辑删除)
     * @Param: [id]
     * @Author: ${author}
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 ${entity} (非逻辑删除)")
    @RequestMapping(value = "/delete${entity}",method = RequestMethod.DELETE)
    <#if restControllerStyle>
    <#else>@ResponseBody
    </#if>
    private Msg delete${entity}(@RequestParam(value = "id",defaultValue = "no")String id){
        boolean state=baseService.removeById(id);
        if (state){
            return Msg.SUCCESS();
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 ${entity}
     * @Param: [${table.name}]
     * @Author: ${author}
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 ${entity}")
    @RequestMapping(value = "/update${entity}",method = RequestMethod.POST)
    <#if restControllerStyle>
    <#else>@ResponseBody
    </#if>
    private Msg update${entity}(@RequestBody ${entity} ${table.name}){
        boolean state=baseService.updateById(${table.name});
        if (state){
            return Msg.SUCCESS();
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 ${entity}
     * @Param: [${table.name}]
     * @Author: ${author}
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 ${entity}")
    @RequestMapping(value = "/select${entity}s",method = RequestMethod.POST)
    <#if restControllerStyle>
    <#else>@ResponseBody
    </#if>
    private Msg select${entity}s(@RequestBody ${entity} ${table.name},@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "1")Integer pageSize){
        Page<${entity}> page=new Page<${entity}>(pageNum,pageSize);
        IPage<${entity}> state=baseService.page(page,new QueryWrapper<${entity}>().setEntity(${table.name}));
        if (state.getSize()>0){
            return Msg.SUCCESS().add("resultSet",state);
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 ${entity}
     * @Param: [id]
     * @Author: ${author}
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 ${entity}")
    @RequestMapping(value = "/select${entity}ById",method = RequestMethod.POST)
    <#if restControllerStyle>
    <#else>@ResponseBody
    </#if>
    private Msg select${entity}ById(@RequestParam(value = "id",defaultValue = "no")String id){
    ${entity} state=baseService.getById(id);
        if (state!=null){
            return Msg.SUCCESS().add("result",state);
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 ${entity}
     * @Param: [id]
     * @Author: ${author}
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 ${entity}")
    @RequestMapping(value = "/import${entity}",method = RequestMethod.POST)
    <#if restControllerStyle>
    <#else>@ResponseBody
    </#if>
    private Msg import${entity}(@RequestParam(value = "file")MultipartFile file){
        boolean state=baseService.imput${entity}s(file);
        if (state){
            return Msg.SUCCESS().add("result",state);
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 ${entity}
     * @Param: [id]
     * @Author: ${author}
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 ${entity}")
    @RequestMapping(value = "/export${entity}",method = RequestMethod.POST)
    <#if restControllerStyle>
    <#else>@ResponseBody
    </#if>
    private void export${entity}(@RequestBody ${entity} ${table.name},HttpServletResponse response){
        List<${entity}> resultSet=baseService.list(new QueryWrapper<${entity}>().setEntity(${table.name}));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet,${table.name}.toString(),"1",${entity}.class,LocalDateTime.now().format(df)+".xlsx",response);
    }

}
</#if>
