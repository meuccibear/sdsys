package com.adsys.util;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class ElTreeBuilder {

    private org.apache.log4j.Logger logger = Logger.getLogger(this.getClass());


    List<ElTreeBuilder.Node> nodes = new ArrayList<ElTreeBuilder.Node>();

    public ElTreeBuilder(List<Node> nodes) {
        super();
        this.nodes = nodes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {

        private Integer id;
        private Integer pid;
        private String label;

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NodeBuild {

        private String value;

        private String label;

        private List<NodeBuild> children;

    }
}
