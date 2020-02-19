package com.wemeet.wemeet.web;

import com.wemeet.wemeet.entity.*;
import com.wemeet.wemeet.pojo.Bug;
import com.wemeet.wemeet.repository.BugContentRepo;
import com.wemeet.wemeet.repository.BugPropertyRepo;
import com.wemeet.wemeet.util.ReturnVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieziwei99
 * 2019-07-17
 */
@RestController
@CrossOrigin(origins = "*")
public class GoldbugController {

    private final BugPropertyRepo bugPropertyRepo;
    private final BugContentRepo bugContentRepo;

    public GoldbugController(BugPropertyRepo bugPropertyRepo, BugContentRepo bugContentRepo) {
        this.bugPropertyRepo = bugPropertyRepo;
        this.bugContentRepo = bugContentRepo;
    }

    @ApiOperation(value = "根据用户当前位置获取种子列表", notes = "范围是以用户坐标为中心5000 × 5000的正方形")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userLon", value = "用户经度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "userLat", value = "用户纬度", required = true, dataType = "double"),
    })
    @RequestMapping(value = "/getAroundBugs", method = RequestMethod.GET)
    public List<Bug> getAroundBugs(@RequestParam double userLon, @RequestParam double userLat) {
        List<BugProperty> bugPropertyList = bugPropertyRepo.
                findByStartLongitudeBetweenAndStartLatitudeBetween(
                        userLon - 2500, userLon + 2500,
                        userLat - 2500, userLat + 2500);
        List<Bug> bugList = new ArrayList<>();
        for (BugProperty bugProperty : bugPropertyList) {
            Bug bug = new Bug();
            bug.setBugProperty(bugProperty);
            switch (bugProperty.getBugContent().getType()) {
                case 0:
                    bug.setMoment((Moment) bugProperty.getBugContent());
                    break;
                case 1:
                    bug.setChoiceQuestion((ChoiceQuestion) bugProperty.getBugContent());
                    break;
                case 2:
                    bug.setNarrativeQuestion((NarrativeQuestion) bugProperty.getBugContent());
                    break;
                case 3:
                    break;
            }
            bugList.add(bug);
        }
        return bugList;
    }

    // just for test
    @GetMapping("/getBug/{id}")
    public Bug getBugById(@PathVariable Long id) throws Exception {
        Bug bug = new Bug();
        BugProperty bugProperty = bugPropertyRepo.findById(id).orElseThrow(Exception::new);
        bug.setBugProperty(bugProperty);
        switch (bugProperty.getBugContent().getType()) {
            case 0:
                bug.setMoment((Moment) bugProperty.getBugContent());
                break;
            case 1:
                bug.setChoiceQuestion((ChoiceQuestion) bugProperty.getBugContent());
                break;
            case 2:
                bug.setNarrativeQuestion((NarrativeQuestion) bugProperty.getBugContent());
                break;
            case 3:
                break;
        }
        return bug;
    }

    /**
     * 增加一条虫子记录
     * 不能增加BugContent
     * 只能依照已存在的BugContent加入，而且不能通过外键指定，要用BugContent的全部来指定
     *
     * @param bugProperty 虫子的基本属性
     * @return 成功返回success
     */
    @ApiOperation(value = "增加一条虫子记录", notes = "不能增加BugContent，只能依照已存在的BugContent加入，而且不能通过外键指定，要用BugContent的全部来指定")
    @ApiImplicitParam(name = "bugProperty", value = "虫子详细实体BugProperty", required = true, dataType = "BugProperty")
    @RequestMapping(value = "/addGoldBug", method = RequestMethod.POST)
    @Deprecated
    public ReturnVO addGoldBug(@RequestBody BugProperty bugProperty) {
        bugPropertyRepo.save(bugProperty);
        return new ReturnVO().setCode(200).setMessage("添加成功");
    }

    @ApiOperation(value = "增加一条虫子内容 - 动态")
    @ApiImplicitParam(name = "bugContent", value = "虫子内容详细实体Moment", required = true, dataType = "Moment")
    @RequestMapping(value = "/addMoment", method = RequestMethod.POST)
    @Deprecated
    public ReturnVO addMoment(@RequestBody Moment bugContent) {
        bugContentRepo.save(bugContent);
        return new ReturnVO().setCode(200).setMessage("添加成功");
    }

    @ApiOperation(value = "增加一条虫子内容 - 选择题")
    @ApiImplicitParam(name = "bugContent", value = "虫子内容详细实体ChoiceQuestion", required = true, dataType = "ChoiceQuestion")
    @RequestMapping(value = "/addChoiceQuestion", method = RequestMethod.POST)
    @Deprecated
    public ReturnVO addChoiceQuestion(@RequestBody ChoiceQuestion bugContent) {
        bugContentRepo.save(bugContent);
        return new ReturnVO().setCode(200).setMessage("添加成功");
    }

    @ApiOperation(value = "增加一条虫子内容 - 叙述题")
    @ApiImplicitParam(name = "bugContent", value = "虫子内容详细实体NarrativeQuestion", required = true, dataType = "NarrativeQuestion")
    @RequestMapping(value = "/addNarrativeQuestion", method = RequestMethod.POST)
    @Deprecated
    public ReturnVO addNarrativeQuestion(@RequestBody NarrativeQuestion bugContent) {
        bugContentRepo.save(bugContent);
        return new ReturnVO().setCode(200).setMessage("添加成功");
    }

    @ApiOperation(value = "增加一个包含内容的虫子")
    @ApiImplicitParam(name = "bug", value = "包含内容的虫子详细实体bug", required = true, dataType = "Bug")
    @PostMapping("/addBug")
    public ReturnVO addBug(@RequestBody Bug bug) {
        BugContent bugContent;
        if (bug.getMoment() != null) {
            bugContent = bug.getMoment();
        } else if (bug.getChoiceQuestion() != null) {
            bugContent = bug.getChoiceQuestion();
        } else if (bug.getNarrativeQuestion() != null) {
            bugContent = bug.getNarrativeQuestion();
        } else {
            bugContent = null;
        }
        assert bugContent != null;
        bugContentRepo.save(bugContent);

        BugProperty bugProperty = bug.getBugProperty();
        bugProperty.setBugContent(bugContent);
        bugPropertyRepo.save(bugProperty);

        return new ReturnVO().setCode(200).setMessage("添加成功");
    }
}
