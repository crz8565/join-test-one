package com.yizhi.student.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yizhi.common.annotation.Log;
import com.yizhi.common.utils.*;
import com.yizhi.student.domain.MajorDO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.yizhi.student.domain.StudentInfoDO;
import com.yizhi.student.service.StudentInfoService;

/**
 * 生基础信息表
 */
 
@Controller
@RequestMapping("/student/studentInfo")
public class StudentInfoController {




	@Autowired
	private StudentInfoService studentInfoService;
    //
	@Log("学生信息保存")
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("student:studentInfo:add")
	public Object save(StudentInfoDO studentInfoDO){
		Map map = new HashMap<>();
		studentInfoDO.setAddTime(new Date());
		if (studentInfoService.save(studentInfoDO)>0){
			map.put("code",0);
		}else {
			map.put("msg","服务器异常");
		}
		return map;
	}

	/**
	 * 可分页 查询
	 */
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("student:studentInfo:studentInfo")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
		if (params.get("sort")!=null) {
			params.put("sort",BeanHump.camelToUnderline(params.get("sort").toString()));
		}
		//查询列表数据
		Query query = new Query(params);
		List<StudentInfoDO> majorList = studentInfoService.list(query);
		int total = studentInfoService.count(query);
		PageUtils pageUtils = new PageUtils(majorList, total,query.getCurrPage(),query.getPageSize());
		return pageUtils;
	}


	/**
	 * 修改
	 */
	@Log("学生基础信息表修改")
	@ResponseBody
	@PostMapping("/update")
	@RequiresPermissions("student:studentInfo:edit")
	public Object update(StudentInfoDO studentInfo){
		Map map = new HashMap<>();
		studentInfo.setEditTime(new Date());
		int flag = studentInfoService.update(studentInfo);
		if (flag > 0){
			map.put("code",0);
		}else {
			map.put("msg","服务器异常");
		}
		return map;
	}

	/**
	 * 删除
	 */
	@Log("学生基础信息表删除")
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("student:studentInfo:remove")
	public Object remove( Integer id){
		Map map = new HashMap<>();
		int flag = studentInfoService.remove(id);
		if (flag > 0){
			map.put("code",0);
			map.put("msg","操作成功");
		}else {
			map.put("msg","服务器异常");
		}
		return map;
	}
	
	/**
	 * 批量删除
	 */
	@Log("学生基础信息表批量删除")
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("student:studentInfo:batchRemove")
	public Object remove(@RequestParam("ids[]") Integer[] ids){
		Map map = new HashMap<>();
		int flag = studentInfoService.batchRemove(ids);
		if (flag > 0){
			map.put("code",0);
			map.put("msg","操作成功");
		}else {
			map.put("msg","服务器异常");
		}
		return map;
	}


	//前后端不分离 客户端 -> 控制器-> 定位视图
	/**
	 * 学生管理 点击Tab标签 forward页面
	 */
	@GetMapping()
	@RequiresPermissions("student:studentInfo:studentInfo")
	String StudentInfo(){
		return "student/studentInfo/studentInfo";
	}

	/**
	 * 更新功能 弹出View定位
	 */
	@GetMapping("/edit/{id}")
	@RequiresPermissions("student:studentInfo:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		StudentInfoDO studentInfo = studentInfoService.get(id);
		model.addAttribute("studentInfo", studentInfo);
		return "student/studentInfo/edit";
	}

	/**
	 * 学生管理 添加学生弹出 View
	 */
	@GetMapping("/add")
	@RequiresPermissions("student:studentInfo:add")
	String add(){
	    return "student/studentInfo/add";
	}
	
}//end class
