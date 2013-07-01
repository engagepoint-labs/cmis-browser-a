package com.engagepoint.labs.web.controllers;

import com.engagepoint.labs.core.models.FSFolder;
import com.engagepoint.labs.core.models.FSObject;
import com.engagepoint.labs.core.service.CMISService;
import com.engagepoint.labs.core.service.CMISServiceImpl;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: bogdan.ezapenkin
 * Date: 6/17/13
 * Time: 4:37 PM
 */

@ManagedBean(name = "treeBean")
@ApplicationScoped
public class TreeBean implements Serializable {

    private boolean backButtonDisabled = true;
    private boolean forwardButtonDisabled = true;
    private TreeNode main;
    private TreeNode selectedNodes;
    private FSObject selectedFSObject;
    private FSFolder parent = new FSFolder();
    private CMISService CMISService = CMISServiceImpl.getService();
    private static Logger logger = Logger.getLogger(TreeBean.class.getName());
    private List<FSObject> fsList;
    private List<FSObject> navigationList;

    private static int number = 2;

    private int backCounter = 0;


    public TreeBean() {
        navigationList = new LinkedList<FSObject>();
        updateTree();
    }

    public void updateTree() {
        logger.log(Level.INFO, "UPDATING... TREEEE...");
        FSFolder root = CMISService.getRootFolder();
        parent.setPath("/");
        fsList = CMISService.getChildren(parent);
        main = new DefaultTreeNode("Main", null);
        TreeNode node0 = new DefaultTreeNode(root, main);
        SubObjects(parent, node0);
    }

    private void SubObjects(FSFolder parent, TreeNode treenodeparent) {
        List<FSObject> children = CMISService.getChildren(parent);
        for (FSObject i : children) {
            if (i instanceof FSFolder) {
                TreeNode treeNode = new DefaultTreeNode(i, treenodeparent);
                SubObjects((FSFolder) i, treeNode);
            }
        }
    }

    public void setSelectedNode(TreeNode selectedNodes) {
        setForwardButtonDisabled(true);
        if (navigationList.size() == 1 && isBackButtonDisabled()) {
            setBackButtonDisabled(false);
        }
        this.selectedNodes = selectedNodes;
        FSObject tmp = (FSObject) selectedNodes.getData();
        logger.log(Level.INFO, "setSelectedNode  tmp null? - " + (tmp == null));
        setSelectedFSObject(tmp);

        int index = navigationList.size() - backCounter;

        logger.log(Level.INFO, "INDEX: " + index);
        int temp = 0;
        for (; index < navigationList.size() + temp; ++index) {
            int s = temp++;
            logger.log(Level.INFO, "object: " + navigationList.get(index - s).getName() + " REMOVED");
            setNumber(getNumber()-1);
            navigationList.remove(index - s);
        }
        //TODO HARDCORE LIST[0] = ROOT
        navigationList.add(selectedFSObject);

        for (int i = 0; i < navigationList.size(); i++) {
            logger.log(Level.INFO, navigationList.get(i).getName());
        }

        if (selectedFSObject.getPath() == null) {
            parent.setPath("/");
            selectedFSObject.setPath("/");
        } else {
            parent.setPath(selectedFSObject.getPath());
        }
        fsList = CMISService.getChildren(parent);
    }

    public void backButton() {
        logger.log(Level.INFO, "SECOND");
        ++backCounter;
        logger.log(Level.INFO, "SIZE: " + navigationList.size());
        logger.log(Level.INFO, "NUMBER: " + getNumber());
        FSObject currentObject = navigationList.get(navigationList.size() - getNumber());
        setNumber(getNumber() + 1);
        if ((navigationList.size() - getNumber()) < 0) {
            setBackButtonDisabled(true);
        }
        setForwardButtonDisabled(false);
        if (selectedFSObject.getPath() == null) {
            parent.setPath("/");
            selectedFSObject.setPath("/");
        } else {
            parent.setPath(currentObject.getPath());
        }
        fsList = CMISService.getChildren(parent);
    }

    public void forwardButton() {
        FSObject currentObject = navigationList.get(navigationList.size() - getNumber() + 2);
        setNumber(getNumber() - 1);
        if (getNumber() <= 2) {
            setForwardButtonDisabled(true);
            System.out.println("fffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }
        setBackButtonDisabled(false);


        System.out.println("FORWARD BUTTON________________________________________" + currentObject.getName());

        if (selectedFSObject.getPath() == null) {
            parent.setPath("/");
            selectedFSObject.setPath("/");
        } else {
            parent.setPath(currentObject.getPath());
        }
        fsList = CMISService.getChildren(parent);
        System.out.println(navigationList.size() + "size111111111111111111111111111111111111111111111111");
        System.out.println(getNumber() + "number11111111111111111111111111111111111111111111111111111111");
    }

    public void onRowSelect(SelectEvent event) {
        if (event == null) {
            logger.log(Level.INFO, "EVENT NULL");
        }
        this.selectedFSObject = (FSObject) event.getObject();
        logger.log(Level.INFO, "onRowSelect: " + selectedFSObject.getName());
    }

    public static int getNumber() {
        return number;
    }

    public static void setNumber(int number) {
        TreeBean.number = number;
    }

    public TreeNode getRoot() {
        return main;
    }

    public TreeNode getSelectedNode() {
        return selectedNodes;
    }

    public List<FSObject> getFsList() {
        return fsList;
    }

    public void setFsList(List<FSObject> fsList) {
        this.fsList = fsList;
    }

    public FSObject getSelectedFSObject() {
        return selectedFSObject;
    }

    public void setSelectedFSObject(FSObject sn) {
        logger.log(Level.INFO, "setSelectedFSObject sn - null? - " + (sn == null));
        if (sn != null)
            this.selectedFSObject = sn;
    }

    public FSFolder getParent() {
        return parent;
    }

    public void setParent(FSFolder parent) {
        this.parent = parent;
    }

    public boolean isForwardButtonDisabled() {
        return forwardButtonDisabled;
    }

    public void setForwardButtonDisabled(boolean forwardButtonDisabled) {
        this.forwardButtonDisabled = forwardButtonDisabled;
    }

    public boolean isBackButtonDisabled() {
        return backButtonDisabled;
    }

    public void setBackButtonDisabled(boolean backButtonDisabled) {
        this.backButtonDisabled = backButtonDisabled;
    }
}