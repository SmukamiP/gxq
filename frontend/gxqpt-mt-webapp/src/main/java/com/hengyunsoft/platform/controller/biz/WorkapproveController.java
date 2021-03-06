package com.hengyunsoft.platform.controller.biz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/fast/workapprove")
public class WorkapproveController {


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(ModelMap map) {
        return "fast/workapprove/list";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(ModelMap map) {
        return "fast/workapprove/create";
    }

    @RequestMapping(value = "sendAudit", method = RequestMethod.GET)
    public String sendAudit(@RequestParam long id, ModelMap model) {
        return "fast/workapprove/sendAudit";
    }

    @RequestMapping(value = "acceptAudit", method = RequestMethod.GET)
    public String acceptAudit(@RequestParam Long id, @RequestParam(required = false) String applyTime,
                              @RequestParam(required = false) String applyUserName, ModelMap map) {
        map.put("applyTime", applyTime);
        map.put("applyUserName", applyUserName);
        return "fast/workapprove/acceptAudit";
    }

    @RequestMapping(value = "waitDoAudit", method = RequestMethod.GET)
    public String waitDoAudit(@RequestParam long id, ModelMap map) {
        return "fast/workapprove/waitDoAudit";
    }

    @RequestMapping(value = "extraWorkHandle", method = RequestMethod.GET)
    public String extraWorkHandle(@RequestParam Long id, @RequestParam(required = false) String applyTime,
                                  @RequestParam(required = false) String applyUserName,
                                  @RequestParam(required = false) String handleStatus,ModelMap map) {
        map.put("id", id);
        map.put("applyTime", applyTime);
        map.put("applyUserName", applyUserName);
        map.put("handleStatus", handleStatus);
        return "fast/workapprove/extraWorkHandle";
    }

    @RequestMapping(value = "outsideHandle", method = RequestMethod.GET)
    public String outsideHandle(@RequestParam long id, ModelMap map) {
        return "fast/workapprove/outsideHandle";
    }

    @RequestMapping(value = "supplyHandle", method = RequestMethod.GET)
    public String supplyHandle(@RequestParam long id, ModelMap map) {
        return "fast/workapprove/supplyHandle";
    }

    @RequestMapping(value = "sendAuditView", method = RequestMethod.GET)
    public String sendAuditView(@RequestParam long id, ModelMap map) {
        return "fast/workapprove/sendAuditView";
    }

    @RequestMapping(value = "acceptAuditView", method = RequestMethod.GET)
    public String acceptAuditView(@RequestParam Long id, @RequestParam(required = false) String applyTime, @RequestParam(required = false) String applyUserName, ModelMap map) {
        map.put("applyTime", applyTime);
        map.put("applyUserName", applyUserName);
        return "fast/workapprove/acceptAuditView";
    }

    @RequestMapping(value = "waitDoAuditView", method = RequestMethod.GET)
    public String waitDoAuditView(@RequestParam long id, ModelMap map) {
        return "fast/workapprove/waitDoAuditView";
    }

    @RequestMapping(value = "outsideView", method = RequestMethod.GET)
    public String outsideView(@RequestParam long id, ModelMap map) {
        return "fast/workapprove/outsideView";
    }

    @RequestMapping(value = "supplyView", method = RequestMethod.GET)
    public String supplyView(@RequestParam long id, ModelMap map) {
        return "fast/workapprove/supplyView";
    }

    @RequestMapping(value = "submitApply", method = RequestMethod.GET)
    public String submitApply(@RequestParam Long id, ModelMap map) {
        map.put("id", id);
        return "fast/workapprove/submitApply";
    }

    @RequestMapping(value = "supplyEdit", method = RequestMethod.GET)
    public String supplyEdit(@RequestParam Long id, ModelMap map) {
        return "fast/workapprove/supplyEdit";
    }

    /**
        选择审批人
     */
    @RequestMapping("/selectPeople")
    public String selectPeople(String userId,String userIds,ModelMap model) {
        model.addAttribute("userId",userId);
        model.addAttribute("userIds",userIds);
        return "fast/workapprove/selectPeople";
    }

    /**
        查看详情
     */
    @RequestMapping("/detail")
    public String detail(ModelMap model) {
        return "fast/workapprove/detail";
    }
}
