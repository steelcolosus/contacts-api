package models.responses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eduardo on 31/03/16.
 */
public class Data {
    private String id;
    private String type;
    private Map<String, Object> attributes = new HashMap<>();
    private List<Relationship> relationships = new ArrayList<>();
}
