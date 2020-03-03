package com.wemeet.wemeet.pojo;

import com.wemeet.wemeet.entity.BugProperty;
import com.wemeet.wemeet.entity.ChoiceQuestion;
import com.wemeet.wemeet.entity.Moment;
import com.wemeet.wemeet.entity.NarrativeQuestion;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author xieziwei99
 * 2019-09-14
 * 接收前端传来的数据（存入一个bug的完整信息，包括BugProperty和BugContent）
 */
@Getter
@Setter
public class Bug {

    private BugProperty bugProperty;
    private Moment moment;
    private ChoiceQuestion choiceQuestion;
    private NarrativeQuestion narrativeQuestion;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bug bug = (Bug) o;
        return Objects.equals(bugProperty, bug.bugProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bugProperty);
    }
}
