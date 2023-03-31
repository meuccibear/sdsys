package com.adsys.util;

import java.util.*;
import net.sf.json.JSONArray;
 
public class TreeBuilder {
        
         List<TreeBuilder.Node>nodes = new ArrayList<TreeBuilder.Node>();
 
         public TreeBuilder(List<Node> nodes) {
                   super();
                   this.nodes= nodes;
         }
        
         /**
          * 构建JSON树形结构
          * @return
          */
         public String buildJSONTree() {
                   List<Node>nodeTree = buildTree();
                   JSONArray jsonArray = JSONArray.fromObject(nodeTree);
                   return jsonArray.toString();
         }
        
         /**
          * 构建树形结构
          * @return
          */
         public List<Node> buildTree() {
                   List<Node>treeNodes = new ArrayList<Node>();
                   List<Node>rootNodes = getRootNodes();
                   for (Node rootNode : rootNodes) {
                            buildChildNodes(rootNode);
                            treeNodes.add(rootNode);
                   }
                   return treeNodes;
         }
        
         /**
          * 递归子节点
          * @param node
          */
       public void buildChildNodes(Node node) {
	       List<Node> children = getChildNodes(node); 
	       if (!children.isEmpty()) {
	            for(Node child : children) {
	                     buildChildNodes(child);
	            } 
	            node.setNodes(children);
	       }
         }
 
         /**
          * 获取父节点下所有的子节点
          * @param nodes
          * @param pnode
          * @return
          */
         public List<Node> getChildNodes(Node pnode) {
                   List<Node>childNodes = new ArrayList<Node>();
                   for (Node n : nodes){
                            if (pnode.getId().equals(n.getPid())) {
                                     childNodes.add(n);
                            }
                   }
                   return childNodes;
         }
        
         /**
          * 判断是否为根节点
          * @param nodes
          * @param inNode
          * @return
          */
         public boolean rootNode(Node node) {
                   boolean isRootNode = true;
                   for (Node n : nodes){
                            if (node.getPid().equals(n.getId())) {
                                     isRootNode= false;
                                     break;
                            }
                   }
                   return isRootNode;
         }
        
         /**
          * 获取集合中所有的根节点
          * @param nodes
          * @return
          */
         public List<Node> getRootNodes() {
                   List<Node>rootNodes = new ArrayList<Node>();
                   for (Node n : nodes){
                            if (rootNode(n)) {
                                     rootNodes.add(n);
                            }
                   }
                   return rootNodes;
         }

         public static class Node {
             
             private String id;
             private String pid;
             private String text;
             private String gpath;
             private Map<String,Object> state;
             private List<Node> nodes;
            
             public Node() {}

             public Node(String id, String pid, String text) {
                      super();
                      this.id =id;
                      this.pid =pid;
                      this.text =text;
                      this.state = new HashMap<String,Object>();
                      this.state.put("checked", false);
                      this.state.put("selected", false);
             }
            
             public String getId() {
                      return id;
             }
             public void setId(String id) {
                      this.id =id;
             }
             public String getPid() {
                      return pid;
             }
             public void setPid(String pid) {
                      this.pid =pid;
             }
             public String getText() {
                      return text;
             }
             public void setText(String text) {
                      this.text =text;
             }
             public String getPath() {
                 return gpath;
             }
             public void setPath(String path) {
                 this.gpath =path;
             }
             public List<Node> getNodes() {
                      return nodes;
             }
             public void setNodes(List<Node> nodes) {
                      this.nodes= nodes;
             }
             public Map<String,Object> getState() {
                 return state;
	         }
	         public void setState(Map<String,Object> state) {
	                 this.state= state;
	         }
         }
         
         public static void main(String[] args) {
             
             List<TreeBuilder.Node> nodes = new ArrayList<TreeBuilder.Node>();
             Node p1 = new Node("01", "0","01");
             Node p6 = new Node("02", "0","02");
             Node p7 = new Node("0201", "02","0201");
             Node p2 = new Node("0101", "01","0101");
             Node p3 = new Node("0102", "01","0102");
             Node p4 = new Node("010101", "0101","010101");
             Node p5 = new Node("010102", "0101","010102");
            
             nodes.add(p1);
             nodes.add(p2);
             nodes.add(p3);
             nodes.add(p4);
             nodes.add(p5);
             nodes.add(p6);
             nodes.add(p7);
            
             TreeBuilder treeBuilder = new TreeBuilder(nodes);
             System.out.println(treeBuilder.buildJSONTree());
   }
   }