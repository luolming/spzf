package com.spzf.web.controller.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spzf.common.PagerModel;
import com.spzf.common.Permission;
import com.spzf.common.Query;
import com.spzf.constant.PermissionConatant;
import com.spzf.constant.ReturnCode;
import com.spzf.model.icsystem.ICSystem;
import com.spzf.model.module.Module;
import com.spzf.model.privilege.SystemPrivilegeValue;
import com.spzf.service.icsystem.IICSystemService;
import com.spzf.service.module.IModuleService;
import com.spzf.service.privilege.ISystemPrivilegeValueService;
import com.spzf.utils.JsonUtils;
import com.spzf.vo.SimpleReturnVo;
import com.spzf.web.controller.BaseController;

/**
 * 
 * @Title:模块
 * @Description:模块管理
 * @Author:
 * @Since:2014-3-27
 * @Version:1.1.0
 */
@Controller
@RequestMapping("/managment/module")
public class ModuleController extends BaseController {
	private static Logger logger = Logger.getLogger(ModuleController.class);

	@Resource
	private IModuleService moduleService;

	@Resource
	private IICSystemService iICSystemService;

	@Resource
	private ISystemPrivilegeValueService systemPrivilegeValueService;

	/**
	 * 跳转到模块分页列表
	 * 
	 * @author 
	 * @return 模块列表页面路径
	 */
	@RequestMapping("/list")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.R)
	public String list(String sessionId, ModelMap model) {
		model.put("sessionId", sessionId);
		return "/module/module_page";
	}

	/**
	 * 异步分页数据
	 * 
	 * @author 
	 * @param query
	 *            分页参数对象，主要参数：pageSize:每页显示的条数,pageIndex：页数
	 * @param module
	 *            筛选条件对象，将会作为查询的条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxlist")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.R)
	public String ajaxList(Query query, Module module) {
		PagerModel<Module> pm = null;
		try {
			pm = this.moduleService.getPagerModule(module, query);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ModuleController-ajaxList:" + e.getMessage());
		}
		return JsonUtils.getPmJson(pm);
	}

	/**
	 * 跳转到模块添加页面
	 * 
	 * @author 
	 * @return 模块添加页面路径
	 */
	@RequestMapping("/insertUI")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.C)
	public String insert(String systemId, String pid, ModelMap model, String sessionId) {
		model.put("systemId", systemId);
		model.put("sessionId", sessionId);
		if (StringUtils.isNotBlank(pid)) {
			model.put("pid", pid);
		}
		return "/module/module_insert";
	}

	/**
	 * 跳转到模块修改页面
	 * 
	 * @param model
	 *            给页面传值
	 * @author 
	 * @return 模块修改页面路径
	 */
	@RequestMapping("/updateUI")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.U)
	public String updateUI(ModelMap model, String sessionId) {
		model.put("sessionId", sessionId);
		return "/module/module_update";
	}

	/**
	 * 根据id获取模块对象数据
	 * 
	 * @author 
	 * @param id
	 *            模块id
	 * @return 模块对象json字符串
	 */
	@ResponseBody
	@RequestMapping("/ajaxUpdate")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.U)
	public String ajaxUpdate(String id) {
		Module module = null;
		try {
			module = this.moduleService.getModuleById(id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-ajaxUpdate:" + e.getMessage());
		}
		return JsonUtils.toJson(module);
	}

	/**
	 * 添加模块对象
	 * 
	 * @author
	 * @param request
	 * @param module
	 *            模块对象，用于接收表单提交的参数
	 * @return 响应的json字符串
	 */
	@ResponseBody
	@RequestMapping("/insert")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.C)
	public String insert(Module module) {
		SimpleReturnVo vo = null;
		try {
			this.moduleService.insertModule(module);
			vo = new SimpleReturnVo(ReturnCode.SUCCESS, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-insert:" + e.getMessage());
			vo = new SimpleReturnVo(ReturnCode.FAIL, "异常");
		}
		return JsonUtils.toJson(vo);
	}

	/**
	 * 修改模块对象
	 * 
	 * @author 
	 * @param request
	 * @param module
	 *            模块对象，用于接收表单提交的参数
	 * @return 响应的json字符串
	 */
	@ResponseBody
	@RequestMapping("/update")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.U)
	public String update(Module module) {
		SimpleReturnVo vo = null;
		try {
			this.moduleService.updateModule(module);
			vo = new SimpleReturnVo(ReturnCode.SUCCESS, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-insert:" + e.getMessage());
			vo = new SimpleReturnVo(ReturnCode.FAIL, "异常");
		}
		return JsonUtils.toJson(vo);
	}

	/**
	 * 删除
	 * 
	 * @author 
	 * @param ids
	 *            id,id,id格式的字符串
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.D)
	public String delete(String ids) {
		SimpleReturnVo vo;
		if (StringUtils.isNotBlank(ids)) {
			String[] id = ids.split(",");
			try {
				this.moduleService.deleteModules(id);
			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("ModuleController-delete:" + e.getMessage());
				vo = new SimpleReturnVo(ReturnCode.FAIL, "异常");
				return JsonUtils.toJson(vo);
			}
		}
		vo = new SimpleReturnVo(ReturnCode.SUCCESS, "成功");
		return JsonUtils.toJson(vo);
	}

	/**
	 * 
	 * @return 系统列表json数组字符串
	 * @Description: 获取所有系统列表树
	 */
	@ResponseBody
	@RequestMapping("/getsystems")
	public String getSystems() {
		List<ICSystem> list = null;
		try {
			list = this.iICSystemService.getAllIcSystem(null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-getSystems:" + e.getMessage());
		}
		return JsonUtils.toJson(list);
	}

	/**
	 * 
	 * @param module
	 *            查询条件
	 * @return
	 * @Description:获取所有模块
	 */
	@ResponseBody
	@RequestMapping("getAll")
	public String getAll(Module module) {
		List<Module> list = null;
		try {
			list = this.moduleService.getAllModulePri(module);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-getAll:" + e.getMessage());
		}
		return JsonUtils.toJson(list);
	}

	@ResponseBody
	@RequestMapping("getModulePriVal")
	public String getModulePriVal(Module module) {
		List<Module> list = null;
		try {
			list = this.moduleService.getAllModule(module);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-getModulePriVal:" + e.getMessage());
		}
		return JsonUtils.toJson(list);
	}

	@ResponseBody
	@RequestMapping("getModuleDataPri")
	public String getModuleDataPri(Module module) {
		List<Module> list = null;
		try {
			list = this.moduleService.getAllModule(module);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-getModuleDataPri:" + e.getMessage());
		}
		return JsonUtils.toJson(list);
	}

	/**
	 * 
	 * @return
	 * @Description:跳转到操作权限页面
	 */
	@RequestMapping("/insertPriValUI")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.C)
	public String insertPriValUI(String moduleId, String systemId, ModelMap model, String sessionId, String moduleSn) {
		model.put("sessionId", sessionId);
		model.put("moduleId", moduleId);
		model.put("moduleSn", moduleSn);
		model.put("systemId", systemId);
		List<SystemPrivilegeValue> list = null;
		try {
			list = systemPrivilegeValueService.getPval(systemId);
			Module module = moduleService.getModuleById(moduleId);
			List<SystemPrivilegeValue> havePrivales = new ArrayList<SystemPrivilegeValue>();
			for (SystemPrivilegeValue spv : list) {
				if (module.getPermission(spv.getPosition())) {
					havePrivales.add(spv);
				}
			}
			list.removeAll(havePrivales);
			model.put("priVals", list);
		} catch (Exception e) {
			logger.error(e);
		}
		return "/module/module_insertpri_value";
	}

	/**
	 * Description:获取操作权限列表
	 * 
	 * @param moduleId
	 *            模块id
	 * @param systemId
	 *            系统id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllPriVal")
	public String getAllPriVal(String moduleId, String systemId) {
		List<SystemPrivilegeValue> list = null;
		try {
			list = systemPrivilegeValueService.getPval(systemId);
			Module module = moduleService.getModuleById(moduleId);
			List<SystemPrivilegeValue> havePrivales = new ArrayList<SystemPrivilegeValue>();
			for (SystemPrivilegeValue spv : list) {
				if (module.getPermission(spv.getPosition())) {
					havePrivales.add(spv);
				}
			}
			list.removeAll(havePrivales);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-getAllPriVal:" + e.getMessage());
		}
		return JsonUtils.toJson(list);
	}
	
	/**
	 * Description:效验模块标识是否重复
	 */
	@ResponseBody
	@RequestMapping("/checkSnExsits")
	public String checkSnExsits(String sn, String systemId , String id) {
		Module module=new Module();
		module.setSn(sn);
		module.setSystemId(systemId);
		try {
			List<Module> list=this.moduleService.getAllModule(module);
			if(StringUtils.isNotBlank(id)){//更新的时候
				Module m=this.moduleService.getModuleById(id);
				if(m.getSn().equals(sn)){
					return "0";
				}else{
					if(list!=null && list.size()>0){
						return "1";
					}
				}
			}else{//新增的时候
				if(list!=null && list.size()>0){
					return "1";
				}
			}
		} catch (Exception e) {
			logger.debug("ModuleController-checkSnExsits:" + e.getMessage());
			e.printStackTrace();
		}
		return "0";
	}

	/**
	 * 得到系统所有的模块和模块值，当然的也做了每个权限值是否勾选判断
	 * 
	 * @param systemSn 系统标识
	 * @param type role或者user
	 * @param releaseId roleid或者userid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllPriValBySystemSn")
	public String getAllPriValBySystemSn(String systemSn, String type, String releaseId) {
		List<Module> datas = null;
		Map<String , Object> returnMap=new HashMap<String, Object>();//easyUI树数据结构
		try {
			datas = moduleService.getAllModuleBySystemSnAndReleaseId(systemSn, type, releaseId);
			returnMap.put("total", datas.size());
			returnMap.put("rows", datas);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-getAllPriVal:" + e.getMessage());
		}
		return JsonUtils.toJson(returnMap);
	}
	/**
	 * 删除模块权限值
	 * @param systemPrivilegeValueId
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/deletePriVal")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.D)
	public String deletePriVal(String systemPrivilegeValueId, String moduleId) throws Exception {
		SimpleReturnVo vo = null;
		moduleService.deletePriVal(systemPrivilegeValueId, moduleId);
		vo = new SimpleReturnVo(ReturnCode.SUCCESS, "成功");
		return JsonUtils.toJson(vo);
	}
	
	/**
	 * @param priVal
	 * @return
	 * @Description:保存权限值
	 */
	@ResponseBody
	@RequestMapping("/insertPriVal")
	@Permission(systemSn = "privilege", moduleSn = "module", value = PermissionConatant.C)
	public String insertPriVal(String pvs, String moduleId) {
		SimpleReturnVo vo = null;
		try {
			if (StringUtils.isNotBlank(pvs)) {
				String[] datas = pvs.split(",");
				List<Integer> positions = new ArrayList<Integer>();
				for (String data : datas) {
					positions.add(Integer.parseInt(data));
				}
				moduleService.addPriVal(positions, moduleId);
				vo = new SimpleReturnVo(ReturnCode.SUCCESS, "成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ModuleController-insertPriVal:" + e.getMessage());
			vo = new SimpleReturnVo(ReturnCode.FAIL, "异常");
		}
		return JsonUtils.toJson(vo);
	}
}
