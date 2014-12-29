package com.codebattle.model.meta;

import java.util.HashMap;
import java.util.Map;

/**
 * Bundle will store the arguments in hashmap, and skill extract the data from bundle
 *
 * NOTE: when used at skill, the following args will be append into
 * Stage: GameStage
 * Skill: Skill
 * SkillEmiiter: GameObject
 * Targrt x , y: int
 * @author williamd
 *
 */
public class Bundle {
    private Map<String, Object> map = new HashMap<String, Object>();

    public void bind(String key, Object val) {
        this.map.put(key, val);
    }

    @SuppressWarnings("unchecked")
    public <T> T extract(String key, Class<T> type) {
        return (T) this.map.get(key);
    }
}
