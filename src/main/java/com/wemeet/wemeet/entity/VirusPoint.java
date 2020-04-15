package com.wemeet.wemeet.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

/**
 * 疫情点
 * @author xieziwei99
 * 2020-03-03
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
public class VirusPoint extends BugContent {
    private String description;
}
