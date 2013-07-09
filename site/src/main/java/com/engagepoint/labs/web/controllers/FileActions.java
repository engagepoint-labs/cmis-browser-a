package com.engagepoint.labs.web.controllers;

import com.engagepoint.labs.core.models.FSFile;
import com.engagepoint.labs.core.service.CMISService;
import com.engagepoint.labs.core.service.CMISServiceImpl;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: r.reznichenko
 * Date: 7/9/13
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */

@ManagedBean(name = "fileActions")
@SessionScoped
public class FileActions implements Serializable {

    private CMISService cmisService;
    private static Logger logger;
    private boolean selectedIsFile;

    public FileActions() {
        cmisService = CMISServiceImpl.getService();
        logger = Logger.getLogger(FileActions.class.getName());
        selectedIsFile = false;
    }

    public StreamedContent download(FSFile file) throws IllegalArgumentException{
        InputStream inputStream = cmisService.getInputStream(file);
        return new DefaultStreamedContent(inputStream, file.getMimetype(), file.getName());
    }

    public boolean isSelectedIsFile() {
        return selectedIsFile;
    }

    public void setSelectedIsFile(boolean selectedIsFile) {
        this.selectedIsFile = selectedIsFile;
    }
}
