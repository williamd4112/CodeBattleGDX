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
    private final Map<String, Object> map = new HashMap<String, Object>();

    public void bind(final String key, final Object val) {
        this.map.put(key, val);
    }

    @SuppressWarnings("unchecked")
    public <T> T extract(final String key, final Class<T> type) {
        return (T) this.map.get(key);
    }
}
